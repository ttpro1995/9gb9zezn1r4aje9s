package com.life.es;

import com.life.common.ECode;
import com.life.common.ESConfiguration;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ESController {

    protected static TransportClient _cli = null;
    protected static final Logger LOGGER = LogManager.getLogger(ESController.class);
    private final String m_strIndexName;
    private final AtomicLong m_lastRestartTime;

    protected ESController(String index_name) {
        m_strIndexName = index_name;
        m_lastRestartTime = new AtomicLong(System.currentTimeMillis());
    }

    public boolean exist() {
        return _cli.admin().indices().exists(new IndicesExistsRequest(m_strIndexName)).actionGet().isExists();
    }

    public boolean exist(String type) {
        try {
            //ClusterState cs = _cli.admin().cluster().prepareState().setFilterIndices(m_strIndexName).execute().actionGet().getState();
            ClusterState cs = _cli.admin().cluster().prepareState().setIndices(m_strIndexName).execute().actionGet().getState();

            IndexMetaData imd = cs.getMetaData().index(m_strIndexName);
            if (imd != null) {
                MappingMetaData mmd = imd.mapping(type);
                if (mmd != null) {
                    return true;
                }
            }
            return false;
        } catch (ElasticsearchException ex) {
            LOGGER.error(ex.getMessage());
            return false;
        }

    }

    protected boolean create() {
        CreateIndexResponse response = _cli.admin().indices().prepareCreate(m_strIndexName)
                .setSettings(Settings.builder()
                        .put("index.number_of_shards", ESConfiguration.instance.getNumberOfShards())
                        .put("index.number_of_replicas", ESConfiguration.instance.getNumberOfReplicas()))
                .get();
        return response.isAcknowledged();
    }

    public synchronized boolean start() {
        try {
            if (_cli != null) {
                return false;
            }

            Settings settings = Settings.builder().put("cluster.name", ESConfiguration.instance.getName()).build();
            TransportClient transport = new PreBuiltTransportClient(settings);

            String server = ESConfiguration.getInstance().getHost();
            int port = ESConfiguration.getInstance().getPort();
            transport.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(server), port));

            _cli = transport;
            if (exist() == false) {
                return create();
            }
            return true;
        } catch (NumberFormatException | UnknownHostException exception) {
            LOGGER.error("ESController::start error");
            LOGGER.error(exception);
            LOGGER.error("Servers:" + ESConfiguration.getInstance().getHost());
            LOGGER.error("Ports: " + ESConfiguration.getInstance().getPort());
            try {
                if (_cli != null) {
                    _cli.close();
                }
            } finally {
                _cli = null;
            }
            return false;
        } finally {
            m_lastRestartTime.set(System.currentTimeMillis());
        }
    }

    public synchronized boolean restart() {
        long interval = System.currentTimeMillis() - m_lastRestartTime.get();
        if (interval > ESConfiguration.getInstance().getRestartInterval()) {
            LOGGER.error("Restart ES Connection");
            ESConfiguration.getInstance().reload();
            return start();
        } else {
            return false;
        }
    }

    public boolean stop() {
        try {
            if (_cli != null) {
                _cli.close();
            }
            _cli = null;
            return true;
        } catch (Exception e) {
            LOGGER.error("ESController::stop");
            LOGGER.error(e);
            return false;
        }
    }

    public void refresh() {
        try {
            RefreshRequest rr = new RefreshRequest(m_strIndexName);
            _cli.admin().indices().refresh(rr).actionGet();
        } catch (ElasticsearchException e) {
            LOGGER.error("ESController::refresh");
            LOGGER.error(e);
        } catch (Exception ex) {
            LOGGER.error("ESController::refresh" + ex.toString());
        }
    }

    /**
     * 
     * @param type
     * @param id
     * @return SUCCESS, -NOT_FOUND, -FAIL, -EXCEPTION
     */
    public int delete(String type, String id) {
        try {
            DeleteRequest dr = new DeleteRequest(m_strIndexName, type, id);
            DeleteResponse ret = _cli.delete(dr).actionGet();

            if (ret != null) {
                DocWriteResponse.Result result = ret.getResult();
                if (null == result) {
                    return -ECode.FAIL.getValue();
                } else {
                    switch (result) {
                        case DELETED:
                            return ECode.SUCCESS.getValue();
                        case NOT_FOUND:
                            return -ECode.NOT_FOUND.getValue();
                        default:
                            return -ECode.FAIL.getValue();
                    }
                }
            }
            return -ECode.FAIL.getValue();
        } catch (ElasticsearchException ex) {
            LOGGER.error("ESController::remove" + ex.toString());
            return -ECode.EXCEPTION.getValue();
        } catch (Exception ex) {
            LOGGER.error("ESController::remove" + ex.toString());
            return -ECode.EXCEPTION.getValue();
        }
    }
}

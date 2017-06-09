package com.life.es.handler;

import com.life.common.ECode;
import com.life.common.ESConfiguration;
import com.life.es.entities.Message;
import com.life.es.ActionResult;
import com.life.es.ESController;
import com.life.es.entities.MessageResult;
import com.life.es.entities.MessageSearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MessageController extends ESController {

    protected static String m_strIndexName;
    protected static final ConcurrentHashMap<String, MessageController> m_instances = new ConcurrentHashMap();
    public static final String TYPE = "message";

    protected MessageController(String index_name) {
        super(index_name);
        m_strIndexName = index_name;
    }

    public static synchronized MessageController getInstance(String indexName) {
        if (m_instances.containsKey(indexName) == false) {
            m_instances.put(indexName, new MessageController(indexName));
        }
        return m_instances.get(indexName);

    }

    private XContentBuilder buildMapping() throws IOException {
        //build mapping file
        XContentBuilder xbuilder = XContentFactory.jsonBuilder();

        XContentBuilder mapping = xbuilder.startObject()
                .startObject(TYPE)
                .startObject("properties")
                .startObject("id").field("type", "integer").endObject()
                .startObject("typeData").field("type", "byte").endObject()
                .startObject("data").field("type", "text").endObject()
                .startObject("idConversation").field("type", "integer").endObject()
                .startObject("idUserOwner").field("type", "integer").endObject()
                .startObject("createdTime").field("type", "long").endObject()
                .endObject()
                .endObject().endObject();
        return mapping;
    }

    public boolean createMapping() {
        try {
            if (this.exist(TYPE)) {
                return true;
            }
            XContentBuilder mapping = buildMapping();
            PutMappingResponse resp = _cli.admin().indices().preparePutMapping(m_strIndexName)
                    .setType(TYPE).setSource(mapping).execute().actionGet();
            return resp.isAcknowledged();

        } catch (NoNodeAvailableException nodeEx) {
            LOGGER.error(nodeEx.getMessage());
            //retry
            this.stop();
            this.restart();
            return false;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            if (_cli == null) {
                this.restart();
            }
            return false;
        }
    }

    public ActionResult index(Message message) {

        ActionResult result = new ActionResult(0);
        if (message != null && message.getId() > 0) {
            try {
                IndexRequestBuilder irb = _cli.prepareIndex(m_strIndexName, TYPE, String.valueOf(message.getId()))
                        .setRefreshPolicy(ESConfiguration.getInstance().getRefreshPolicy());
                XContentBuilder xbuilder = XContentFactory.jsonBuilder();
                xbuilder.startObject()
                        .field("id", message.getId())
                        .field("typeData", message.getTypeData())
                        .field("data", message.getData())
                        .field("idConversation", message.getIdConversation())
                        .field("idUserOwner", message.getIdUserOwner())
                        .field("createdTime", message.getCreatedTime())
                        .endObject();
                IndexResponse irsp = irb.setSource(xbuilder).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).execute().actionGet();

                if (message.getId() != Integer.parseInt(irsp.getId())) {
                    result.setError(-ECode.FAIL.getValue());
                    result.setMessage("Index response failed, retValue:" + message.getId());
                }
            } catch (NoNodeAvailableException nodeEx) {
                LOGGER.error(nodeEx.getMessage());
                //retry
                this.stop();
                this.restart();
                result.setError(-ECode.EXCEPTION.getValue());
                result.setMessage("Couldn't Connect To Search Server");
            } catch (IOException | NumberFormatException ex) {
                LOGGER.error(ex.getMessage());
                if (_cli == null) {
                    this.restart();
                }
                result.setError(-ECode.EXCEPTION.getValue());
                result.setMessage(ex.getMessage());
            }
        } else {
            result.setError(-ECode.INVALID_DATA.getValue());
            result.setMessage("Value is Null or UserId is smaller than 1");
        }
        return result;
    }

    public int removeBy(Integer id) {

        return this.delete(TYPE, id.toString());

    }

    public MessageSearchResult getMessageOfConversation(int conversationId, int from, int size) {
        try {
            SearchRequestBuilder srb = _cli.prepareSearch(m_strIndexName);
            FieldSortBuilder timeOrder = SortBuilders.fieldSort("createdTime").order(SortOrder.DESC);
            srb.addSort(timeOrder);

            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.filter(new TermQueryBuilder("idConversation", conversationId));
            srb.setFrom(from).setSize(size);
            srb.setTypes(TYPE);
            srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
            srb.setQuery(queryBuilder);

            //LOGGER.debug(srb.toString());
            SearchResponse resp = srb.execute().actionGet();

            SearchHits hits = resp.getHits();
            LOGGER.debug(resp.toString());
            return parseFromSearchHits(hits);
        } catch (NoNodeAvailableException nodeEx) {
            LOGGER.error(nodeEx.getMessage());
            //retry
            this.stop();
            this.restart();
            return new MessageSearchResult(-ECode.EXCEPTION.getValue());
        } catch (Exception ex) {
            if (_cli == null) {
                this.restart();
            }
            LOGGER.error(ex.getMessage());
            return new MessageSearchResult(-ECode.EXCEPTION.getValue());
        }
    }

    public MessageSearchResult parseFromSearchHits(SearchHits hits) {
        if (hits != null) {

            MessageSearchResult result = new MessageSearchResult(ECode.SUCCESS.getValue());
            result.setTotal(hits.getTotalHits());
            int total = hits.getHits().length;

            List<Message> listResponse = new ArrayList();
            for (int i = 0; i < total; ++i) {
                try {
                    SearchHit hit = hits.getAt(i);
                    Message value = new Message();
                    value.setId(Integer.parseInt(hit.getId()));
                    Map<String, Object> mapFields = hit.sourceAsMap();

                    
                    value.setTypeData(Byte.valueOf(mapFields.get("typeData").toString()));
                    value.setData(mapFields.get("data").toString());
                    value.setIdConversation(Integer.valueOf(mapFields.get("idConversation").toString()));
                    value.setIdUserOwner(Integer.valueOf(mapFields.get("idUserOwner").toString()));
                    value.setCreatedTime(Long.valueOf(mapFields.get("createdTime").toString()));
                    listResponse.add(value);
                } catch (NumberFormatException | ElasticsearchParseException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }

            result.setValue(listResponse);

            return result;
        } else {
            return new MessageSearchResult(-ECode.EXCEPTION.getValue());
        }
    }

    public MessageResult get(int id) {
        MessageResult result = new MessageResult();
        try {
            GetResponse getResponse = _cli.get(new GetRequest(m_strIndexName, TYPE, String.valueOf(id))).actionGet();
            if (getResponse.isExists() == true) {
                result.setError(0);
                Message value = new Message();
                value.setId(Integer.parseInt(getResponse.getId()));
                Map<String, Object> mapFields = getResponse.getSourceAsMap();

                
                value.setTypeData(Byte.valueOf(mapFields.get("typeData").toString()));
                value.setData(mapFields.get("data").toString());
                value.setIdConversation(Integer.valueOf(mapFields.get("idConversation").toString()));
                value.setIdUserOwner(Integer.valueOf(mapFields.get("idUserOwner").toString()));
                value.setCreatedTime(Long.valueOf(mapFields.get("createdTime").toString()));
                result.setValue(value);
            } else {
                result.setError(-ECode.NOT_FOUND.getValue());
            }
        } catch (NumberFormatException | ElasticsearchParseException ex) {
            LOGGER.error(ex.toString());
            result.setError(-ECode.EXCEPTION.getValue());
        }
        return result;
    }

}

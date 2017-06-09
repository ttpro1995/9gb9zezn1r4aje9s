package com.life.es.handler;

import com.life.common.ECode;
import com.life.common.ECodeHelper;
import com.life.common.ESConfiguration;
import com.life.common.PlaceType;
import com.life.es.ActionResult;
import com.life.es.ESController;
import com.life.es.entities.Conversation;
import com.life.es.entities.ConversationResult;
import com.life.es.entities.ConversationSearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
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
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TypeQueryBuilder;
import org.elasticsearch.index.reindex.BulkIndexByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ConversationController extends ESController {

    protected static String m_strIndexName;
    protected static final ConcurrentHashMap<String, ConversationController> m_instances = new ConcurrentHashMap();
    public static final String TYPE = "conversation";

    protected ConversationController(String index_name) {
        super(index_name);
        m_strIndexName = index_name;
    }

    public static synchronized ConversationController getInstance(String indexName) {
        if (m_instances.containsKey(indexName) == false) {
            m_instances.put(indexName, new ConversationController(indexName));
        }
        return m_instances.get(indexName);

    }

    private XContentBuilder buildMapping() throws IOException {
        //build mapping file
        XContentBuilder xbuilder = XContentFactory.jsonBuilder();

        XContentBuilder mapping = xbuilder.startObject()
                .startObject(TYPE)
                .startObject("properties")
                .startObject("uid_idRef").field("type", "text").endObject()
                .startObject("name").field("type", "text").endObject()
                .startObject("idRef").field("type", "integer").endObject()
                .startObject("chatPrivate").field("type", "boolean").endObject()
                .startObject("idConversation").field("type", "integer").endObject()
                .startObject("idUser").field("type", "integer").endObject()
                .startObject("createdTime").field("type", "long").endObject()
                .startObject("updatedTime").field("type", "long").endObject()
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

    public ActionResult index(Conversation conversation) {

        ActionResult result = new ActionResult(0);
        if (conversation != null && StringUtils.isNotBlank(conversation.id)) {
            try {
                IndexRequestBuilder irb = _cli.prepareIndex(m_strIndexName, TYPE, conversation.getId())
                        .setRefreshPolicy(ESConfiguration.getInstance().getRefreshPolicy());
                XContentBuilder xbuilder = XContentFactory.jsonBuilder();
                xbuilder.startObject()
                        .field("uid_idRef", conversation.getId())
                        .field("name", conversation.getName())
                        .field("idRef", conversation.getIdRef())
                        .field("chatPrivate", conversation.isChatPrivate())
                        .field("idConversation", conversation.getIdConversation())
                        .field("idUser", conversation.getIdUser())
                        .field("createdTime", conversation.getCreatedTime())
                        .field("updatedTime", conversation.getUpdatedTime())
                        .endObject();
                IndexResponse irsp = irb.setSource(xbuilder).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).execute().actionGet();

                if (!conversation.getId().equals(irsp.getId())) {
                    result.setError(-ECode.FAIL.getValue());
                    result.setMessage("Index response failed, retValue:" + conversation.getId());
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

    public int removeBy(String id) {

        return this.delete(TYPE, id);

    }

    public boolean exists(int idUser) {
        ConversationSearchResult messageIdentity = conversationOfUser(idUser, 0, 1);
        return ECodeHelper.isSuccess(messageIdentity.getError());
    }

    public ConversationSearchResult conversationOfUser(int idUser, int from, int size) {
        try {
            SearchRequestBuilder srb = _cli.prepareSearch(m_strIndexName);
            FieldSortBuilder timeOrder = SortBuilders.fieldSort("updatedTime").order(SortOrder.DESC);
            srb.addSort(timeOrder);

            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.filter(new TermQueryBuilder("idUser", idUser));
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
            return new ConversationSearchResult(-ECode.EXCEPTION.getValue());
        } catch (Exception ex) {
            if (_cli == null) {
                this.restart();
            }
            LOGGER.error(ex.getMessage());
            return new ConversationSearchResult(-ECode.EXCEPTION.getValue());
        }
    }

    public ConversationSearchResult parseFromSearchHits(SearchHits hits) {
        if (hits != null) {

            ConversationSearchResult result = new ConversationSearchResult(ECode.SUCCESS.getValue());
            result.setTotal((int) hits.getTotalHits());
            int total = hits.getHits().length;

            List<Conversation> listResponse = new ArrayList();
            for (int i = 0; i < total; ++i) {
                try {
                    SearchHit hit = hits.getAt(i);
                    Conversation value = new Conversation();
                    value.setId(hit.getId());
                    Map<String, Object> mapFields = hit.sourceAsMap();

                    value.setName(mapFields.get("name").toString());
                    value.setIdRef(Integer.valueOf(mapFields.get("idRef").toString()));
                    value.setChatPrivate(Boolean.valueOf(mapFields.get("chatPrivate").toString()));
                    value.setIdConversation(Integer.valueOf(mapFields.get("idConversation").toString()));
                    value.setIdUser(Integer.valueOf(mapFields.get("idUser").toString()));
                    value.setCreatedTime(Long.valueOf(mapFields.get("createdTime").toString()));
                    value.setUpdatedTime(Long.valueOf(mapFields.get("updatedTime").toString()));

                    listResponse.add(value);
                } catch (NumberFormatException | ElasticsearchParseException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }

            result.setValue(listResponse);

            return result;
        } else {
            return new ConversationSearchResult(-ECode.EXCEPTION.getValue());
        }
    }

    public boolean exists(int idUser, boolean isChatPrivate, int idRef) {
        ConversationSearchResult messageIdentity = get(idUser, isChatPrivate, idRef);
        return messageIdentity.getTotal() > 0;
    }

    public ConversationSearchResult get(int idUser, boolean isChatPrivate, int idRef) {
        try {
            SearchRequestBuilder srb = _cli.prepareSearch(m_strIndexName);
            FieldSortBuilder timeOrder = SortBuilders.fieldSort("updatedTime").order(SortOrder.DESC);
            srb.addSort(timeOrder);

            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.filter(new TermQueryBuilder("idUser", idUser));
            queryBuilder.filter(new TermQueryBuilder("idRef", idRef));
            queryBuilder.filter(new TermQueryBuilder("chatPrivate", isChatPrivate));

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
            return new ConversationSearchResult(-ECode.EXCEPTION.getValue());
        } catch (Exception ex) {
            if (_cli == null) {
                this.restart();
            }
            LOGGER.error(ex.getMessage());
            return new ConversationSearchResult(-ECode.EXCEPTION.getValue());
        }
    }

    public ConversationResult get(String id) {
        ConversationResult result = new ConversationResult();
        try {
            GetResponse getResponse = _cli.get(new GetRequest(m_strIndexName, TYPE, id)).actionGet();
            if (getResponse.isExists() == true) {
                result.setError(0);
                Conversation value = new Conversation();
                value.setId(getResponse.getId());
                Map<String, Object> mapFields = getResponse.getSourceAsMap();

                value.setName(mapFields.get("name").toString());
                value.setIdRef(Integer.valueOf(mapFields.get("idRef").toString()));
                value.setChatPrivate(Boolean.valueOf(mapFields.get("chatPrivate").toString()));
                value.setIdConversation(Integer.valueOf(mapFields.get("idConversation").toString()));

                value.setIdUser(Integer.valueOf(mapFields.get("idUser").toString()));
                value.setCreatedTime(Long.valueOf(mapFields.get("createdTime").toString()));
                value.setUpdatedTime(Long.valueOf(mapFields.get("updatedTime").toString()));

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

    public boolean update(String id, byte typeData, String data) {
        UpdateRequestBuilder urb = _cli.prepareUpdate(m_strIndexName, TYPE, id).setRefreshPolicy(ESConfiguration.getInstance().getRefreshPolicy());
        try {

            XContentBuilder xbuilder = XContentFactory.jsonBuilder();
            xbuilder.startObject()
                    .field("updatedTime", System.currentTimeMillis())
                    .endObject();

            UpdateResponse actionGet = urb.setDoc(xbuilder).execute().actionGet();
            return actionGet.status() == RestStatus.OK;

        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return false;
    }

    public long updateLastMessageTime(int idConversation) {

        UpdateByQueryRequestBuilder urb = UpdateByQueryAction.INSTANCE.newRequestBuilder(_cli);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.filter(new TermQueryBuilder("idConversation", idConversation));
        queryBuilder.filter(new TypeQueryBuilder(TYPE));

        urb.filter(queryBuilder);
        urb.script(new Script(String.format("ctx._source.updatedTime=%dL", System.currentTimeMillis())));
        BulkIndexByScrollResponse actionGet = urb.execute().actionGet();
        return actionGet.getUpdated();
    }

}

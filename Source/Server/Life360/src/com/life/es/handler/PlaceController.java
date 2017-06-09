package com.life.es.handler;

import com.life.common.ECode;
import com.life.common.ESConfiguration;
import com.life.common.PlaceType;
import com.life.es.entities.Place;
import com.life.entity.base.LatLon;
import com.life.es.ActionResult;
import com.life.es.ESController;
import com.life.es.entities.PlaceResult;
import com.life.es.entities.PlaceSearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
public class PlaceController extends ESController {

    protected static String m_strIndexName;
    protected static final ConcurrentHashMap<String, PlaceController> m_instances = new ConcurrentHashMap();
    public static final String TYPE = "place";

    protected PlaceController(String index_name) {
        super(index_name);
        m_strIndexName = index_name;
    }

    public static synchronized PlaceController getInstance(String indexName) {
        if (m_instances.containsKey(indexName) == false) {
            m_instances.put(indexName, new PlaceController(indexName));
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
                .startObject("name").field("type", "text").endObject()
                .startObject("location").field("type", "geo_point").endObject()
                .startObject("time").field("type", "long").endObject()
                .startObject("zone").field("type", "integer").endObject()
                .startObject("type").field("type", "byte").endObject()
                .startObject("idUserOwner").field("type", "integer").endObject()
                .startObject("idGroup").field("type", "integer").endObject()
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

    /**
     * id = app.getid
     *
     * @param appointment
     * @return SUCCESS, -FAIL, -EXCEPTION, -INVALID_DATA
     */
    public ActionResult index(Place appointment) {

        ActionResult result = new ActionResult(0);
        if (appointment != null && appointment.getId() > 0) {
            try {
                IndexRequestBuilder irb = _cli.prepareIndex(m_strIndexName, TYPE, String.valueOf(appointment.getId()))
                        .setRefreshPolicy(ESConfiguration.getInstance().getRefreshPolicy());
                XContentBuilder xbuilder = XContentFactory.jsonBuilder();
                xbuilder.startObject()
                        .field("id", appointment.getId())
                        .field("name", appointment.getName())
                        .startObject("location").field("lat", appointment.getLatlon().getLat()).field("lon", appointment.getLatlon().getLon()).endObject()
                        .field("time", appointment.getTime())
                        .field("zone", appointment.getZone())
                        .field("type", appointment.getType())
                        .field("idUserOwner", appointment.getIdUserOwner())
                        .field("idGroup", appointment.getIdGroup())
                        .field("createdTime", appointment.getCreatedTime())
                        .endObject();
                IndexResponse irsp = irb.setSource(xbuilder).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).execute().actionGet();

                if (appointment.getId() != Integer.parseInt(irsp.getId())) {
                    result.setError(-ECode.FAIL.getValue());
                    result.setMessage("Index response failed, retValue:" + appointment.getId());
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

    /**
     *
     * @param id
     * @return SUCCESS, -NOT_FOUND, -FAIL, -EXCEPTION
     */
    public int removeBy(Integer id) {
        return this.delete(TYPE, id.toString());
    }

    public PlaceSearchResult appointmentOfGroup(int idGroup, long fromTime, int from, int size) {
        SearchRequestBuilder srb = _cli.prepareSearch(m_strIndexName);
        FieldSortBuilder timeOrder = SortBuilders.fieldSort("time").order(SortOrder.ASC);
        srb.addSort(timeOrder);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        queryBuilder.filter(new TermQueryBuilder("idGroup", idGroup));
        queryBuilder.filter(QueryBuilders.rangeQuery("time").gte(fromTime));
        queryBuilder.filter(new TermQueryBuilder("type", PlaceType.APPOINTMENT));
        srb.setFrom(from).setSize(size);
        srb.setTypes(TYPE);
        srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        srb.setQuery(queryBuilder);
        return listPlace(srb);
    }

    public PlaceSearchResult placeOfGroup(int idGroup, int from, int size) {
        SearchRequestBuilder srb = _cli.prepareSearch(m_strIndexName);
        FieldSortBuilder timeOrder = SortBuilders.fieldSort("createdTime").order(SortOrder.ASC);
        srb.addSort(timeOrder);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        queryBuilder.filter(new TermQueryBuilder("idGroup", idGroup));
        queryBuilder.filter(QueryBuilders.rangeQuery("type").gte(PlaceType.PLACE_MIN).lte(PlaceType.PLACE_MAX));

        srb.setFrom(from).setSize(size);
        srb.setTypes(TYPE);
        srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        srb.setQuery(queryBuilder);
        return listPlace(srb);
    }

    private PlaceSearchResult listPlace(SearchRequestBuilder srb) {
        try {
            SearchResponse resp = srb.execute().actionGet();

            SearchHits hits = resp.getHits();
            // LOGGER.debug(resp.toString());
            return parseFromSearchHits(hits);
        } catch (NoNodeAvailableException nodeEx) {
            LOGGER.error(nodeEx.getMessage());
            //retry
            this.stop();
            this.restart();
            return new PlaceSearchResult(-ECode.EXCEPTION.getValue());
        } catch (Exception ex) {
            if (_cli == null) {
                this.restart();
            }
            LOGGER.error(ex.getMessage());
            return new PlaceSearchResult(-ECode.EXCEPTION.getValue());
        }
    }

    public PlaceSearchResult parseFromSearchHits(SearchHits hits) {
        if (hits != null) {

            PlaceSearchResult result = new PlaceSearchResult(ECode.SUCCESS.getValue());
            result.setTotal(hits.getTotalHits());
            int total = hits.getHits().length;

            List<Place> listResponse = new ArrayList(total);
            for (int i = 0; i < total; ++i) {
                try {
                    SearchHit hit = hits.getAt(i);
                    Place value = new Place();
                    value.setId(Integer.parseInt(hit.getId()));
                    Map<String, Object> mapFields = hit.sourceAsMap();
                    Object location = mapFields.get("location");
                    if (location instanceof HashMap) {
                        HashMap temp = (HashMap) location;
                        LatLon latLon = new LatLon();
                        value.setLatlon(latLon);
                        if (temp.size() == 2) {
                            try {
                                latLon.setLat((double) temp.get("lat"));
                                latLon.setLon((double) temp.get("lon"));

                            } catch (Exception ex) {
                                LOGGER.error(ex.toString());
                            }
                        }
                    }
                    value.setName(String.valueOf(mapFields.get("name")));
                    value.setTime(Long.valueOf(mapFields.get("time").toString()));
                    value.setZone(Integer.valueOf(mapFields.get("zone").toString()));
                    value.setType(Byte.valueOf(mapFields.get("type").toString()));
                    value.setIdUserOwner(Integer.valueOf(mapFields.get("idUserOwner").toString()));
                    value.setIdGroup(Integer.valueOf(mapFields.get("idGroup").toString()));
                    value.setCreatedTime(Long.valueOf(mapFields.get("createdTime").toString()));

                    listResponse.add(value);
                } catch (NumberFormatException | ElasticsearchParseException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }
            result.setValue(listResponse);
            return result;
        } else {
            return new PlaceSearchResult(-ECode.EXCEPTION.getValue());
        }
    }

    /**
     *
     * @param appointmentId
     * @return SUCCESS,-NOT_FOUND, -EXCEPTION
     */
    public PlaceResult get(int appointmentId) {
        PlaceResult result = new PlaceResult();
        try {
            GetResponse getResponse = _cli.get(new GetRequest(m_strIndexName, TYPE, String.valueOf(appointmentId))).actionGet();
            if (getResponse.isExists() == true) {
                result.setError(0);
                Place value = new Place();
                value.setId(Integer.parseInt(getResponse.getId()));
                Map<String, Object> mapFields = getResponse.getSourceAsMap();
                Object location = mapFields.get("location");
                if (location instanceof HashMap) {
                    HashMap temp = (HashMap) location;
                    LatLon latLon = new LatLon();
                    value.setLatlon(latLon);
                    if (temp.size() == 2) {
                        try {
                            latLon.setLat((double) temp.get("lat"));
                            latLon.setLon((double) temp.get("lon"));

                        } catch (Exception ex) {
                            LOGGER.error(ex.toString());
                        }
                    }
                }
                value.setName(String.valueOf(mapFields.get("name")));
                value.setTime(Long.valueOf(mapFields.get("time").toString()));
                value.setZone(Integer.valueOf(mapFields.get("zone").toString()));
                value.setType(Byte.valueOf(mapFields.get("type").toString()));
                value.setIdUserOwner(Integer.valueOf(mapFields.get("idUserOwner").toString()));
                value.setIdGroup(Integer.valueOf(mapFields.get("idGroup").toString()));
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

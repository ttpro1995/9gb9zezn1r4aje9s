package com.life.db;

import com.life.common.ECodeHelper;
import com.life.common.ESConfiguration;
import com.life.common.JsonUtils;
import static com.life.db.MessageDb.instance;
import com.life.entity.base.LatLon;
import com.life.es.ActionResult;
import com.life.es.entities.Message;
import com.life.es.entities.Place;
import com.life.es.entities.PlaceResult;
import com.life.es.entities.PlaceSearchResult;
import com.life.es.handler.PlaceController;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class PlaceDb {

    private static final Logger LOGGER = LogManager.getLogger(PlaceDb.class);

    private PlaceController ac = PlaceController.getInstance(ESConfiguration.instance.getIndex());

    public static PlaceDb instance = new PlaceDb();

    private PlaceDb() {
        ac.start();
        ac.createMapping();
    }

    public void init() {

    }

    /**
     *
     * @param appointment
     * @return SUCCESS, -FAIL, -EXCEPTION, -INVALID_DATA
     */
    public int indexPlace(Place appointment) {

        ActionResult index = ac.index(appointment);
        return index.getError();

    }

    /**
     *
     * @param id
     * @return null if not exists
     */
    public Place getPlace(int id) {
        PlaceResult get = ac.get(id);
        if (ECodeHelper.isSuccess(get.getError())) {
            return get.getValue();
        }
        return null;
    }

    /**
     *
     * @param id
     * @return SUCCESS, -NOT_FOUND, -FAIL, -EXCEPTION
     */
    public int removePlace(int id) {
        return ac.removeBy(id);

    }

    public PlaceSearchResult appointmentOfGroup(int groupId, long fromTime, int from, int size) {
        return ac.appointmentOfGroup(groupId, fromTime, from, size);
    }

    public PlaceSearchResult placeOfGroup(int idGroup, int from, int size) {
        return ac.placeOfGroup(idGroup, from, size);
    }

    public static void main(String[] args) {

        Place appointment = new Place();
        appointment.id = IdGenI32Db.instance.nextIdAppointment(); //public int id;
        appointment.name = "temp";//public String name;
        appointment.latlon = new LatLon(10.31, 106.52);//public LatLon latlon;
        //public long time;
        appointment.zone = 550;//public int zone;
        appointment.type = (byte) 1;//public byte type;
        appointment.idUserOwner = 1; //public int idUserOwner;
        appointment.idGroup = 1;//public int idGroup;

        instance.indexPlace(appointment);

        PlaceSearchResult appointmentOfGroup = instance.appointmentOfGroup(1, System.currentTimeMillis(), 1, 10);

        System.err.println("get: " + appointmentOfGroup);
//        String[] data = new String[]{
//            "{\"_index\":\"life360\",\"_type\":\"appointment\",\"_id\":\"19\",\"_score\":1,\"_source\":{\"id\":19,\"name\":\"name 13\",\"location\":{\"lat\":10.207850662659926,\"lon\":106.91056841384768},\"time\":1501138738144,\"zone\":758,\"type\":0,\"idUserOwner\":2,\"idGroup\":1,\"createdTime\":1494053940429}}",
//            "{\"_index\":\"life360\",\"_type\":\"appointment\",\"_id\":\"20\",\"_score\":1,\"_source\":{\"id\":20,\"name\":\"name 14\",\"location\":{\"lat\":10.369547699484071,\"lon\":106.59047455781943},\"time\":1502261938144,\"zone\":909,\"type\":0,\"idUserOwner\":2,\"idGroup\":1,\"createdTime\":1494053940450}}",
//            "{\"_index\":\"life360\",\"_type\":\"appointment\",\"_id\":\"17\",\"_score\":1,\"_source\":{\"id\":17,\"name\":\"name 11\",\"location\":{\"lat\":10.311407378650653,\"lon\":106.60273047719991},\"time\":1497855538144,\"zone\":441,\"type\":0,\"idUserOwner\":2,\"idGroup\":1,\"createdTime\":1494053940285}}",};
//
//        for (String item : data) {
//            JSONObject convertObject = JsonUtils.Instance.convertObject(JSONObject.class, item);
//
//            Map map = (Map) convertObject.get("_source");
//            Place convertObject1 = JsonUtils.Instance.convertObject(Place.class, map);
//            System.out.println("index: " + instance.indexPlace(convertObject1));
//
//        }

        System.exit(0);
    }

}

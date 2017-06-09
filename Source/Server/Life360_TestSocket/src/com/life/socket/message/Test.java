package com.life.socket.message;

import com.life.common.JsonUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test {

    public static void main(String[] args) {
        MsgLogin lm = new MsgLogin();
        lm.token = "user";

        System.out.println("get: " + JsonUtils.Instance.toJson(lm));

        String json = "{\"username\":\"user\",\"password\":\"s\"}";
        json = "{\"command\":5,\"username\":\"user\",\"password\":\"123\",\"vbyte\":3.2,\"vbyte1\":1,\"vshort\":4,\"vint\":5,\"vlong\":6,\"vfloat\":7.1,\"vdouble\":8.1}";

        Map<String, Object> map = JsonUtils.Instance.getMap(json);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MsgLogin convertValue;
        try {
            convertValue = mapper.convertValue(map, MsgLogin.class);
        } catch (IllegalArgumentException ex) {
            System.out.println("err: " + ex.getMessage());
        }
        LinkedHashMap hashMap = new LinkedHashMap();
        hashMap.put(null, "as");

        System.out.println("");

    }
}

package com.life.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class JsonUtils {

    private final ObjectMapper objMapper = new ObjectMapper();
    private static final Logger LOGGER = LogManager.getLogger(JsonUtils.class);

    public JsonUtils() {
        objMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static JsonUtils Instance = new JsonUtils();

    public JsonUtils(Map<SerializationConfig.Feature, Boolean> config) {
        for (SerializationConfig.Feature feature : config.keySet()) {
            objMapper.configure(feature, config.get(feature));
        }
        objMapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
    }

    public <T> List<T> getList(String json) {
        try {

            return objMapper.readValue(json, new TypeReference<List<T>>() {
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return new ArrayList<T>();
        }
    }

    public Map<String, Object> getMap(String json) {
        try {
            return objMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            LOGGER.error("getmap: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public <T> T convertObject(Class<T> type, String json) {
        try {
            return objMapper.readValue(json, type);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param <T>
     * @param type
     * @param fromValue
     * @return null if fail
     */
    public <T> T convertObject(Class<T> type, Object fromValue) {
        try {
            return objMapper.convertValue(fromValue, type);
        } catch (IllegalArgumentException ex) {
            LOGGER.error("convertObject: " + ex.getMessage());
            return null;
        }
    }

    public String toJson(Object obj) {
        try {
            return objMapper.writeValueAsString(obj);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public boolean isValidJSON(final String json) {
        boolean valid = true;
        try {
            objMapper.readTree(json);
        } catch (JsonProcessingException ex) {
            valid = false;
        } catch (IOException ex) {
            valid = false;
        }
        return valid;
    }
}

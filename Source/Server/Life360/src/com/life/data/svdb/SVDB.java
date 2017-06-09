package com.life.data.svdb;

import com.life.common.BytesUtils;
import com.life.common.Config;
import com.life.data.base.BaseRock;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 * @param <V>
 */
public class SVDB<V extends Serializable> extends BaseRock {

    private static final Logger LOGGER = LogManager.getLogger(SVDB.class);

    public SVDB(String name) {

        String namedb = Config.instance.getString(SVDB.class, name, "name", "");
        File dir = new File(Config.instance.getString(SVDB.class, name, "dir", "./"));
        boolean readonly = Config.instance.getBoolean(SVDB.class, name, "readonly", false);
        boolean allowConcurrentMemtableWrite = Config.instance.getBoolean(SVDB.class, name, "allowConcurrentMemtableWrite", true);

        super.init(namedb, dir, readonly, allowConcurrentMemtableWrite);
    }

    /**
     *
     * @param key
     * @return value , null
     */
    public V get(String key) {
        V result = null;
        byte[] get = super.getValue(BytesUtils.getBytes(key));
        if (Objects.nonNull(get)) {
            result = SerializationUtils.deserialize(get);
        }
        return result;
    }

    private List<byte[]> tobytes(List<String> ids) {
        List<byte[]> ret = new ArrayList<>();
        if (ids != null) {
            ids.forEach((i) -> {
                ret.add(BytesUtils.getBytes(i));
            });
        }
        return ret;
    }

    /**
     *
     * @param ids
     * @return not null
     */
    public Map<String, V> multiGet(List<String> ids) {

        Map<String, V> ret = new HashMap<>();
        List<byte[]> keyIds = tobytes(ids);
        Map<byte[], byte[]> multiGetValue = super.multiGetValue(keyIds);
        if (multiGetValue != null) {
            multiGetValue.entrySet().forEach((entry) -> {
                byte[] key = entry.getKey();
                byte[] value = entry.getValue();
                if (key != null && value != null) {
                    try {
                        String toKey = BytesUtils.toString(key);
                        V deserialize = SerializationUtils.deserialize(value);
                        ret.put(toKey, deserialize);
                    } catch (Exception ex) {
                        LOGGER.error(ex);
                    }
                }
            });
        }

        return ret;
    }

    public boolean put(String key, V value) {
        return super.putValue(BytesUtils.getBytes(key), SerializationUtils.serialize(value));
    }

    public boolean delete(String key) {
        return super.deleteValue(BytesUtils.getBytes(key));
    }

}

package com.life.data.i32db;

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
public class I32VDB<V extends Serializable> extends BaseRock {

    private static final Logger LOGGER = LogManager.getLogger(I32VDB.class);

    public I32VDB(String name) {
        String namedb = Config.instance.getString(I32VDB.class, name, "name", "");
        File dir = new File(Config.instance.getString(I32VDB.class, name, "dir", "./"));
        boolean readonly = Config.instance.getBoolean(I32VDB.class, name, "readonly", false);
        boolean allowConcurrentMemtableWrite = Config.instance.getBoolean(I32VDB.class, name, "allowConcurrentMemtableWrite", true);

        super.init(namedb, dir, readonly, allowConcurrentMemtableWrite);
    }

    /**
     * 
     * @param key
     * @return null if not exists
     */
    public V get(int key) {
        V result = null;
        byte[] get = super.getValue(BytesUtils.getBytes(key));
        if (Objects.nonNull(get)) {
            result = SerializationUtils.deserialize(get);
        }
        return result;

    }

    private List<byte[]> tobytes(List<Integer> ids) {
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
    public Map<Integer, V> multiGet(List<Integer> ids) {

        Map<Integer, V> ret = new HashMap<>();
        List<byte[]> keyIds = tobytes(ids);
        Map<byte[], byte[]> multiGetValue = super.multiGetValue(keyIds);
        if (multiGetValue != null) {
            multiGetValue.entrySet().forEach((entry) -> {
                byte[] key = entry.getKey();
                byte[] value = entry.getValue();
                if (key != null && value != null) {
                    try {
                        int toInt = BytesUtils.toInt(key);
                        V deserialize = SerializationUtils.deserialize(value);
                        ret.put(toInt, deserialize);
                    } catch (Exception ex) {
                        LOGGER.error(ex);
                    }
                }
            });
        }

        return ret;
    }

    public boolean put(int key, V value) {
        return super.putValue(BytesUtils.getBytes(key), SerializationUtils.serialize(value));
    }

    public boolean delete(int key) {
        return super.deleteValue(BytesUtils.getBytes(key));
    }

    public boolean exists(int key) {
        V get = this.get(key);
        return get != null;
    }


}

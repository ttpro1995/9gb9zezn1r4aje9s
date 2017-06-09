package com.life.data.mapi32str;

import com.life.common.BytesUtils;
import com.life.data.mapstri32.*;
import com.life.data.mapid32.*;
import com.life.common.Config;
import com.life.common.ECode;
import com.life.data.base.BaseRock;
import java.io.File;
import java.nio.ByteBuffer;
import org.elasticsearch.common.util.ByteUtils;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MapI32Str extends BaseRock {

    public MapI32Str(String name) {
        String namedb = Config.instance.getString(MapI32Str.class, name, "name", "");
        File dir = new File(Config.instance.getString(MapI32Str.class, name, "dir", "./"));
        boolean readonly = Config.instance.getBoolean(MapI32Str.class, name, "readonly", false);
        boolean allowConcurrentMemtableWrite = Config.instance.getBoolean(MapI32Str.class, name, "allowConcurrentMemtableWrite", true);

        super.init(namedb, dir, readonly, allowConcurrentMemtableWrite);
    }

    public boolean put(int key, String value) {
        return super.putValue(BytesUtils.getBytes(key), value.getBytes());
    }

    /**
     *
     * @param key
     * @return null if not exists
     */
    public String get(int key) {
        byte[] value = super.getValue(BytesUtils.getBytes(key));
        if (value != null) {
            return new String(value);
        }
        return null;
    }

    public boolean delete(int key) {
        return super.deleteValue(BytesUtils.getBytes(key));
    }

}

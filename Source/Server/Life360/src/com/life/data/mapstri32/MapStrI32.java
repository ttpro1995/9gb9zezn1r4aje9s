package com.life.data.mapstri32;

import com.life.common.BytesUtils;
import com.life.data.mapid32.*;
import com.life.common.Config;
import com.life.common.ECode;
import com.life.data.base.BaseRock;
import java.io.File;
import java.nio.ByteBuffer;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MapStrI32 extends BaseRock {

    public MapStrI32(String name) {
        String namedb = Config.instance.getString(MapStrI32.class, name, "name", "");
        File dir = new File(Config.instance.getString(MapStrI32.class, name, "dir", "./"));
        boolean readonly = Config.instance.getBoolean(MapStrI32.class, name, "readonly", false);
        boolean allowConcurrentMemtableWrite = Config.instance.getBoolean(MapStrI32.class, name, "allowConcurrentMemtableWrite", true);

        super.init(namedb, dir, readonly, allowConcurrentMemtableWrite);
    }

    public boolean put(String key, int value) {
        return super.putValue(key.getBytes(), BytesUtils.getBytes(value));
    }

    /**
     * 
     * @param key
     * @return value if exists or &lt 0
     */
    public int get(String key) {
        byte[] value = super.getValue(key.getBytes());
        if (value != null) {
            return ByteBuffer.wrap(value).getInt();
        }

        return -ECode.NOT_EXIST.getValue();
    }

    public boolean delete(String key) {
        return super.deleteValue(key.getBytes());
    }

}

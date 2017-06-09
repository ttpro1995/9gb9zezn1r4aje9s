package com.life.data.mapchecki32;

import com.life.common.BytesUtils;
import com.life.common.Config;
import com.life.data.base.BaseRock;
import java.io.File;
import java.nio.ByteBuffer;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MapCheckI32 extends BaseRock {

    public MapCheckI32(String name) {
        String namedb = Config.instance.getString(MapCheckI32.class, name, "name", "");
        File dir = new File(Config.instance.getString(MapCheckI32.class, name, "dir", "./"));
        boolean readonly = Config.instance.getBoolean(MapCheckI32.class, name, "readonly", false);
        boolean allowConcurrentMemtableWrite = Config.instance.getBoolean(MapCheckI32.class, name, "allowConcurrentMemtableWrite", true);

        super.init(namedb, dir, readonly, allowConcurrentMemtableWrite);
    }

    public boolean put(int key, boolean value) {

        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.put(value ? (byte) 1 : (byte) 0);
        buffer.putLong(System.currentTimeMillis());

        return super.putValue(BytesUtils.getBytes(key), buffer.array());
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean get(int key) {
        byte[] value = super.getValue(BytesUtils.getBytes(key));
        if (value != null && value.length > 1) {
            return value[0] != 0;
        }
        return false;
    }

    public boolean delete(int key) {
        return super.deleteValue(BytesUtils.getBytes(key));
    }

}

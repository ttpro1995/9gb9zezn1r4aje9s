package com.life.data.mapid32;

import com.life.common.BytesUtils;
import com.life.common.Config;
import com.life.common.ECode;
import com.life.data.base.BaseRock;
import java.io.File;
import java.nio.ByteBuffer;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MapId32 extends BaseRock {

    public MapId32(String name) {
        String namedb = Config.instance.getString(MapId32.class, name, "name", "");
        File dir = new File(Config.instance.getString(MapId32.class, name, "dir", "./"));
        boolean readonly = Config.instance.getBoolean(MapId32.class, name, "readonly", false);
        boolean allowConcurrentMemtableWrite = Config.instance.getBoolean(MapId32.class, name, "allowConcurrentMemtableWrite", true);

        super.init(namedb, dir, readonly, allowConcurrentMemtableWrite);
    }

    public boolean put(int key, int value) {
        return super.putValue(BytesUtils.getBytes(key), BytesUtils.getBytes(value));
    }

    public int get(int key) {
        byte[] value = super.getValue(BytesUtils.getBytes(key));
        if (value != null) {
            return ByteBuffer.wrap(value).getInt();
        }

        return -ECode.NOT_EXIST.getValue();
    }

    public boolean delete(int key) {
        return super.deleteValue(BytesUtils.getBytes(key));
    }

}

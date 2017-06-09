package com.life.data.idgeni32;

import com.life.common.BytesUtils;
import com.life.common.Config;
import com.life.data.base.BaseRock;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class IdGenI32 extends BaseRock {

    public IdGenI32(String name) {
        String namedb = Config.instance.getString(IdGenI32.class, name, "name", "");
        File dir = new File(Config.instance.getString(IdGenI32.class, name, "dir", "./"));
        boolean readonly = Config.instance.getBoolean(IdGenI32.class, name, "readonly", false);
        boolean allowConcurrentMemtableWrite = Config.instance.getBoolean(IdGenI32.class, name, "allowConcurrentMemtableWrite", true);

        super.init(namedb, dir, readonly, allowConcurrentMemtableWrite);
    }

    /**
     *
     * @param nameDB
     * @return -1 if error.
     */
    public int nextId(String nameDB) {
        byte[] key = BytesUtils.getBytes(nameDB);
        byte[] data = super.getValue(key);

        int value;

        if (Objects.nonNull(data) && data.length == Integer.BYTES) {
            ByteBuffer bb = ByteBuffer.wrap(data);
            value = bb.getInt() + 1;

        } else {
            value = 1;
        }
        boolean put = super.putValue(key, ByteBuffer.allocate(Integer.BYTES).putInt(value).array());
        if (put) {
            return value;
        }
        return -1;
    }

}

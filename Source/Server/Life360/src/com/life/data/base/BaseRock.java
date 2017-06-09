package com.life.data.base;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public abstract class BaseRock {

    static final Logger LOGGER = LogManager.getLogger(BaseRock.class);

    File dir;
    private String name;
    private boolean readonly = false;
    private RocksDB db = null;
    private Options options;
    private boolean allowConcurrentMemtableWrite;

    public BaseRock() {
    }

    protected void init(String name, File dir) {
        init(name, dir, false, true);
    }

    protected void init(String name, File dir, boolean readonly, boolean allowConcurrentMemtableWrite) {
        if (StringUtils.isBlank(name)) {
            LOGGER.error("BaseRock name empty!!!");
            System.exit(-1);
        }
        this.name = name.trim();
        this.dir = dir;
        this.readonly = readonly;
        this.allowConcurrentMemtableWrite = allowConcurrentMemtableWrite;

        if (!dir.exists()) {
            dir.mkdirs();
        }

        options = new Options();
        options.setCreateIfMissing(true);
        options.setAllowConcurrentMemtableWrite(allowConcurrentMemtableWrite);

        try {
            if (readonly) {
                db = RocksDB.openReadOnly(options, dir.getAbsolutePath() + "/" + name);
            } else {
                db = RocksDB.open(options, dir.getAbsolutePath() + "/" + name);
            }
        } catch (RocksDBException ex) {
            LOGGER.error("", ex);
        }

    }

    public String getName() {
        return name;
    }

    /**
     *
     * @param key
     * @return null if not exists or error;
     */
    protected byte[] getValue(byte[] key) {
        byte[] result = null;
        try {
            result = db.get(key);
        } catch (RocksDBException ex) {
            LOGGER.error("", ex);
        }
        return result;
    }

    protected Map<byte[], byte[]> multiGetValue(List<byte[]> list) {
        Map<byte[], byte[]> result = null;
        try {
            result = db.multiGet(list);
        } catch (RocksDBException ex) {
            LOGGER.error("", ex);
        }
        return result;
    }

    /**
     *
     * @param key
     * @param value
     * @return false if error.
     */
    protected boolean putValue(byte[] key, byte[] value) {
        boolean result = true;
        try {
            db.put(key, value);
        } catch (RocksDBException ex) {
            result = false;
            LOGGER.error("", ex);
        }
        return result;
    }

    /**
     *
     * @param key
     * @return return false if error
     */
    protected boolean deleteValue(byte[] key) {
        boolean result = true;
        try {
            db.delete(key);
        } catch (RocksDBException ex) {
            result = false;
            LOGGER.error("", ex);
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (Objects.nonNull(db)) {
                db.close();
            }
        } finally {
            super.finalize();
        }
    }

}

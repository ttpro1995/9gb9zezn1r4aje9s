package com.life.db;

import com.life.data.i32db.I32VDB;
import com.life.entity.Batery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class BateryDb extends I32VDB<Batery> {

    private static final Logger LOGGER = LogManager.getLogger(BateryDb.class);

    public static BateryDb instance = new BateryDb("Batery");

    private BateryDb(String name) {
        super(name);
    }

    public void init() {

    }

    public static void main(String[] args) {
        Batery b = new Batery((byte) 51);
        int uid = -1;

        //System.err.println("put: " + instance.put(uid, b));
        System.err.println("get: " + instance.get(uid));

        System.exit(0);
    }
}

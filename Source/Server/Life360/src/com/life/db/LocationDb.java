package com.life.db;

import com.life.data.i32db.I32VDB;
import com.life.entity.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class LocationDb extends I32VDB<Location> {

    private static final Logger LOGGER = LogManager.getLogger(LocationDb.class);

    public static LocationDb instance = new LocationDb("location");

    private LocationDb(String name) {
        super(name);
    }

    public void init() {

    }

    public static void main(String[] args) {
        Location l = new Location("Temp", 1.2, 2.3);
        int uid = -1;

        System.err.println("put: " + instance.put(uid, l));

        System.err.println("get: " + instance.get(uid));

    }
}

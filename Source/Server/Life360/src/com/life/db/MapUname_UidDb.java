package com.life.db;

import com.life.data.mapstri32.MapStrI32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MapUname_UidDb extends MapStrI32 {

    private static final Logger LOGGER = LogManager.getLogger(MapUname_UidDb.class);

    public static MapUname_UidDb instance = new MapUname_UidDb("MapUsernameWithUserId");

    private MapUname_UidDb(String name) {
        super(name);
    }

    public void init() {

    }
}

package com.life.db;

import com.life.data.mapstri32.MapStrI32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MapToken_UidDb extends MapStrI32 {

    private static final Logger LOGGER = LogManager.getLogger(MapToken_UidDb.class);

    public static MapToken_UidDb instance = new MapToken_UidDb("mapTokenWithUserId");

    private MapToken_UidDb(String name) {
        super(name);
    }

    public void init() {

    }
}

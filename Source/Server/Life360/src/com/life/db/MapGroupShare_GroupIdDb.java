package com.life.db;

import com.life.data.mapstri32.MapStrI32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MapGroupShare_GroupIdDb extends MapStrI32 {

    private static final Logger LOGGER = LogManager.getLogger(MapGroupShare_GroupIdDb.class);

    public static MapGroupShare_GroupIdDb instance = new MapGroupShare_GroupIdDb("GroupShareGroupId");

    private MapGroupShare_GroupIdDb(String name) {
        super(name);
    }

    public void init() {

    }

}

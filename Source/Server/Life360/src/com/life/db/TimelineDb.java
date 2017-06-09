package com.life.db;

import com.life.data.i32db.I32VDB;
import com.life.entity.Timeline;
import com.life.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class TimelineDb extends I32VDB<Timeline> {

    private static final Logger LOGGER = LogManager.getLogger(TimelineDb.class);

    public static TimelineDb instance = new TimelineDb("Timeline");

    private TimelineDb(String name) {
        super(name);
    }

    public void init() {

    }

}

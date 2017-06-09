package com.life.model;

import com.life.data.k32list32.K32List32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class TimelineOfUserModel {

    private static final Logger LOGGER = LogManager.getLogger(TimelineOfUserModel.class);

    public static TimelineOfUserModel instance = new TimelineOfUserModel();

    private TimelineOfUserModel() {
    }

}

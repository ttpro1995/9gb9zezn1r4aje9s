package com.life.backend;

import com.life.common.ECode;
import com.life.data.idgeni32.IdGenI32;
import com.life.db.IdGenI32Db;
import com.life.db.TimelineDb;
import com.life.db.TimelineOfUserDb;
import com.life.entity.Timeline;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class TimelineBE {

    private static final Logger LOGGER = LogManager.getLogger(TimelineBE.class);

    public static TimelineBE instance = new TimelineBE();

    private TimelineBE() {
    }

    /**
     *
     * @param timeline
     * @return id timeline , -INVALID_DATA, -FAIL
     */
    public int addTimeline(Timeline timeline) {
        if (timeline == null || timeline.idUser <= 0) {
            return -ECode.INVALID_DATA.getValue();
        }
        if (timeline.id <= 0) {
            Integer nextIdTimeline = IdGenI32Db.instance.nextIdTimeline();
            if (nextIdTimeline <= 0) {
                LOGGER.error("idGenI32 Timeline error!!!");
                return ECode.INVALID_DATA.getValue();
            }
            timeline.setId(nextIdTimeline);
        }

        boolean put = TimelineDb.instance.put(timeline.id, timeline);
        int putEntries = TimelineOfUserDb.instance.putEntries(timeline.idUser, timeline.id);
        if (put && (putEntries > 0 || putEntries == -ECode.ALREADY_EXIST.getValue())) {
            return timeline.id;
        }

        if (putEntries == -ECode.FAIL.getValue()) {
            TimelineOfUserDb.instance.remove(timeline.idUser, putEntries);
        }

        return -ECode.FAIL.getValue();
    }

    /**
     *
     * @param timeline
     * @return SUCCESS, -INVALID_DATA, -FAIL
     */
    public int updateTimeline(Timeline timeline) {
        if (timeline == null || timeline.idUser <= 0) {
            return -ECode.INVALID_DATA.getValue();
        }
        if (timeline.id <= 0) {
            return -ECode.INVALID_DATA.getValue();
        }
        boolean put = TimelineDb.instance.put(timeline.id, timeline);
        if (put) {
            return ECode.SUCCESS.getValue();
        }
        return -ECode.FAIL.getValue();
    }
}

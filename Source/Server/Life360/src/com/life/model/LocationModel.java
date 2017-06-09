package com.life.model;

import com.jtn.monitorstats.MonitorStats;
import com.jtn.monitorstats.ThreadMonitor;
import com.life.api.ApiMessage;
import com.life.common.LocationUtils;
import com.life.db.LocationDb;
import com.life.entity.Location;
import com.life.socket.message.MsgLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class LocationModel {

    private static final Logger LOGGER = LogManager.getLogger(LocationModel.class);

    public static final LocationModel instance = new LocationModel();

    private LocationModel() {
    }

    public ApiMessage putLocation(int uid, MsgLocation msg) {
        if (uid <= 0 || msg == null) {
            return ApiMessage.INVALID_DATA;
        }

        if (!LocationUtils.checkValidLonLat(msg.lon, msg.lat)) {
            return ApiMessage.INVALID_DATA;
        }

//		if (StringUtils.isBlank(msg.name)) {
//			return ApiMessage.INVALID_DATA;
//		}
        Location l = new Location("", msg.lat, msg.lon);

        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("putLocation");
        boolean put = LocationDb.instance.put(uid, l);
        monitor.pop("putLocation");
        if (put) {
            return ApiMessage.SUCCESS;
        }

        return ApiMessage.FAIL;

    }

}

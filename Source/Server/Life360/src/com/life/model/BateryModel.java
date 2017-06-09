package com.life.model;

import com.jtn.monitorstats.MonitorStats;
import com.jtn.monitorstats.ThreadMonitor;
import com.life.api.ApiMessage;
import com.life.db.BateryDb;
import com.life.entity.Batery;
import com.life.socket.message.MsgBatery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class BateryModel {

	private static final Logger LOGGER = LogManager.getLogger(BateryModel.class);

	public static final BateryModel instance = new BateryModel();

	private BateryModel() {
	}

	public ApiMessage putBatery(int uid, MsgBatery msg) {
		if (uid <= 0 || msg == null) {
			return ApiMessage.INVALID_DATA;
		}

		if (msg.batery < 0 || msg.batery > 100) {
			return ApiMessage.INVALID_DATA;
		}

		Batery b = new Batery(msg.batery);
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
		monitor.push("putBatery");
		boolean put = BateryDb.instance.put(uid, b);
		monitor.pop("putBatery");
		if (put) {
			return ApiMessage.SUCCESS;
		}

		return ApiMessage.FAIL;

	}

}

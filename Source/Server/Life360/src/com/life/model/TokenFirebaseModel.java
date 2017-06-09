package com.life.model;

import com.jtn.monitorstats.MonitorStats;
import com.jtn.monitorstats.ThreadMonitor;
import com.life.api.ApiMessage;
import com.life.db.TokenFirebaseDb;
import com.life.entity.TokenFirebase;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class TokenFirebaseModel {

	private static final Logger LOGGER = LogManager.getLogger(TokenFirebaseModel.class);

	public static final TokenFirebaseModel instance = new TokenFirebaseModel();

	private TokenFirebaseModel() {
	}

	public ApiMessage putTokenFirebase(int uid, String token) {
		if (uid <= 0 || StringUtils.isBlank(token)) {
			return ApiMessage.INVALID_DATA;
		}

		TokenFirebase tf = new TokenFirebase(token);
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
		monitor.push("putTokenFirebase");
		boolean put = TokenFirebaseDb.instance.put(uid, tf);
		monitor.pop("putTokenFirebase");
		if (put) {
			return ApiMessage.SUCCESS;
		}

		return ApiMessage.FAIL;

	}

}

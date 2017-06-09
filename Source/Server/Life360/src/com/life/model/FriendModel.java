package com.life.model;

import com.jtn.monitorstats.MonitorStats;
import com.jtn.monitorstats.ThreadMonitor;
import com.life.api.ApiMessage;
import com.life.backend.FriendsBE;
import com.life.common.ECode;
import com.life.common.ECodeHelper;
import com.life.data.k32list32.Value;
import com.life.db.MapUname_UidDb;
import com.life.db.UserDb;
import com.life.entity.User;
import com.life.entityret.ListUserRet;
import com.life.entityret.UserRet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class FriendModel {

	private static final Logger LOGGER = LogManager.getLogger(FriendModel.class);

	public static final FriendModel instance = new FriendModel();

	private FriendModel() {
	}

	public ApiMessage requestFriend(int uid, String userName) {
		if (uid <= 0 || StringUtils.isBlank(userName)) {
			return ApiMessage.INVALID_DATA;
		}

		ApiMessage apiMessage = ApiMessage.ITEM_NOT_FOUND;
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();

		monitor.push("getMapUname_Uid");
		int uidGet = MapUname_UidDb.instance.get(userName);
		monitor.pop("getMapUname_Uid");
		if (uidGet > 0) {
			monitor.push("requestFriend");
			int requestFriend = FriendsBE.instance.requestFriend(uid, uidGet);
			monitor.pop("requestFriend");
			if (ECodeHelper.isSuccess(requestFriend)) {
				apiMessage = ApiMessage.SUCCESS;
			} else if (requestFriend == -ECode.ALREADY_EXIST.getValue()) {
				apiMessage = ApiMessage.ALREADY_EXISTS;
			} else if (requestFriend == -ECode.IS_FRIEND.getValue()) {
				apiMessage = ApiMessage.IS_FRIEND;
			} else {
				apiMessage = ApiMessage.FAIL;
			}
		}
		return apiMessage;
	}

	public ApiMessage getlistRequesting(int uid, int offset, int size) {
		if (uid <= 0) {
			return ApiMessage.INVALID_DATA;
		}
		if (offset < 0) {
			offset = 0;
		}

		if (size <= 0 || size > 20) {
			size = 20;
		}
		ApiMessage apiMessage = ApiMessage.ITEM_NOT_FOUND;
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
		monitor.push("getListRequesting");
		Value listRequesting = FriendsBE.instance.getListRequesting(uid, offset, size);
		monitor.pop("getListRequesting");
		if (ECodeHelper.isSuccess(listRequesting.error)) {

			ListUserRet data = new ListUserRet();
			data.total = listRequesting.total;
			List<Integer> uids = listRequesting.value;

			if (uids != null && !uids.isEmpty()) {
				monitor.push("multiGetUser");
				Map<Integer, User> users = UserDb.instance.multiGet(uids);
				monitor.pop("multiGetUser");
				for (int id : uids) {
					User user = users.get(id);
					if (user != null) {
						UserRet ur = new UserRet(user);
						data.add(ur);
					}
				}
			}
			apiMessage = new ApiMessage(data);
		}
		return apiMessage;
	}

	public ApiMessage getlistFriend(int uid, int offset, int size) {
		if (uid <= 0) {
			return ApiMessage.INVALID_DATA;
		}
		if (offset < 0) {
			offset = 0;
		}

		if (size <= 0 || size > 20) {
			size = 20;
		}
		ApiMessage apiMessage = ApiMessage.ITEM_NOT_FOUND;
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
		monitor.push("getListFriend");
		Value listRequesting = FriendsBE.instance.getListFriend(uid, offset, size);
		monitor.pop("getListFriend");
		if (ECodeHelper.isSuccess(listRequesting.error)) {

			ListUserRet data = new ListUserRet();
			data.total = listRequesting.total;
			List<Integer> uids = listRequesting.value;

			if (uids != null && !uids.isEmpty()) {
				monitor.push("multiGetUser");
				Map<Integer, User> users = UserDb.instance.multiGet(uids);
				monitor.pop("multiGetUser");
				for (int id : uids) {
					User user = users.get(id);
					if (user != null) {
						UserRet ur = new UserRet(user);
						data.add(ur);
					}
				}
			}
			apiMessage = new ApiMessage(data);
		}
		return apiMessage;
	}

	public ApiMessage getlistRequested(int uid, int offset, int size) {
		if (uid <= 0) {
			return ApiMessage.INVALID_DATA;
		}
		if (offset < 0) {
			offset = 0;
		}

		if (size <= 0 || size > 20) {
			size = 20;
		}

		ApiMessage apiMessage = ApiMessage.ITEM_NOT_FOUND;
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
		monitor.push("getListRequested");
		Value listRequested = FriendsBE.instance.getListRequested(uid, offset, size);
		monitor.pop("getListRequested");
		if (ECodeHelper.isSuccess(listRequested.error)) {

			ListUserRet data = new ListUserRet();

			data.total = listRequested.total;

			List<Integer> uids = listRequested.value;

			if (uids != null && !uids.isEmpty()) {
				monitor.push("multiGetUser");
				Map<Integer, User> users = UserDb.instance.multiGet(uids);
				monitor.pop("multiGetUser");
				for (int id : uids) {
					User user = users.get(id);
					if (user != null) {
						UserRet ur = new UserRet(user);
						data.add(ur);
					}
				}
			}
			apiMessage = new ApiMessage(data);

		}

		return apiMessage;
	}

	public ApiMessage acceptFriend(int uidFrom, int uidTo) {
		if (uidFrom <= 0 || uidTo <= 0) {
			return ApiMessage.INVALID_DATA;
		}
		List<Integer> ids = Arrays.asList(uidFrom, uidTo);
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
		monitor.push("multiGetUser");
		Map<Integer, User> users = UserDb.instance.multiGet(ids);
		monitor.pop("multiGetUser");
		User userFrom = users.get(uidFrom);
		User userTo = users.get(uidTo);

		ApiMessage apiMessage;
		if (userFrom != null && userTo != null) {
			monitor.push("acceptFriend");
			int acceptFriend = FriendsBE.instance.acceptFriend(uidFrom, uidTo);
			monitor.pop("acceptFriend");
			if (ECodeHelper.isSuccess(acceptFriend)) {
				apiMessage = ApiMessage.SUCCESS;
			} else if (acceptFriend == -ECode.IS_FRIEND.getValue()) {
				apiMessage = ApiMessage.IS_FRIEND;
			} else {
				apiMessage = ApiMessage.NOT_EXISTS;
			}
		} else {
			apiMessage = ApiMessage.ITEM_NOT_FOUND;
		}
		return apiMessage;

	}

	public ApiMessage denyFriend(int uidFrom, int uidTo) {
		if (uidFrom <= 0 || uidTo <= 0) {
			return ApiMessage.INVALID_DATA;
		}
		List<Integer> ids = Arrays.asList(uidFrom, uidTo);
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
		monitor.push("multiGetUser");
		Map<Integer, User> users = UserDb.instance.multiGet(ids);
		monitor.pop("multiGetUser");
		User userFrom = users.get(uidFrom);
		User userTo = users.get(uidTo);

		ApiMessage apiMessage;
		if (userFrom != null && userTo != null) {
			monitor.push("denyFriend");
			int acceptFriend = FriendsBE.instance.denyFriend(uidFrom, uidTo);
			monitor.pop("denyFriend");
			if (ECodeHelper.isSuccess(acceptFriend)) {
				apiMessage = ApiMessage.SUCCESS;
			} else if (acceptFriend == -ECode.NOT_EXIST.getValue()) {
				apiMessage = ApiMessage.NOT_EXISTS;
			} else {
				apiMessage = ApiMessage.FAIL;
			}
		} else {
			apiMessage = ApiMessage.ITEM_NOT_FOUND;
		}
		return apiMessage;

	}

	public ApiMessage unFriend(int uidFrom, int uidTo) {
		if (uidFrom <= 0 || uidTo <= 0) {
			return ApiMessage.INVALID_DATA;
		}

		List<Integer> ids = Arrays.asList(uidFrom, uidTo);
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
		monitor.push("multiGetUser");
		Map<Integer, User> users = UserDb.instance.multiGet(ids);
		monitor.pop("multiGetUser");
		User userFrom = users.get(uidFrom);
		User userTo = users.get(uidTo);

		ApiMessage apiMessage;
		if (userFrom != null && userTo != null) {
			monitor.push("unFriend");
			int acceptFriend = FriendsBE.instance.unFriend(uidFrom, uidTo);
			monitor.pop("unFriend");
			if (ECodeHelper.isSuccess(acceptFriend)) {
				apiMessage = ApiMessage.SUCCESS;
			} else if (acceptFriend == -ECode.NOT_FRIEND.getValue()) {
				apiMessage = ApiMessage.NOT_FRIEND;
			} else {
				apiMessage = ApiMessage.FAIL;
			}
		} else {
			apiMessage = ApiMessage.ITEM_NOT_FOUND;
		}
		return apiMessage;

	}
}

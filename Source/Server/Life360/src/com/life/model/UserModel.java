package com.life.model;

import com.jtn.monitorstats.MonitorStats;
import com.jtn.monitorstats.ThreadMonitor;
import com.life.db.IdGenI32Db;
import com.life.api.ApiMessage;
import com.life.backend.UserBE;
import com.life.common.ECode;
import com.life.common.ECodeHelper;
import com.life.common.JsonUtils;
import com.life.common.JsonWebToken;
import com.life.data.i32db.I32VDB;
import com.life.data.mapstri32.MapStrI32;
import com.life.db.MapToken_UidDb;
import com.life.db.MapUname_UidDb;
import com.life.db.UserDb;
import com.life.entity.User;
import com.life.entityret.LoginRet;
import com.life.entityret.UserRet;
import com.life.socket.message.MsgLogin;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 *
 */
public class UserModel {

	private static final Logger LOGGER = LogManager.getLogger(UserModel.class);

	private static final String DEFAULT_AVARTAR = "resource/avartar_default.png";

	//private final MapI32Str mapUserIdWithSocketId = new MapI32Str("MapUserIdWithSocketId");
	//    private final MapStrI32 mapSocketIdWithUserId = new MapStrI32("MapSocketIdWithUserId");
	public static UserModel instance = new UserModel();

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String PHONE_PATTERN = "^84\\d{9,10}$";

	private Pattern patternEmail;
	private Pattern patternPhone;

	private UserModel() {
		patternEmail = Pattern.compile(EMAIL_PATTERN);
		patternPhone = Pattern.compile(PHONE_PATTERN);
	}

	private int validateUser(User user) {

		String firstname = user.getFirstName();
		if (StringUtils.isBlank(firstname)) {
			return -ECode.INVALID_DATA.getValue();
		}

		user.setFirstName(firstname.trim());

		String lastName = user.getLastName();
		if (lastName == null) {
			user.setLastName("");
		}

		//email
		String email = user.getEmail();
		if (StringUtils.isBlank(email)) {
			return -ECode.INVALID_DATA.getValue();
		}

		email = email.trim();
		Matcher matcher = patternEmail.matcher(email);
		if (!matcher.matches()) {
			return -ECode.INVALID_DATA.getValue();
		}
		user.setEmail(email);

		//phoneNumber
		String phoneNumber = user.getPhoneNumber();
		if (StringUtils.isBlank(phoneNumber)) {
			return -ECode.INVALID_DATA.getValue();
		}

		phoneNumber = phoneNumber.trim();
		if (!patternPhone.matcher(phoneNumber).matches()) {
			return -ECode.INVALID_DATA.getValue();
		}
		user.setPhoneNumber(phoneNumber);

		if (StringUtils.isBlank(user.getPassword())) {
			return -ECode.INVALID_DATA.getValue();
		}

		if (StringUtils.isBlank(user.getAvatar())) {
			user.setAvatar(DEFAULT_AVARTAR);
		}

		return ECode.SUCCESS.getValue();

	}

	public ApiMessage checkExistUsername(String username) {
		if (StringUtils.isBlank(username)) {
			return ApiMessage.INVALID_DATA;
		}
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();

		monitor.push("checkExistUsername");
		int checkExistUsername = UserBE.instance.checkExistUsername(username);
		monitor.pop("checkExistUsername");

		if (ECodeHelper.isSuccess(checkExistUsername)) {
			return ApiMessage.SUCCESS;
		} else if (checkExistUsername == -ECode.NOT_EXIST.getValue()) {
			return ApiMessage.NOT_EXISTS;
		}

		return ApiMessage.INVALID_DATA;

	}

	public ApiMessage createUser(String firstName, String lastName, String email, String phoneNumber, String password) {

		User user = new User(firstName, lastName, email, phoneNumber, password);

		int ret = validateUser(user);
		if (!ECodeHelper.isSuccess(ret)) {
			return ApiMessage.INVALID_DATA;
		}

		String token = JsonWebToken.instance.createToken(user.getEmail());
		user.setToken(token);

		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
		monitor.push("checkExistUsername");
		ret = UserBE.instance.checkExistUsername(user.getEmail());
		monitor.pop("checkExistUsername");
		if (ECodeHelper.isSuccess(ret)) {
			return ApiMessage.ALREADY_EXISTS;
		}

		monitor.push("checkExistUsername");
		ret = UserBE.instance.checkExistUsername(user.getPhoneNumber());
		monitor.pop("checkExistUsername");
		if (ECodeHelper.isSuccess(ret)) {
			return ApiMessage.ALREADY_EXISTS;
		}

		Integer id = IdGenI32Db.instance.nextIdUser();
		user.setId(id);

		monitor.push("AddUser");
		int AddUser = UserBE.instance.addUser(id, user);
		monitor.pop("AddUser");
		if (ECodeHelper.isSuccess(AddUser)) {
			return ApiMessage.SUCCESS;
		}
		return ApiMessage.FAIL;

	}

	public ApiMessage updateUser(int uid, String firstName, String lastName) {
		if (uid <= 0) {
			return ApiMessage.INVALID_DATA;
		}
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
		monitor.push("getUser");
		User user = UserDb.instance.get(uid);
		monitor.pop("getUser");
		if (user != null) {
			if (StringUtils.isBlank(firstName)) {
				return ApiMessage.INVALID_DATA;
			}

			user.setFirstName(firstName.trim());
			user.setLastName(lastName == null ? "" : lastName.trim());
			user.setUpdatedTime(System.currentTimeMillis());

			monitor.push("putUser");
			boolean put = UserDb.instance.put(uid, user);
			monitor.pop("putUser");
			if (put) {
				return ApiMessage.SUCCESS;
			} else {
				return ApiMessage.FAIL;
			}
		}

		return ApiMessage.ITEM_NOT_FOUND;

	}

	/**
	 *
	 * @param msg
	 * @return uid or &lte 0
	 */
	public int checkToken(String token) {
		if (StringUtils.isBlank(token)) {
			return -ECode.INVALID_DATA.value;
		}

		int get = MapToken_UidDb.instance.get(token);
		if (get > 0) {
			User user = UserDb.instance.get(get);
			if (user != null) {
				if (user.getToken().equals(token)) {
					return get;
				}
			} else {
				MapToken_UidDb.instance.delete(token);
			}
		}
		return -ECode.FAIL.getValue();
	}

	public ApiMessage login(String username, String password) {

		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return ApiMessage.FAIL;
		}
		ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
		monitor.push("getMapUname_UidDb");
		int uid = MapUname_UidDb.instance.get(username);
		monitor.pop("getMapUname_UidDb");
		if (uid > 0) {
			monitor.push("getUser");
			User user = UserDb.instance.get(uid);
			monitor.pop("getUser");
			if (user != null
					&& (user.getEmail().equals(username) || user.getPhoneNumber().equals(username))
					&& user.getPassword().equals(password)) {
				return new ApiMessage(new LoginRet(user.getId(), user.getToken()));
			}
		}
		return ApiMessage.FAIL;

	}

	public ApiMessage get(int uid) {
		if (uid > 0) {
			ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
			monitor.push("getUser");
			User user = UserDb.instance.get(uid);
			monitor.pop("getUser");
			if (user != null) {
				UserRet userRet = new UserRet(user);
				return new ApiMessage(userRet);
			}
		}

		return ApiMessage.FAIL;
	}

	public Map<Integer, User> multiGet(List<Integer> uids) {
		return UserDb.instance.multiGet(uids);
	}

	public static void main(String[] args) {
		String firstName = "Truong";
		String lastName = "Nguyen";
		String phone = "84111111";
		String email = "testmail";
		String pass = "123456";

		System.err.println("gte: " + instance.get(5));

		//System.err.println(String.format("f: %1$03d", 5));
//        for (int i = 1; i < 10; i++) {
//            int createUser = instance.createUser(
//                    firstName + i,
//                    lastName + i,
//                    email + i + "@gmail.com",
//                    phone + String.format("%1$03d", i),
//                    pass);
//            System.out.println(String.format("create %d: %d", i, createUser));
//        }
		System.exit(0);
	}
}

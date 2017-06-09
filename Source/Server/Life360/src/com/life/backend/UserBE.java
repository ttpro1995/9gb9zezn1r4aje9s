package com.life.backend;

import com.life.common.ECode;
import com.life.db.MapToken_UidDb;
import com.life.db.MapUname_UidDb;
import com.life.db.UserDb;
import com.life.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class UserBE {

    private static final Logger LOGGER = LogManager.getLogger(UserBE.class);

    public static UserBE instance = new UserBE();

    private UserBE() {
    }

    /**
     *
     * @param uid
     * @param user
     * @return SUCCESS, -FAIL
     */
    public int addUser(int uid, User user) {
        if (uid <= 0
                || StringUtils.isBlank(user.token)
                || StringUtils.isBlank(user.phoneNumber)
                || StringUtils.isBlank(user.email)) {
            return -ECode.FAIL.getValue();
        }

        boolean put = UserDb.instance.put(uid, user);
        boolean put1 = MapUname_UidDb.instance.put(user.email, uid);
        boolean put2 = MapUname_UidDb.instance.put(user.phoneNumber, uid);
        boolean put3 = MapToken_UidDb.instance.put(user.token, uid);
        if (put && put1 && put2 && put3) {
            return ECode.SUCCESS.getValue();
        }

        if (put) {
            UserDb.instance.delete(uid);
        }

        if (put1) {
            MapUname_UidDb.instance.delete(user.email);
        }
        if (put2) {
            MapUname_UidDb.instance.delete(user.phoneNumber);
        }
        if (put3) {
            MapToken_UidDb.instance.delete(user.token);
        }

        return -ECode.FAIL.getValue();
    }

    /**
     *
     * @param username
     * @return SUCCESS, -INVALID_DATA, -NOT_EXIST
     */
    public int checkExistUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return -ECode.INVALID_DATA.getValue();
        }

        int get = MapUname_UidDb.instance.get(username);
        if (get > 0) {
            if (UserDb.instance.get(get) != null) {
                return ECode.SUCCESS.getValue();
            } else {
                MapUname_UidDb.instance.delete(username);

            }
        }
        return -ECode.NOT_EXIST.getValue();
    }

}

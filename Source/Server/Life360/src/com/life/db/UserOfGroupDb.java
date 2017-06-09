package com.life.db;

import com.life.common.ECode;
import com.life.common.ECodeHelper;
import com.life.data.k32list32.K32List32;
import com.life.data.k32list32.Value;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class UserOfGroupDb extends K32List32 {

    private static final Logger LOGGER = LogManager.getLogger(UserOfGroupDb.class);

    public static UserOfGroupDb instance = new UserOfGroupDb("UserOfGroup");

    private UserOfGroupDb(String name) {
        super(name);
    }

    public void init() {

    }

    /**
     *
     * @param groupId
     * @param uid
     * @return -FAIL, SUCCESS, -NOT_EXISTS
     */
    public int exists(int groupId, int uid) {
        if (groupId <= 0) {
            return -ECode.FAIL.getValue();
        }
        Value get = this.get(groupId);
        if (ECodeHelper.isSuccess(get.getError())) {
            List<Integer> value = get.value;
            if (value.contains(uid)) {
                return ECode.SUCCESS.getValue();
            } else {
                return -ECode.NOT_EXIST.getValue();
            }

        }

        return -ECode.FAIL.getValue();
    }

}

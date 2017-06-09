package com.life.backend;

import com.life.common.ECode;
import com.life.common.ECodeHelper;
import com.life.data.k32list32.Value;
import com.life.db.FriendsDb;
import com.life.db.FriendsRequestedDb;
import com.life.db.FriendsRequestingDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class FriendsBE {

    private static final Logger LOGGER = LogManager.getLogger(GroupBE.class);

    public static FriendsBE instance = new FriendsBE();

    private FriendsBE() {
    }

    /**
     * Check is friend
     *
     * @param uidFrom
     * @param uidTo
     * @return
     */
    public boolean isFriend(int uidFrom, int uidTo) {
        int contain = FriendsDb.instance.contain(uidFrom, uidTo);
        int contain1 = FriendsDb.instance.contain(uidTo, uidFrom);
        if (ECodeHelper.isSuccess(contain) && ECodeHelper.isSuccess(contain1)) {
            return true;
        }
        if (ECodeHelper.isSuccess(contain)) {
            FriendsDb.instance.remove(uidFrom, uidTo);
        }
        if (ECodeHelper.isSuccess(contain1)) {
            FriendsDb.instance.remove(uidTo, uidFrom);
        }
        return false;
    }

    /**
     * request friend from uidFrom to uidTo
     *
     * @param uidFrom
     * @param uidTo
     * @return SUCCESS: request thành công, -ALREADY_EXIST: đã request, -FAIL: lỗi request, -IS_FRIEND: đã là friend.
     */
    public int requestFriend(int uidFrom, int uidTo) {
        if (isFriend(uidFrom, uidTo)) {
            return -ECode.IS_FRIEND.getValue();
        }

        int requested = FriendsRequestedDb.instance.putEntries(uidFrom, uidTo);
        int requesting = FriendsRequestingDb.instance.putEntries(uidTo, uidFrom);

        if ((requested > 0 && requesting > 0)
                || (requested > 0 && requesting == -ECode.ALREADY_EXIST.getValue())
                || (requested == -ECode.ALREADY_EXIST.getValue() && requesting > 0)) {
            return ECode.SUCCESS.getValue();
        }

        if (requested == -ECode.ALREADY_EXIST.getValue() && requesting == -ECode.ALREADY_EXIST.getValue()) {
            return -ECode.ALREADY_EXIST.getValue();
        }

        if (requested > 0 || requested == -ECode.ALREADY_EXIST.getValue()) {
            FriendsRequestedDb.instance.remove(uidFrom, uidTo);
        }
        if (requesting > 0 || requesting == -ECode.ALREADY_EXIST.getValue()) {
            FriendsRequestingDb.instance.remove(uidTo, uidFrom);
        }

        return -ECode.FAIL.getValue();
    }

    public Value getListRequesting(int uid, int offset, int size) {
        return FriendsRequestingDb.instance.get(uid, offset, size);

    }

    public Value getListRequested(int uid, int offset, int size) {
        return FriendsRequestedDb.instance.get(uid, offset, size);
    }

    public Value getListFriend(int uid, int offset, int size) {
        return FriendsDb.instance.get(uid, offset, size);
    }

    /**
     * accept friend from uidFrom to uidTo
     *
     * @param uidFrom
     * @param uidTo
     * @return SUCCESS, -IS_FRIEND, NOT_EXIST
     */
    public int acceptFriend(int uidFrom, int uidTo) {
        int isReqesting = FriendsRequestingDb.instance.contain(uidFrom, uidTo);
        int isRequested = FriendsRequestedDb.instance.contain(uidTo, uidFrom);
        if (ECodeHelper.isSuccess(isReqesting) && ECodeHelper.isSuccess(isRequested)) {

            int putFriendFrom = FriendsDb.instance.putEntries(uidFrom, uidTo);
            int putFriendTo = FriendsDb.instance.putEntries(uidTo, uidFrom);

            if ((putFriendFrom > 0 && putFriendTo > 0)
                    || (putFriendFrom > 0 && putFriendTo == -ECode.ALREADY_EXIST.getValue())
                    || (putFriendFrom == -ECode.ALREADY_EXIST.getValue() && putFriendTo > 0)) {

                FriendsRequestingDb.instance.remove(uidFrom, uidTo);
                FriendsRequestedDb.instance.remove(uidTo, uidFrom);

                return ECode.SUCCESS.getValue();
            }

            if (putFriendFrom == -ECode.ALREADY_EXIST.getValue() && putFriendTo == -ECode.ALREADY_EXIST.getValue()) {
                return -ECode.IS_FRIEND.getValue();
            }

        }

        if (ECodeHelper.isSuccess(isReqesting)) {
            FriendsRequestingDb.instance.remove(uidFrom, uidTo);
        }
        if (ECodeHelper.isSuccess(isRequested)) {
            FriendsRequestedDb.instance.remove(uidTo, uidFrom);
        }
        return isReqesting - ECode.NOT_EXIST.getValue();

    }

    /**
     *
     * @param uidFrom
     * @param uidTo
     * @return SUCCESS, -FAIL, -NOT_EXIST
     */
    public int denyFriend(int uidFrom, int uidTo) {
        int isReqesting = FriendsRequestingDb.instance.contain(uidFrom, uidTo);
        int isRequested = FriendsRequestedDb.instance.contain(uidTo, uidFrom);
        if (ECodeHelper.isSuccess(isReqesting) && ECodeHelper.isSuccess(isRequested)) {
            int removeRequesting = FriendsRequestingDb.instance.remove(uidFrom, uidTo);
            int removeRequested = FriendsRequestedDb.instance.remove(uidTo, uidFrom);
            if (ECodeHelper.isSuccess(removeRequesting)
                    || (ECodeHelper.isSuccess(removeRequesting) && ECodeHelper.isSuccess(removeRequested))) {
                return ECode.SUCCESS.getValue();
            }
            return -ECode.FAIL.getValue();
        }
        if (ECodeHelper.isSuccess(isReqesting)) {
            FriendsRequestingDb.instance.remove(uidFrom, uidTo);
        }
        if (ECodeHelper.isSuccess(isRequested)) {
            FriendsRequestedDb.instance.remove(uidTo, uidFrom);
        }
        return isReqesting - ECode.NOT_EXIST.getValue();
    }

    /**
     * 
     * @param uidFrom
     * @param uidTo
     * @return  SUCCESS, -1FAIL, -NOT_FRIEND
     */
    public int unFriend(int uidFrom, int uidTo) {
        if (isFriend(uidFrom, uidTo)) {
            int removeFriendFrom = FriendsDb.instance.remove(uidFrom, uidTo);
            int removeFriendTo = FriendsDb.instance.remove(uidTo, uidFrom);

            if (removeFriendFrom > 0 && removeFriendTo > 0) {
                return ECode.SUCCESS.getValue();
            }
            return -ECode.FAIL.getValue();
        }

        return -ECode.NOT_FRIEND.getValue();

    }

}

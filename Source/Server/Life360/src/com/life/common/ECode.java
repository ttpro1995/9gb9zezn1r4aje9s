/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.life.common;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public enum ECode {
    SUCCESS(0),
    FAIL(1),
    EXCEPTION(2),
    INVALID_PARAM(3),
    INVALID_DATA(4),
    NOT_FOUND(5),
    OUT_RANGE(6),
    NOT_EXIST(7),
    ALREADY_EXIST(8),
    EMPTY(9),
    UNSUPPORTED(10),
    UNLOADED(11),
    TIMEOUT(12), 
    OVER_FLOW(13),
    NOT_SERVE(14),
    NOT_PERMIT(15), 
    PERMISSION_DENY(16),
    UNREADABLE(17), 
    UNWRITABLE(18),
    WRONG_AUTH(19), 
    NOT_CONNECTED(20), 
    BAD_CONNECTION(21),
    NOT_MATCHED(22),
    UNCHANGED(23),
    DUPLICATED(24),
    OVER_LOAD(25),
    SERVER_ERROR(26),
    NOT_CONTAIN(27),
    IS_FRIEND(28),
    NOT_FRIEND(29);

    public final int value;

    ECode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

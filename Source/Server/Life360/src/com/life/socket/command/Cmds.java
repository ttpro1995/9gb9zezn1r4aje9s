package com.life.socket.command;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public enum Cmds {
    ERR_NOT_EXISTS_UID(-1),
    ERR_PARSE_CMD(-2),
    ERR_NOT_EXISTS_CMD(-3),
    LOGIN(101),
    LIST_MESSAGE(102),
    SEND_MESSAGE(103),
    REMOVE_MESSAGE(104),
    APPOINTMENT_CHECK_IN(105),
    APPOINTMENT_CHECK_OUT(106),
    NEW_TIMELINE(107),
    TIMELINE_OF_USER(108),
    UPDATE_LOCATION(109),
    UPDATE_BATERY(110),
    USER_OF_GOURP_TIMELINE(111),
    CREATE_PLACE(112),
    UPDATE_PLACE(113),
    REMOVE_PLACE(114),
    PLACE_OF_USER(115),
    NEW_CONVERSATION(116),
    LIST_CONVERSATION(117),
    PLACE_CHECK_IN(118),
    PLACE_CHECK_OUT(119),
    PUSH_PLACE_CHECK_IN(120),
    PUSH_PLACE_CHECK_OUT(121),
    PUSH_APPOINTMENT_CHECK_IN(122),
    PUSH_APPOINTMENT_CHECK_OUT(123),;

    public int value;

    Cmds(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}

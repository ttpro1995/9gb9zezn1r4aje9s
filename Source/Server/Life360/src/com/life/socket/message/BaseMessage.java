package com.life.socket.message;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public abstract class BaseMessage /*implements IMessage*/ {

    public static final int BASE_COUNT_DATA = 2;

    public int command;
    public int subCmd;

}

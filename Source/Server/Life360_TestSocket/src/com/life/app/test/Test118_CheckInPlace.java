package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgPlaceCheckIn;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test118_CheckInPlace {

    public static void main(String[] args) {
        SocketClientManager.login();

        MsgPlaceCheckIn data = new MsgPlaceCheckIn();

        data.command = 118;

        data.groupId = 1;
        data.idPlace = 110;

        SocketClientManager.execute(data);

        System.exit(0);
    }
}

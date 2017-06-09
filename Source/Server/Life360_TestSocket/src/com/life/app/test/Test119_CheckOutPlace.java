package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgPlaceCheckOut;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test119_CheckOutPlace {

    public static void main(String[] args) {
        SocketClientManager.login();

        MsgPlaceCheckOut data = new MsgPlaceCheckOut();

        data.command = 119;

        data.groupId = 1;
        data.idPlace = 110;

        SocketClientManager.execute(data);

        System.exit(0);
    }
}

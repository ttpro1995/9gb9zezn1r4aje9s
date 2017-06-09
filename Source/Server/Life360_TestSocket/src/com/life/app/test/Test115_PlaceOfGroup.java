package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgPlaceOfGroup;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test115_PlaceOfGroup {

    public static void main(String[] args) {
        SocketClientManager.login();

        MsgPlaceOfGroup data = new MsgPlaceOfGroup();

        data.command = 115;

        data.groupId = 1;
        data.from = 0;
        data.size = 20;

        SocketClientManager.execute(data);

        System.exit(0);
    }
}

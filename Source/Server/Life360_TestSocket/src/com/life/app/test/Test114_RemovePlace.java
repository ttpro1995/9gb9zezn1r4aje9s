package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgRemovePlace;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test114_RemovePlace {

    public static void main(String[] args) {
        SocketClientManager.login();

        MsgRemovePlace data = new MsgRemovePlace();

        data.command = 114;

        data.groupId = 1;
        data.placeId = 11;

        SocketClientManager.execute(data);

        System.exit(0);
    }
}

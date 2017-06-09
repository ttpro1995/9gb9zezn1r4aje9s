package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgCreatePlace;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test112_CreatePlace {

    public static void main(String[] args) {
        SocketClientManager.login();

        MsgCreatePlace data = new MsgCreatePlace();

        data.command = 112;

        data.groupId = 1;
        data.name = "place 1";
        data.lat = 10.3023;
        data.lon = 106.3023;
        data.zone = 5002;
        data.type = 10;
        
        SocketClientManager.execute(data);

        System.exit(0);
    }
}

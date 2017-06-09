package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgUpdatePlace;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test113_UpdatePlace {

    public static void main(String[] args) {
        SocketClientManager.login();

        MsgUpdatePlace data = new MsgUpdatePlace();

        data.command = 113;
        
        
        data.appointmentId = 112;
        data.name = "place 3 update";
        data.lat = 10.30;
        data.lon = 106.30;
        data.zone = 501;
        

        SocketClientManager.execute(data);

        System.exit(0);
    }
}

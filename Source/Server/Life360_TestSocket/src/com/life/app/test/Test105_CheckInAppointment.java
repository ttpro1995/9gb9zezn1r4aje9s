package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgAppointmentCheckIn;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test105_CheckInAppointment {

    public static void main(String[] args) {
        SocketClientManager.login();

        MsgAppointmentCheckIn data = new MsgAppointmentCheckIn();

        data.command = 105;

        data.groupId = 1;
        data.idAppointment = 108;

        SocketClientManager.execute(data);

        System.exit(0);
    }
}

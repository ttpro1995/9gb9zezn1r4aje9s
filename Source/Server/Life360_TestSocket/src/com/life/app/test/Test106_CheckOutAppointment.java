package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgAppointmentCheckOut;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test106_CheckOutAppointment {

	public static void main(String[] args) {
		SocketClientManager.login();

		MsgAppointmentCheckOut data = new MsgAppointmentCheckOut();

		data.command = 106;
		data.groupId = 1;
		data.idAppointment = 109;

		SocketClientManager.execute(data);

		System.exit(0);

	}
}

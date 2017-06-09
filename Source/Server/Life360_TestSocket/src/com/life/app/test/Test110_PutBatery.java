package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgBatery;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test110_PutBatery {

	public static void main(String[] args) {
		SocketClientManager.login();

		MsgBatery batery = new MsgBatery();

		batery.command = 110;
		batery.batery = 57;

		SocketClientManager.execute(batery);

		System.exit(0);
	}
}

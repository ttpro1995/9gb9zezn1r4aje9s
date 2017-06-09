package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgLocation;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test109_PutLocation {

	public static void main(String[] args) {
		SocketClientManager.login();
		MsgLocation location = new MsgLocation();

		location.command = 109;

		location.lat = 10.764125;
		location.lon = 106.656245;
		//location.name = "VNG";

		SocketClientManager.execute(location);

		System.exit(0);

	}
}

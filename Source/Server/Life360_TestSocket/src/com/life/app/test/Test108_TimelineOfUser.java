package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgTimelineOfUser;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test108_TimelineOfUser {

	public static void main(String[] args) {
		SocketClientManager.login();

		MsgTimelineOfUser data = new MsgTimelineOfUser();

		data.command = 108;

		data.from = 0;
		data.size = 20;

		SocketClientManager.execute(data.toString());

		System.exit(0);

	}
}

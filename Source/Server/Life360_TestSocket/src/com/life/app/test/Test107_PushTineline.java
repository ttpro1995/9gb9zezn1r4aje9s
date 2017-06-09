package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgPushTimeline;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test107_PushTineline {

	public static void main(String[] args) {
		SocketClientManager.login();
		MsgPushTimeline data = new MsgPushTimeline();

		data.command = 107;

		data.content = "test 4";
		data.lat = 10.32;
		data.lon = 106.1;
		data.fromTime = System.currentTimeMillis();
		data.toTime = data.fromTime + 600000;
		data.zone = 555;

		SocketClientManager.execute(data.toString());
		
		System.exit(0);

	}
}

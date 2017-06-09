package com.life.app.test;

import com.life.socket.SocketClientManager;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test1_Hello {

	public static void main(String[] args) {

		SocketClientManager.login();

		JSONObject jSONObject = new JSONObject();
		jSONObject.put("command", 1);

		SocketClientManager.execute(jSONObject.toJSONString());

		System.exit(0);
	}
}

package com.life.socket;

import com.life.socket.command.Cmds;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class SocketClient {

	static final String HOST = System.getProperty("host", "103.254.13.25");
	static final int PORT = 8081;

	public static void main(String[] args) throws Exception {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.handler(new initClient());
			Channel ch = b.connect(HOST, PORT).sync().channel();

			System.out.println("id: " + ch.id().asLongText());

			JSONObject jSONObject = new JSONObject();
			jSONObject.put("command", Cmds.LOGIN.getValue());
			jSONObject.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ0cnVvbmduZ3V5ZW5heEBnbWFpbC5jb20iLCJpYXQiOjE0OTM0NjE1NzUsInN1YiI6IlRva2VuIiwiaXNzIjoiTGlmZTM2MCIsImV4cCI6MjEyNDE4MTU3NX0.7ZPGE40ZbUaJ2Zt7tjzVdRtii7CoQ7hb2R6IMm9Aks8");
			//jSONObject.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ0cnVvbmduZ3V5ZW5heEBnbWFpbC5jb20iLCJpYXQiOjE0OTI1MzgxMzQsInN1YiI6IlRva2VuIiwiaXNzIjoiTGlmZTM2MCIsImV4cCI6MjEyMzI1ODEzNH0.F3XIcUcfe5kMengsDIhcNTjQ2CV51kubAWgOTT5z7EE");

			System.out.println("json: " + jSONObject);
			ch.writeAndFlush(jSONObject.toJSONString() + "\n");

			Thread.sleep(3000);
			jSONObject = new JSONObject();
			jSONObject.put("command", 1);
			
			ch.writeAndFlush(jSONObject.toJSONString() + "\n");
			Thread.sleep(5000);

		} finally {
			group.shutdownGracefully();
		}
	}
}

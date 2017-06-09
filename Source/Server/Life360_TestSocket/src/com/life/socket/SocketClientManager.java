package com.life.socket;

import com.life.socket.message.BaseMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class SocketClientManager {

    private static final boolean IS_LOCAL = true;
    public static String token;

    private static String host = IS_LOCAL ? "127.0.0.1" : "103.254.13.25";
    private static int port = 8081;

    private static Channel channel;
    static BlockingQueue<String> result = new LinkedBlockingQueue<>(32);

    static {

        //live
        token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ0cnVvbmduZ3V5ZW5heEBnbWFpbC5jb20iLCJpYXQiOjE0OTM0NjE1NzUsInN1YiI6IlRva2VuIiwiaXNzIjoiTGlmZTM2MCIsImV4cCI6MjEyNDE4MTU3NX0.7ZPGE40ZbUaJ2Zt7tjzVdRtii7CoQ7hb2R6IMm9Aks8";
        if (IS_LOCAL) {
            //local
            token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ0cnVvbmduZ3V5ZW5heEBnbWFpbC5jb20iLCJpYXQiOjE0OTI1MzgxMzQsInN1YiI6IlRva2VuIiwiaXNzIjoiTGlmZTM2MCIsImV4cCI6MjEyMzI1ODEzNH0.F3XIcUcfe5kMengsDIhcNTjQ2CV51kubAWgOTT5z7EE";
        }

        try {
            init();

        } catch (InterruptedException ex) {
            System.out.println("Err socket client!!! exit");

            System.exit(1);
        }

    }

    private static void init() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new initClient());
        channel = b.connect(host, port).sync().channel();

    }

    public static void execute(String msg) {
        System.out.println("send msg: " + msg);
        channel.writeAndFlush(msg + "\n");

        getResult();
    }

    public static void execute(BaseMessage msg) {
        System.out.println("send msg: " + msg);
        channel.writeAndFlush(msg.toString() + "\n");

        getResult();
    }

    public static String getResult() {
        String resultStr;
        try {
            resultStr = result.take();
        } catch (InterruptedException ex) {
            resultStr = "Error: " + ex.getMessage();
        }

        System.out.println("result: " + resultStr);
        return resultStr;
    }

    public static void login() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("command", 101);
        jSONObject.put("token", token);

        execute(jSONObject.toJSONString());
        System.out.println("");
    }

}

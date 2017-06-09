package com.life.app.test;

import com.life.socket.SocketClientManager;
import com.life.socket.message.MsgUserOfGroupTimeline;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test111_UserOfGroupTimeline {

    public static void main(String[] args) {
        SocketClientManager.login();

        MsgUserOfGroupTimeline data = new MsgUserOfGroupTimeline();

        data.command = 111;
        
        data.groupId = 1;

        SocketClientManager.execute(data);

        System.exit(0);
    }
}

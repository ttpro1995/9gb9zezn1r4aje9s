package com.life.server;

import com.life.common.Config;
import com.life.socket.handler.InitConnection;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class SServer {

    private static final Logger LOGGER = LogManager.getLogger(SServer.class);
    private final int PORT;

    public SServer(String name) {

        PORT = Config.instance.getInt(SServer.class, name, "port", 8081);
    }

    public boolean settupAndStart() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new InitConnection());

        b.bind(PORT);//.sync().channel().closeFuture().sync();
        LOGGER.info("Server socket start with port " + PORT);
        return true;

    }

}

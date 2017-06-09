package com.life.socket.handler;

import com.life.api.ApiMessage;
import com.life.api.SApiMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {

    public static void send(Channel ctx, String json) {
        ctx.writeAndFlush(json + "\n");
    }

    public static void send(Channel ctx, SApiMessage sApiMessage) {
        send(ctx, sApiMessage.toString());
    }

    public static void send(ChannelHandlerContext ctx, SApiMessage sApiMessage) {
        send(ctx.channel(), sApiMessage.toString());
    }

    public static void send(Channel ctx, int cmd, ApiMessage apiMessage) {
        SApiMessage sApiMessage = new SApiMessage(cmd, apiMessage);
        send(ctx, sApiMessage.toString());
    }

    public static void send(ChannelHandlerContext ctx, int cmd, ApiMessage apiMessage) {
        SApiMessage sApiMessage = new SApiMessage(cmd, apiMessage);
        send(ctx.channel(), sApiMessage.toString());
    }

}

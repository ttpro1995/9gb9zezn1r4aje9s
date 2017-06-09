package com.life.socket.handler;

import com.life.api.ApiMessage;
import com.life.api.SApiMessage;
import com.life.common.JsonUtils;
import com.life.socket.command.Cmds;
import com.life.socket.command.CommandManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class HandlerController extends BaseHandler<String> {

    private static final Logger LOGGER = LogManager.getLogger(HandlerController.class);

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    static ConcurrentMap<Integer, ChannelId> mapuid_ChanelId = new ConcurrentHashMap<>();

    public static Map<String, Integer> mapSocketIdWithUserId = new HashMap<>();

    public static boolean pushNotify(int uid, SApiMessage apiMessage) {

        if (mapuid_ChanelId.containsKey(uid)) {
            ChannelId channelId = mapuid_ChanelId.get(uid);
            Channel channel = channels.find(channelId);

            send(channel, apiMessage);
            return true;
        }
        return false;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active: " + ctx.channel().id().asLongText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        LOGGER.info(String.format("[disconnect] ChannelGroup:%d - identity: %d - mapSocketIdWithUserId:%d ", channels.size(), mapuid_ChanelId.size(), mapSocketIdWithUserId.size()));

        if (StringUtils.isBlank(msg)) {
            ctx.close();
            return;
        }
        String socketId = ctx.channel().id().asLongText();
        Map<String, Object> map = JsonUtils.Instance.getMap(msg);
        if (map.containsKey("command")) {

            int cmd;
            Integer uid = mapSocketIdWithUserId.get(socketId);
            if (uid == null) {
                BaseHandler.send(ctx, Cmds.ERR_NOT_EXISTS_UID.value, ApiMessage.UNKNOWN_EXCEPTION);
                ctx.close();
            }
            try {
                cmd = Integer.valueOf(map.get("command").toString());
            } catch (NumberFormatException ex) {
                BaseHandler.send(ctx, Cmds.ERR_PARSE_CMD.value, ApiMessage.INVALID_DATA);
                LOGGER.error("miss cmd, uid: " + uid, ex);
                return;
            }
            SApiMessage execute = CommandManager.instance.execute(cmd, uid, map);
            BaseHandler.send(ctx.channel(), execute);

        } else {
            BaseHandler.send(ctx, Cmds.ERR_NOT_EXISTS_CMD.value, ApiMessage.INVALID_DATA);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String socketId = ctx.channel().id().asLongText();
        int get = mapSocketIdWithUserId.remove(socketId);
        ChannelId get1 = mapuid_ChanelId.get(get);
        if (get1.asLongText().equals(ctx.channel().id().asLongText())) {
            mapuid_ChanelId.remove(get);
        }
        LOGGER.info(String.format("[disconnect] ChannelGroup:%d - identity: %d - mapSocketIdWithUserId:%d ", channels.size(), mapuid_ChanelId.size(), mapSocketIdWithUserId.size()));

    }

}

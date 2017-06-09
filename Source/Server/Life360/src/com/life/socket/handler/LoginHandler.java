package com.life.socket.handler;

import com.life.api.ApiMessage;
import com.life.common.JsonUtils;
import com.life.model.UserModel;
import com.life.socket.command.Cmds;
import com.life.socket.message.MsgLogin;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelPipeline;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class LoginHandler extends BaseHandler<String> {

    private static final Logger LOGGER = LogManager.getLogger(LoginHandler.class);

    private byte countFail = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        ApiMessage apiMessage;
        JSONObject jSONObject = new JSONObject();

        if (countFail > 5) {
            apiMessage = ApiMessage.EXCEED_LOGIN_TIMES;
            BaseHandler.send(ctx, Cmds.LOGIN.value, apiMessage);
            ctx.close();

        } else {

            if (StringUtils.isBlank(msg)) {
                ctx.close();
                return;
            }

            Map<String, Object> map = JsonUtils.Instance.getMap(msg);
            MsgLogin oMsg = MsgLogin.convertObject(map);

            if (oMsg == null) {
                jSONObject.put("params", msg);
                apiMessage = ApiMessage.INVALID_DATA;
                BaseHandler.send(ctx, Cmds.LOGIN.value, apiMessage);

            } else {
                jSONObject.put("params", oMsg.command);
                if (oMsg.command == Cmds.LOGIN.getValue()) {

                    int uid = UserModel.instance.checkToken(oMsg.token);

                    if (uid > 0) {
                        boolean containsKey = HandlerController.mapuid_ChanelId.containsKey(uid);
                        if (containsKey) {
                            ChannelId get = HandlerController.mapuid_ChanelId.get(uid);
                            Channel existsChannel = HandlerController.channels.find(get);
                            existsChannel.close();
                        }
                        Channel channel = ctx.channel();

                        ChannelPipeline pipeline = ctx.pipeline();
                        pipeline.remove("login");
                        pipeline.addLast("main", new HandlerController());

                        HandlerController.channels.add(channel);
                        HandlerController.mapuid_ChanelId.put(uid, channel.id());
                        HandlerController.mapSocketIdWithUserId.put(channel.id().asLongText(), uid);

                        //JSONObject result = new JSONObject();
                        // result.put("command", CommandIds.LOGIN.getValue());
                        apiMessage = ApiMessage.SUCCESS;//new ApiMessage(result);
                        BaseHandler.send(ctx, oMsg.command, apiMessage);

                    } else {
                        ++countFail;
                        apiMessage = ApiMessage.FAIL;
                        BaseHandler.send(ctx, oMsg.command, apiMessage);

                    }
                } else {
                    apiMessage = ApiMessage.PERMISSION_DENY;
                    BaseHandler.send(ctx, oMsg.command, apiMessage);
                }
            }
        }

        jSONObject.put("resut", apiMessage);
        LOGGER.info("socket: " + jSONObject);
    }

}

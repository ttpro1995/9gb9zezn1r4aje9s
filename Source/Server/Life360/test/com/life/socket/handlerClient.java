package com.life.socket;

import com.life.common.JsonUtils;
import com.life.socket.command.Cmds;
import com.life.socket.message.MsgAppointmentCheckIn;
import com.life.socket.message.MsgAppointmentCheckOut;
import com.life.socket.message.MsgPushTimeline;
import com.life.socket.message.MsgTimelineOfUser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class handlerClient extends SimpleChannelInboundHandler<String> {

    static int count = 1;

    private static final int TEST = 3;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        System.out.println(String.format("count: %d - receiver: %s", count, msg));

        Map<String, Object> map = JsonUtils.Instance.getMap(msg);
        if (map.get("error_code").equals(0)) {

            if (count == -1) {

                String json = "";
                switch (TEST) {
                    case 1:
                        json = hello();
                        break;
                    case 2:
                        json = pushTimeline();
                        break;
                    case 3:
                        json = timelineOfUser();
                        break;
                    case 4:
                        json = checkIn();
                        break;
                    case 5:
                        json = checkOut();
                        break;
                }
                System.out.println("send: " + json);
                ctx.writeAndFlush(json + "\n");
                count++;
            } else {
                return;
            }

        }
    }

    private String hello() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("command", 1);
        return jSONObject.toJSONString();
    }

    private String pushTimeline() {
        MsgPushTimeline data = new MsgPushTimeline();

        data.command = Cmds.NEW_TIMELINE.value;

        data.content = "test 3";
        data.lat = 10.3;
        data.lon = 106.1;
        data.fromTime = System.currentTimeMillis();
        data.toTime = data.fromTime + 600000;
        data.zone = 550;

        return data.toString();
    }

    private String timelineOfUser() {
        MsgTimelineOfUser data = new MsgTimelineOfUser();

        data.command = Cmds.TIMELINE_OF_USER.value;

        data.from = 0;
        data.size = 20;

        return data.toString();
    }

    private String checkIn() {
        MsgAppointmentCheckIn data = new MsgAppointmentCheckIn();

//        data.command = Cmds.CHECK_IN_APPOINTMENT.value;
//
//        data.fromTime = System.currentTimeMillis();
//        data.idAppointment = 97;

        return data.toString();
    }

    private String checkOut() {
        MsgAppointmentCheckOut data = new MsgAppointmentCheckOut();

        data.command = Cmds.APPOINTMENT_CHECK_OUT.value;

//        data.idTimeline = 5;
//
//        data.toTime = System.currentTimeMillis() + 1000000;
        return data.toString();

    }

}

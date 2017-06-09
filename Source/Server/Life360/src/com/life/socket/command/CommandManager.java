package com.life.socket.command;

import com.life.api.ApiMessage;
import com.life.api.SApiMessage;
import com.life.socket.entities.Packet;
import com.life.socket.handler.CheckInAppointmentHandler;
import com.life.socket.handler.CheckInPlaceHandler;
import com.life.socket.handler.CheckOutAppointmentHandler;
import com.life.socket.handler.CheckOutPlaceHandler;
import com.life.socket.handler.CreatePlaceHandler;
import com.life.socket.handler.HelloCommand;
import com.life.socket.handler.ListMessageOfUserHandler;
import com.life.socket.handler.NewTimelineHandler;
import com.life.socket.handler.PlaceOfGroupHandler;
import com.life.socket.handler.RemovePlaceHandler;
import com.life.socket.handler.UpdateBateryHandler;
import com.life.socket.handler.UpdateLocationHandler;
import com.life.socket.handler.TimelineOfUserHandler;
import com.life.socket.handler.UpdatePlaceHandler;
import com.life.socket.handler.UserOfGroupTimelineHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class CommandManager {

    private static final Logger LOGGER = LogManager.getLogger(CommandManager.class);

    public static CommandManager instance = new CommandManager();

    private static final Map<Integer, Class<? extends BaseCommand>> map = new HashMap<>();

    static {
        addHandler(1, HelloCommand.class);
        addHandler(Cmds.LIST_MESSAGE.value, ListMessageOfUserHandler.class);
        addHandler(Cmds.NEW_TIMELINE.value, NewTimelineHandler.class);
        
        addHandler(Cmds.APPOINTMENT_CHECK_IN.value, CheckInAppointmentHandler.class);
        addHandler(Cmds.APPOINTMENT_CHECK_OUT.value, CheckOutAppointmentHandler.class);
        
        addHandler(Cmds.TIMELINE_OF_USER.value, TimelineOfUserHandler.class);
        addHandler(Cmds.UPDATE_LOCATION.value, UpdateLocationHandler.class);
        addHandler(Cmds.UPDATE_BATERY.value, UpdateBateryHandler.class);
        addHandler(Cmds.USER_OF_GOURP_TIMELINE.value, UserOfGroupTimelineHandler.class);

        addHandler(Cmds.CREATE_PLACE.value, CreatePlaceHandler.class);
        addHandler(Cmds.UPDATE_PLACE.value, UpdatePlaceHandler.class);
        addHandler(Cmds.REMOVE_PLACE.value, RemovePlaceHandler.class);
        addHandler(Cmds.PLACE_OF_USER.value, PlaceOfGroupHandler.class);

        addHandler(Cmds.PLACE_CHECK_IN.value, CheckInPlaceHandler.class);
        addHandler(Cmds.PLACE_CHECK_OUT.value, CheckOutPlaceHandler.class);
    }

    private CommandManager() {

    }

    public static void addHandler(Integer commandId, Class<? extends BaseCommand> clazz) {
        map.put(commandId, clazz);
    }

    public Class removeHander(Integer commandId) {
        return map.remove(commandId);
    }

    public SApiMessage execute(Integer command, int uid, Map<String, Object> data) {

        ApiMessage apiMessage;
        try {
            Class clazz = map.get(command);
            if (clazz != null) {
                Packet packet = new Packet(uid, data);
                BaseCommand commandChannel = (BaseCommand) clazz.getDeclaredConstructor().newInstance();
                commandChannel.init(command, packet);

                apiMessage = commandChannel.execute();

            } else {
                apiMessage = ApiMessage.ITEM_NOT_FOUND;
            }
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            LOGGER.error(ex);
            apiMessage = ApiMessage.SERVER_ERROR;
        }
        SApiMessage sApiMessage = new SApiMessage(command, apiMessage);
        return sApiMessage;

    }
}

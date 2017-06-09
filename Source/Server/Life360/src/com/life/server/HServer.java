package com.life.server;

import com.life.common.Config;
import com.life.http.filter.FilterAuthentication;
import com.life.http.handler.AcceptFriendHandler;
import com.life.http.handler.AppointmentOfGroupHandler;
import com.life.http.handler.CreateUserHander;
import com.life.http.handler.CustomErrorHandler;
import com.life.http.handler.CreateGroupHandler;
import com.life.http.handler.UserGetHandler;
import com.life.http.handler.HelloHandler;
import com.life.http.handler.LoginHandler;
import com.life.http.handler.CheckExistsUserNameHandler;
import com.life.http.handler.CreateAppointmentHandler;
import com.life.http.handler.DenyFriendHandler;
import com.life.http.handler.GetCodeGroupShareHandler;
import com.life.http.handler.GetListFriendHandler;
import com.life.http.handler.GetListFriendRequestedHandler;
import com.life.http.handler.GetListFriendRequestingHandler;
import com.life.http.handler.GetListGroupOfUserHandler;
import com.life.http.handler.GetListUserOfGroupHandler;
import com.life.http.handler.UpdateGroupHandler;
import com.life.http.handler.JoinGroupHandler;
import com.life.http.handler.KickUserOfGroupHandler;
import com.life.http.handler.LeaveGroupHandler;
import com.life.http.handler.PutTokenFirebaseHandler;
import com.life.http.handler.RemoveAppointmentHandler;
import com.life.http.handler.RequestFriendHandler;
import com.life.http.handler.UnFriendHandler;
import com.life.http.handler.UpdateAppointmentHandler;
import com.life.http.handler.UserUpdateHander;
import java.io.File;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class HServer {

    static final Logger LOGGER = LogManager.getLogger(HServer.class);

    int port;
    String name;
    String pathResourceShare;

    public HServer(String name) {
        this.name = name;
        port = Config.instance.getInt(HServer.class, name, "port", 8080);
        pathResourceShare = Config.instance.getString(HServer.class, name, "pathResourceShare", "/data/app/Life360/resource/");
        File f = new File(pathResourceShare);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public boolean settupAndStart() {

        Server server = new Server(port);

        HandlerCollection handlerCollection = new HandlerCollection();

        ContextHandler ctxResource = new ContextHandler();
        ctxResource.setContextPath("/resource");
        //ctx.setErrorHandler(new CustomErrorHandler());
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(pathResourceShare);
        resourceHandler.setDirectoriesListed(false);

        ctxResource.setHandler(resourceHandler);

        handlerCollection.addHandler(ctxResource);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/api");

        context.setErrorHandler(new CustomErrorHandler());

        context.addServlet(HelloHandler.class, "/hello");

        EnumSet<DispatcherType> dispatcherType = EnumSet.of(DispatcherType.REQUEST);

        //user
        context.addServlet(CreateUserHander.class, "/createUser");

        context.addServlet(CheckExistsUserNameHandler.class, "/checkExistsUserName");

        context.addServlet(UserUpdateHander.class, "/updateUser");
        context.addFilter(FilterAuthentication.class, "/updateUser", dispatcherType);

        context.addServlet(UserGetHandler.class, "/getUser");
        context.addFilter(FilterAuthentication.class, "/getUser", dispatcherType);

        context.addServlet(LoginHandler.class, "/login");

        //group
        context.addServlet(CreateGroupHandler.class, "/createGroup");
        context.addFilter(FilterAuthentication.class, "/createGroup", dispatcherType);

        context.addServlet(UpdateGroupHandler.class, "/updateGroup");
        context.addFilter(FilterAuthentication.class, "/updateGroup", dispatcherType);

        context.addServlet(GetListGroupOfUserHandler.class, "/getGroupOfUser");
        context.addFilter(FilterAuthentication.class, "/getGroupOfUser", dispatcherType);

        context.addServlet(GetListUserOfGroupHandler.class, "/getUserOfGroup");
        context.addFilter(FilterAuthentication.class, "/getUserOfGroup", dispatcherType);

        context.addServlet(GetCodeGroupShareHandler.class, "/getCodeGroupShare");
        context.addFilter(FilterAuthentication.class, "/getCodeGroupShare", dispatcherType);

        context.addServlet(JoinGroupHandler.class, "/joinGroup");
        context.addFilter(FilterAuthentication.class, "/joinGroup", dispatcherType);

        context.addServlet(LeaveGroupHandler.class, "/leaveGroup");
        context.addFilter(FilterAuthentication.class, "/leaveGroup", dispatcherType);

        context.addServlet(KickUserOfGroupHandler.class, "/kickUserOfGroup");
        context.addFilter(FilterAuthentication.class, "/kickUserOfGroup", dispatcherType);

        context.addServlet(CreateAppointmentHandler.class, "/createAppointment");
        context.addFilter(FilterAuthentication.class, "/createAppointment", dispatcherType);
        
        context.addServlet(UpdateAppointmentHandler.class, "/updateAppointment");
        context.addFilter(FilterAuthentication.class, "/updateAppointment", dispatcherType);
        
        context.addServlet(RemoveAppointmentHandler.class, "/removeAppointment");
        context.addFilter(FilterAuthentication.class, "/removeAppointment", dispatcherType);

        context.addServlet(AppointmentOfGroupHandler.class, "/appointmentOfGroup");
        context.addFilter(FilterAuthentication.class, "/appointmentOfGroup", dispatcherType);

        context.addServlet(RequestFriendHandler.class, "/requestFriend");
        context.addFilter(FilterAuthentication.class, "/requestFriend", dispatcherType);

        context.addServlet(AcceptFriendHandler.class, "/acceptFriend");
        context.addFilter(FilterAuthentication.class, "/acceptFriend", dispatcherType);

        context.addServlet(DenyFriendHandler.class, "/denyFriend");
        context.addFilter(FilterAuthentication.class, "/denyFriend", dispatcherType);

        context.addServlet(GetListFriendRequestingHandler.class, "/friendRequesting");
        context.addFilter(FilterAuthentication.class, "/friendRequesting", dispatcherType);

        context.addServlet(GetListFriendRequestedHandler.class, "/friendRequested");
        context.addFilter(FilterAuthentication.class, "/friendRequested", dispatcherType);

        context.addServlet(GetListFriendHandler.class, "/getFriend");
        context.addFilter(FilterAuthentication.class, "/getFriend", dispatcherType);

        context.addServlet(UnFriendHandler.class, "/unFriend");
        context.addFilter(FilterAuthentication.class, "/unFriend", dispatcherType);

        context.addServlet(PutTokenFirebaseHandler.class, "/updateTokenFirebase");
        context.addFilter(FilterAuthentication.class, "/updateTokenFirebase", dispatcherType);

        handlerCollection.addHandler(context);

        server.setHandler(handlerCollection);
        try {
            server.start();
            LOGGER.info("Server Http start with port " + port);
            //server.join();

            return true;
        } catch (Exception ex) {
            LOGGER.error("Cannot start Http server. exit now.", ex);
        }
        return false;

    }

}

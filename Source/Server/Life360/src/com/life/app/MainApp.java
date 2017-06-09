package com.life.app;

import com.jtn.monitorstats.MonitorStats;
import com.life.common.Log4j2Config;
import com.life.db.PlaceDb;
import com.life.db.BateryDb;
import com.life.db.ConversationDb;
import com.life.db.FriendsDb;
import com.life.db.FriendsRequestedDb;
import com.life.db.FriendsRequestingDb;
import com.life.db.GroupDb;
import com.life.db.GroupMemberDb;
import com.life.db.GroupOfUserDb;
import com.life.db.GroupShareDb;
import com.life.db.IdGenI32Db;
import com.life.db.LastMessageDb;
import com.life.db.LocationDb;
import com.life.db.MapGroupShare_GroupIdDb;
import com.life.db.MapToken_UidDb;
import com.life.db.MapUname_UidDb;
import com.life.db.MessageReceivedDb;
import com.life.db.MessageWatchedDb;
import com.life.db.TimelineDb;
import com.life.db.TimelineOfUserDb;
import com.life.db.TokenFirebaseDb;
import com.life.db.UserDb;
import com.life.db.UserOfGroupDb;
import com.life.server.HServer;
import com.life.server.SServer;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MainApp {

    public static void main(String[] args) {

        init();

        Log4j2Config.init();

        PlaceDb.instance.init();
        BateryDb.instance.init();
        ConversationDb.instance.init();
        FriendsDb.instance.init();
        FriendsRequestedDb.instance.init();
        FriendsRequestingDb.instance.init();
        GroupDb.instance.init();
        GroupMemberDb.instance.init();
        GroupOfUserDb.instance.init();
        GroupShareDb.instance.init();
        IdGenI32Db.instance.init();
        LocationDb.instance.init();
        MapGroupShare_GroupIdDb.instance.init();
        MapToken_UidDb.instance.init();
        MapUname_UidDb.instance.init();
        TimelineDb.instance.init();
        TimelineOfUserDb.instance.init();
        TokenFirebaseDb.instance.init();
        UserDb.instance.init();
        UserOfGroupDb.instance.init();
        MessageReceivedDb.instance.init();
        MessageWatchedDb.instance.init();
        LastMessageDb.instance.init();

        HServer lifeServer = new HServer("life360");
        if (!lifeServer.settupAndStart()) {

            System.exit(-1);
        }

        SServer socketServer = new SServer("life360");
        if (!socketServer.settupAndStart()) {
            System.exit(-1);
        }
    }

    private static void init() {
        MonitorStats.createThreadMonitorStats("init");
        MonitorStats.closeThreadMonitorStats();
    }

}

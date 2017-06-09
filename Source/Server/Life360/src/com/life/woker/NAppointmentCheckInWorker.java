package com.life.woker;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.SApiMessage;
import com.life.common.ECodeHelper;
import com.life.data.k32list32.Value;
import com.life.db.UserOfGroupDb;
import com.life.notification.entitiesret.NAppointmentCheckInRet;
import com.life.socket.command.Cmds;
import com.life.socket.handler.HandlerController;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class NAppointmentCheckInWorker implements Runnable {

    int uid;
    int groupId;
    int appointmentId;

    public NAppointmentCheckInWorker(int uid, int groupId, int appointmentId) {
        this.uid = uid;
        this.groupId = groupId;
        this.appointmentId = appointmentId;
    }

    @Override
    public void run() {
        MonitorStats.createThreadMonitorStats("NAppointmentCheckInWorker");
        try {
            Value get = UserOfGroupDb.instance.get(groupId);
            if (ECodeHelper.isSuccess(get.error)) {
                List<Integer> uids = get.value;
                uids.remove((Object) uid);
                
                List<Integer> uidsNFirebase = new ArrayList<>(uids.size());
                SApiMessage data = new SApiMessage(Cmds.PUSH_APPOINTMENT_CHECK_IN.value, new NAppointmentCheckInRet(groupId, appointmentId));
                for (int uid : uids) {
                    if (!HandlerController.pushNotify(uid, data)) {
                        uidsNFirebase.add(uid);
                    }
                }

                if (!uidsNFirebase.isEmpty()) {
                    NotificationManager.pushNotifyFirebase(uidsNFirebase, data);
                }
            }
        } finally {
            MonitorStats.closeThreadMonitorStats();
        }

    }

}

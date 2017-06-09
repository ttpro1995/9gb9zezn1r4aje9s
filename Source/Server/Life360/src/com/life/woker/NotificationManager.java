package com.life.woker;

import com.life.api.SApiMessage;
import com.life.common.Config;
import com.life.common.FirebaseNotify;
import com.life.db.TokenFirebaseDb;
import com.life.entity.TokenFirebase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class NotificationManager {

    private static final Logger LOGGER = LogManager.getLogger(NotificationManager.class);

    private static ThreadPoolExecutor _threadPoolExcecutor = null;
    private static final int core_size;
    private static final int max_size;
    private static final int keep_alive_time;
    private static final int max_queue_size;

    static {
        core_size = Config.instance.getInt(NotificationManager.class, "notification", "core_size", 4);
        max_size = Config.instance.getInt(NotificationManager.class, "notification", "max_size", 8);
        keep_alive_time = Config.instance.getInt(NotificationManager.class, "notification", "keep_alive_time", 10000);
        max_queue_size = Config.instance.getInt(NotificationManager.class, "notification", "max_queue_size", 500);

        _threadPoolExcecutor = new ThreadPoolExecutor(
                core_size, // core size
                max_size, // max size
                keep_alive_time, // keep alive time
                TimeUnit.MILLISECONDS, // keep alive time units
                new ArrayBlockingQueue<Runnable>(max_queue_size) // the queue to use
        );
    }

    public static void pushNotify(Runnable r) {
        _threadPoolExcecutor.execute(r);
    }

    public void monitor() {
        LOGGER.info(String.format("[monitor] [%d/%d] active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                _threadPoolExcecutor.getPoolSize(),
                _threadPoolExcecutor.getCorePoolSize(),
                _threadPoolExcecutor.getActiveCount(),
                _threadPoolExcecutor.getCompletedTaskCount(),
                _threadPoolExcecutor.getTaskCount(),
                _threadPoolExcecutor.isShutdown(),
                _threadPoolExcecutor.isTerminated()));
    }

    public static void pushNotifyFirebase(List<Integer> uids, SApiMessage data) {
        Map<Integer, TokenFirebase> tokenFirebases = TokenFirebaseDb.instance.multiGet(uids);
        List<String> tokens = new ArrayList<>(uids.size());
        for (Integer uid : uids) {
            TokenFirebase tokenFirebase = tokenFirebases.get(uid);
            if (tokenFirebase != null && StringUtils.isNotBlank(tokenFirebase.tokenFirebase)) {
                tokens.add(tokenFirebase.tokenFirebase);
            }
        }
        if (!tokens.isEmpty()) {
            FirebaseNotify.instance.notify(tokens, data);
        }

    }

    public static void main(String[] args) {
        System.out.println("");
    }
}

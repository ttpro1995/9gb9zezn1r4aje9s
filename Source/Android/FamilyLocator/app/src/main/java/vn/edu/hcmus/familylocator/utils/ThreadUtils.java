package vn.edu.hcmus.familylocator.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by quangcat on 5/1/17.
 */

public class ThreadUtils {

    public static Thread processWithNewThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.start();
        return t;
    }

    public static void processWithUIThread(Runnable runnable) {
        Handler h = new Handler(Looper.getMainLooper());
        h.post(runnable);
    }

}

package vn.edu.hcmus.familylocator.core;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MainApplication.context;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
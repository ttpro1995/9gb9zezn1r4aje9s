package com.quangcv.demo;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Quang Cat on 01/06/2017.
 */

public class MainService extends IntentService {

    private static final String TAG = MainService.class.getSimpleName();
    private static final int INTERVAL = 15000;



    public MainService() {
        super(TAG);

//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}

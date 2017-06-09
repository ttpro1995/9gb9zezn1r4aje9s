package vn.edu.hcmus.familylocator.core;

import android.Manifest;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import vn.edu.hcmus.familylocator.models.UserPost;
import vn.edu.hcmus.familylocator.utils.DataUtils;
import vn.edu.hcmus.familylocator.utils.ThreadUtils;
import vn.edu.hcmus.familylocator.utils.Utils;

/**
 * Created by quangcat on 5/21/17.
 */

public class MainService extends IntentService implements GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainService.class.getSimpleName();
    private static final int RADIUS = 50; // meter
    private static final int MINIMUM_TIME = 60 * 1000; // ms
    private static final int DELAY = 30000; // ms
    private static final int MINIMUM_METER = 5;

    private UserPost userPost = DataUtils.getUncompletePost();

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location location;

    public MainService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Vui lòng bật GPS trước khi sử dụng ứng dụng này.", Toast.LENGTH_SHORT).show();
            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(callGPSSettingIntent);
            return;
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(String.valueOf(ActionCode.PUSH_TIMELINE)));

        ThreadUtils.processWithNewThread(new Runnable() {
            @Override
            public void run() {
                while (SocketAPI.getInstance().isRunning()) {
                    if (location != null) {
                        SocketAPI.getInstance().updateLocation(location.getLatitude(), location.getLongitude());
                    }
                    SocketAPI.getInstance().updateBatery();
                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Log.d(TAG, "Service started");
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Integer.parseInt(intent.getAction())) {
                case ActionCode.PUSH_TIMELINE:
                    userPost = null;
                    DataUtils.saveUncompletePost(null);
                    break;
            }
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

        // Nếu chưa có userPost nào thì tạo userPost mới
        if (userPost == null) {
            userPost = new UserPost();
            userPost.lat = location.getLatitude();
            userPost.lon = location.getLongitude();
            userPost.from = userPost.to = System.currentTimeMillis();
            DataUtils.saveUncompletePost(userPost);
        }
        // Nếu đã có userPost và được đánh dấu là hoàn thành thì gửi lên server
        else if (userPost.completed) {
            if (SocketAPI.getInstance().isRunning()) {
                SocketAPI.getInstance().pushTimeline(userPost);
            }
        }
        // Nếu chưa hoàn thành
        else {
            float distance = Utils.calculateDistanceInMeter(userPost.lat, userPost.lon, location.getLatitude(), location.getLongitude());
            // Còn trong phạm vi bán kính -> Cập nhật thời gian
            if (distance <= RADIUS) {
                userPost.to = System.currentTimeMillis();
                DataUtils.saveUncompletePost(userPost);
            }
            // Vượt ra phạm vi bán kính + Thỏa thời gian tối thiểu -> Hoàn thành 1 userPost
            else if (userPost.to - userPost.from >= MINIMUM_TIME) {
                userPost.completed = true;
                DataUtils.saveUncompletePost(userPost);
                if (SocketAPI.getInstance().isRunning()) {
                    SocketAPI.getInstance().pushTimeline(userPost);
                }
            }
        }

//        INTENT.putExtra("lat", location.getLatitude());
//        INTENT.putExtra("lon", location.getLongitude());
//        LocalBroadcastManager.getInstance(this).sendBroadcast(INTENT);

        Log.e(TAG, location.toString());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(30 * 1000); // 30s
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
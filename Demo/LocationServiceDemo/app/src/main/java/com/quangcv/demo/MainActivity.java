package com.quangcv.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location location;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textView = new TextView(this);
        textView.setTextSize(8);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && event.getY() <= v.getHeight() / 3 && location != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/@" + location.getLatitude() + "," + location.getLongitude() + ",17z"));
                    startActivity(browserIntent);
                }
                return true;
            }
        });
        setContentView(textView);

        textView.append("onCreate\n");


//        startService(new Intent(this, MainService.class));


        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();
        textView.append("googleApiClient connecting...\n");
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        textView.append("onConnected\n");

        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(30 * 1000); // 30s
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        textView.append("onConnectionSuspended\n");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        textView.append("onConnectionFailed\n");
    }

    @Override
    public void onLocationChanged(Location location) {
        textView.append("[" + convertMsToTimeString(System.currentTimeMillis())
                + " | onLocationChanged | " + location.getProvider() + " | " + location.getLatitude() + ","
                + location.getLongitude() + " | " + location.getSpeed() + "m/s]\n");
        this.location = location;
    }

    DateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    public String convertMsToTimeString(long ms) {
        return formatter.format(new Date(ms));
    }
}

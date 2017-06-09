//package vn.edu.hcmus.familylocator.activities;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Typeface;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Handler;
//import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.NotificationCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.Random;
//
//import vn.edu.hcmus.familylocator.R;
//import vn.edu.hcmus.familylocator.utils.MathUtils;
//import vn.edu.hcmus.familylocator.objs.Appointment;
//import vn.edu.hcmus.familylocator.objs.UserInfo;
//import vn.edu.hcmus.familylocator.objs.UserLocation;
//
//public class TrackingAppointmentActivity extends AppCompatActivity {
//
//    private static final int THRESHOLD = 1 * 60 * 1000;
//
//    private GoogleMap mGMap;
//    private DatabaseReference mDbRef;
//    private ArrayList<UserInfo> mPoints = new ArrayList<>();
//
//    private Handler handler = new Handler();
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            redrawMarkers();
//            handler.postDelayed(this, THRESHOLD);
//        }
//    };
//
//    private String mUserId;
//    private String mAppointmentId;
//    private Appointment mAppointment;
//
//    private SharedPreferences mSP;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tracking_appointment);
//
//        Intent intent = getIntent();
//        mUserId = intent.getStringExtra("user_id");
//        mAppointmentId = intent.getStringExtra("appointment_id");
//
//        mSP = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
//
//        init();
//    }
//
//    private void init() {
//        // Init map
//        MapView mapView = (MapView) findViewById(R.id.map_view);
//        mapView.onCreate(null);
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                mGMap = googleMap;
//            }
//        });
//        mapView.onResume();
//
//        // Init firebase
//        mDbRef = FirebaseDatabase.getInstance().getReference();
//        mDbRef.child("appointments").child(mAppointmentId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mAppointment = dataSnapshot.getValue(Appointment.class);
//                mAppointment.id = dataSnapshot.getKey();
//                for (String uid : mAppointment.participants) {
//                    mDbRef.child("users").child(uid).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            UserInfo p = dataSnapshot.getValue(UserInfo.class);
//                            p.user_id = dataSnapshot.getKey();
//                            // Distance <= 1 km -> push notification
//                            String key = mAppointmentId + p.user_id;
//                            boolean flag = mSP.getBoolean(key, false);
//                            if (MathUtils.distance(p.location.lat, p.location.lng,
//                                    mAppointment.latitude, mAppointment.longitude, 'K') <= mAppointment.radius) {
//                                if (!flag) {
//                                    pushLocalNotification(p.user_name + " đã đến");
//                                    SharedPreferences.Editor editor = mSP.edit();
//                                    editor.putBoolean(key, true);
//                                    editor.apply();
//                                }
//                            } else {
//                                if (flag) {
//                                    pushLocalNotification(p.user_name + " đã rời khỏi");
//                                    SharedPreferences.Editor editor = mSP.edit();
//                                    editor.putBoolean(key, false);
//                                    editor.apply();
//                                }
//                            }
//                            for (int i = 0; i < mPoints.size(); i++) {
//                                if (mPoints.get(i).user_id == p.user_id) {
//                                    mPoints.set(i, p);
//                                    redrawMarkers();
//                                    return;
//                                }
//                            }
//                            mPoints.add(p);
//                            redrawMarkers();
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//                mGMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                        new LatLng(mAppointment.latitude, mAppointment.longitude), 15f));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        // GPS tracking
//        LocationManager locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                UserLocation l = new UserLocation();
//                l.lat = location.getLatitude();
//                l.lng = location.getLongitude();
//                l.timestamp = System.currentTimeMillis();
//
//                mDbRef.child("users").child(mUserId).child("location").setValue(l);
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//        // Check permission state
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                3000, 5, locationListener);
//
//        // Check enable state
//        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            displayPromptForEnablingGPS();
//        }
//
//        handler.postDelayed(runnable, THRESHOLD);
//    }
//
//    public void displayPromptForEnablingGPS() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
//        final String message = "Do you want open GPS setting?";
//
//        builder.setMessage(message)
//                .setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface d, int id) {
//                                startActivity(new Intent(action));
//                                d.dismiss();
//                            }
//                        })
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface d, int id) {
//                                d.cancel();
//                            }
//                        });
//        builder.create().show();
//    }
//
//    private void redrawMarkers() {
//        mGMap.clear();
//        if (mPoints.size() > 0) {
//            for (UserInfo p : mPoints) {
//                if (p.location != null) {
//                    Marker marker = mGMap.addMarker(new MarkerOptions().position(
//                            new LatLng(p.location.lat, p.location.lng)));
//                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerIcon(p.user_name)));
//                    if (p.user_id.equals(mUserId)) {
//                        marker.setTitle("YOU - " + p.user_name);
//                    } else {
//                        marker.setTitle(p.user_name);
//                    }
//                }
//            }
//        }
//    }
//
//    private Bitmap getMarkerIcon(String name) {
//        if (name.length() < 1) {
//            return null;
//        }
//
//        name = String.valueOf(name.charAt(0));
//        name = name.toUpperCase();
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inMutable = true;
//        Bitmap markerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.marker, opts);
//
//        Canvas canvas = new Canvas(markerIcon);
//        Paint paint = new Paint();
//        paint.setColor(Color.parseColor("#fa1b36"));
//        paint.setAntiAlias(true);
//        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        paint.setTextSize(65);
//        paint.setTextAlign(Paint.Align.CENTER);
//        canvas.drawText(name, canvas.getWidth() / 2, 71, paint);
//
//        return markerIcon;
//    }
//
//    public void pushLocalNotification(String content) {
////        Intent intent = new Intent(this, LoginActivity.class);
////        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
//
//        b.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Family Locator")
//                .setContentText(content)
//                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(new Random().nextInt(), b.build());
//    }
//
//}

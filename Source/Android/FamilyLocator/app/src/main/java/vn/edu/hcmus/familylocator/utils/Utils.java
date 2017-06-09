package vn.edu.hcmus.familylocator.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.BatteryManager;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by quangcat on 5/20/17.
 */

public class Utils {

    public static float getBatteryLevel(Context ctx) {
        Intent batteryIntent = ctx.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return ((float) level / (float) scale) * 100.0f;
    }

    public static float calculateDistanceInMeter(double lat1, double lng1, double lat2, double lng2) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        return loc1.distanceTo(loc2);
    }

    public static String convertMsToTimeString(long ms, boolean hasDate) {
        Date date = new Date(ms);
        DateFormat formatter = new SimpleDateFormat(hasDate ? "dd/MM/yyyy HH:mm" : "HH:mm");
        return formatter.format(date);
    }

    public static void getCompleteAddressString(final Context context, final double lat, final double lon, final Callback callback) {
        ThreadUtils.processWithNewThread(new Runnable() {
            int retryTime = 3;

            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = null;
                String result;

                // Prevent time out exception
                while (retryTime > 0) {
                    retryTime--;
                    try {
                        addresses = geocoder.getFromLocation(lat, lon, 1);
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (addresses != null && addresses.size() > 0) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("");
                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                    }
                    result = strReturnedAddress.toString();
                } else {
                    result = "Vị trí không xác định";
                }

                if (callback != null) {
                    final String finalStrAdd = result;
                    ThreadUtils.processWithUIThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDone(finalStrAdd);
                        }
                    });
                }
            }
        });
    }

    public interface Callback {
        void onDone(String address);
    }

}

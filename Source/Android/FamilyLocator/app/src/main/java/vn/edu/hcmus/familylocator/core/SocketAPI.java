package vn.edu.hcmus.familylocator.core;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import vn.edu.hcmus.familylocator.models.Place;
import vn.edu.hcmus.familylocator.models.UserPost;
import vn.edu.hcmus.familylocator.utils.DataUtils;
import vn.edu.hcmus.familylocator.utils.Utils;

/**
 * Created by Quang Cat on 05/05/2017.
 */

public class SocketAPI {

    private static final String TAG = SocketAPI.class.getSimpleName();
    private static final String SERVER_IP = "103.254.13.25";
    private static final int PORT = 8081;

    private Socket socket;
    private boolean running = false;
    private PrintWriter out;
    private BufferedReader in;

    private static SocketAPI instance;

    private SocketAPI() {
    }

    public static SocketAPI getInstance() {
        if (instance == null) {
            instance = new SocketAPI();
        }
        return instance;
    }

    public void start() {
        if (!running) {
            running = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = new Socket(SERVER_IP, PORT);
                        out = new PrintWriter(socket.getOutputStream());
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        if (listener != null) {
                            listener.onSocketOpened(true);
                        }
                        Log.d(TAG, "Socket started");
                        listen();
                    } catch (Exception e) {
                        if (listener != null) {
                            listener.onSocketOpened(false);
                        }
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void listen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Socket listening...");
                while (running) {
                    try {
                        String response = in.readLine();
                        JSONObject json = new JSONObject(response);
                        Intent intent = new Intent(String.valueOf(json.optInt("command")));
                        intent.putExtra("data", response);
                        intent.putExtra("error", json.optJSONObject("apiMessage").optInt("error_code") != 0);
                        LocalBroadcastManager.getInstance(MainApplication.getAppContext()).sendBroadcast(intent);
                        Log.d(TAG, "Socket response: " + response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        stop(true);
                        return;
                    }
                }
            }
        }).start();
    }

    public void send(final String data) {
        if (running) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        out.print(data + "\n");
                        out.flush();
                        Log.d(TAG, "Socket send: " + data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void stop() {
        stop(false);
    }

    private void stop(boolean inside) {
        if (running) {
            try {
                running = false;
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
                if (socket != null)
                    socket.close();
                if (inside && listener != null)
                    listener.onSocketClosed(inside);
                Log.d(TAG, "Socket stopped");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    private ClientSocketListener listener;

    public void setClientSocketListener(ClientSocketListener listener) {
        this.listener = listener;
    }

    public interface ClientSocketListener {
        void onSocketOpened(boolean isSuccess);

        void onSocketClosed(boolean reasonFromInside);
    }

    // APIs

    public void login() {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.LOGIN)
                .put("token", DataUtils.getToken())
                .create();
        send(json);
    }

    // với những địa điểm không phải là appointment
    public void pushTimeline(UserPost userPost) {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.PUSH_TIMELINE)
                .put("content", "")
                .put("lat", userPost.lat)
                .put("lon", userPost.lon)
                .put("fromTime", userPost.from)
                .put("toTime", userPost.to)
                .put("zone", userPost.zone)
                .put("type", userPost.type)
                .create();
        send(json);
    }

    public void timelineOfUser(int from, int size) {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.TIMELINE_OF_USER)
                .put("from", from)
                .put("size", size)
                .create();
        send(json);
    }

    public void checkIn() {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.CHECK_IN)
                .put("idAppointment", 0)
                .put("fromTime", 0)
                .create();
        send(json);
    }

    public void checkOut() {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.CHECK_OUT)
                .put("idTimeline", 0)
                .put("toTime", 0)
                .create();
        send(json);
    }

    public void updateLocation(double lat, double lon) {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.UPDATE_LOCATION)
                .put("lat", lat)
                .put("lon", lon)
                .create();
        send(json);
    }

    public void updateBatery() {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.UPDATE_BATERY)
                .put("batery", Utils.getBatteryLevel(MainApplication.getAppContext()))
                .create();
        send(json);
    }

    public void userOfGroupTimeline(long groupId) {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.USER_OF_GROUP_TIMELINE)
                .put("groupId", groupId)
                .create();
        send(json);
    }

    public void createPlace(Place place) {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.CREATE_PLACE)
                .put("groupId", place.groupId)
                .put("name", place.name)
                .put("lat", place.lat)
                .put("lon", place.lon)
                .put("zone", place.zone)
                .put("type", place.type)
                .create();
        send(json);
    }

    public void updatePlace(long appointmentId, String name, double lat, double lon, int zone) {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.UPDATE_PLACE)
                .put("appointmentId", appointmentId)
                .put("name", name)
                .put("lat", lat)
                .put("lon", lon)
                .put("zone", zone)
                .create();
        send(json);
    }

    public void removePlace(long groupId, long placeID) {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.REMOVE_PLACE)
                .put("groupId", groupId)
                .put("placeId", placeID)
                .create();
        send(json);
    }

    public void placeOfUser(long groupId, int from, int size) {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.PLACE_OF_USER)
                .put("groupId", groupId)
                .put("from", from)
                .put("size", size)
                .create();
        send(json);
    }

    public void checkInPlace(long groupId, long placeId) {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.CHECK_IN_PLACE)
                .put("groupId", groupId)
                .put("placeId", placeId)
                .create();
        send(json);
    }

    public void checkOutPlace(long groupId, long placeId) {
        String json = new JSONStringBuilder()
                .put("command", ActionCode.CHECK_OUT_PLACE)
                .put("groupId", groupId)
                .put("placeId", placeId)
                .create();
        send(json);
    }

}

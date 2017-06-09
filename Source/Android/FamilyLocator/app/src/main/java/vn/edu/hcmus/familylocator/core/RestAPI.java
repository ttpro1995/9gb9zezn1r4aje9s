package vn.edu.hcmus.familylocator.core;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import vn.edu.hcmus.familylocator.utils.ThreadUtils;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

/**
 * Created by quangcat on 4/29/17.
 */

public class RestAPI {

    private static final String TAG = RestAPI.class.getSimpleName();
    public static final String URL_BASE = "http://103.254.13.25:8080";
    public static final String URL_API_PATH = URL_BASE + "/api";
    public static final String URL_CREATE_USER = URL_API_PATH + "/createUser";
    public static final String URL_GET_USER = URL_API_PATH + "/getUser?uid=%d";
    public static final String URL_LOGIN = URL_API_PATH + "/login?userName=%s&password=%s";
    public static final String URL_UPDATE_USER = URL_API_PATH + "/updateUser";
    public static final String URL_CHECK_EXISTS_USERNAME = URL_API_PATH + "/checkExistsUserName?userName=%s";
    public static final String URL_CREATE_GROUP = URL_API_PATH + "/createGroup";
    public static final String URL_UPDATE_GROUP = URL_API_PATH + "/updateGroup";
    public static final String URL_GET_LIST_GROUP_OF_USER = URL_API_PATH + "/getGroupOfUser";
    public static final String URL_GET_LIST_USER_OF_GROUP = URL_API_PATH + "/getUserOfGroup?groupId=%d";
    public static final String URL_GET_CODE_GROUP_SHARE = URL_API_PATH + "/getCodeGroupShare?groupId=%d";
    public static final String URL_JOIN_GROUP = URL_API_PATH + "/joinGroup";
    public static final String URL_CREATE_APPOINTMENT = URL_API_PATH + "/createAppointment";
    public static final String URL_APPOINTMENT_OF_GROUP = URL_API_PATH + "/appointmentOfGroup";
    public static final String URL_REMOVE_APPOINTMENT = URL_API_PATH + "/removeAppointment";
    public static final String URL_LEAVE_GROUP = URL_API_PATH + "/leaveGroup";
    public static final String URL_KICK_USER_OF_GROUP = URL_API_PATH + "/kickUserOfGroup";
    public static final String URL_REQUEST_FRIEND = URL_API_PATH + "/requestFriend";
    public static final String URL_ACCEPT_FRIEND = URL_API_PATH + "/acceptFriend";
    public static final String URL_DENY_FRIEND = URL_API_PATH + "/denyFriend";
    public static final String URL_GET_LIST_REQUESTED = URL_API_PATH + "/friendRequested?from=%d&size=%d";
    public static final String URL_GET_LIST_REQUESTING = URL_API_PATH + "/friendRequesting?from=%d&size=%d";
    public static final String URL_GET_LIST_FRIEND = URL_API_PATH + "/getFriend?from=%d&size=%d";
    public static final String URL_UNFRIEND = URL_API_PATH + "/unFriend";


    public interface Callback {
        void onDone(JSONObject responseData, boolean hasError);
    }

    private static void post(final String url, final String json, final String token, final Callback callback, final boolean showProgressDialog, Context context) {
        if (showProgressDialog) {
            ViewUtils.showProgressDialog(context);
        }
        ThreadUtils.processWithNewThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("token", token == null ? "" : token)
                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json == null ? "" : json))
                        .build();
                String result = null;
                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (callback != null) {
                    if (result == null)
                        callback.onDone(null, true);
                    else {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (json == null)
                            callback.onDone(null, true);
                        else {
                            final JSONObject finalJson = json;
                            final boolean hasError = (finalJson.optInt("error_code") != 0);
                            ThreadUtils.processWithUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (showProgressDialog)
                                        ViewUtils.closeProgressDialog();
                                    callback.onDone(finalJson, hasError);
                                }
                            });
                        }
                    }
                }

                Log.d(TAG, "[POST] URL= " + url + " | Request= " + json);
                Log.d(TAG, "[POST] URL= " + url + " | Response= " + result);
            }
        });
    }

    private static void get(final String url, final String token, final Callback callback, final boolean showProgressDialog, final Context context) {
        if (showProgressDialog) {
            ViewUtils.showProgressDialog(context);
        }
        ThreadUtils.processWithNewThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("token", token == null ? "" : token)
                        .build();
                String result = null;
                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (callback != null) {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final JSONObject finalJson = json;
                    final boolean hasError = (finalJson.optInt("error_code") != 0);
                    ThreadUtils.processWithUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (showProgressDialog) {
                                ViewUtils.closeProgressDialog();
                            }
                            callback.onDone(finalJson, hasError);
                        }
                    });
                }

                Log.d(TAG, "[GET] URL= " + url);
                Log.d(TAG, "[GET] URL= " + url + " | Response= " + result);
            }
        });
    }

    public static void createUser(String json, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_CREATE_USER, json, null, callback, showProgressDialog, context);
    }

    public static void getUser(long uid, String token, Callback callback, boolean showProgressDialog, Context context) {
        get(String.format(URL_GET_USER, uid), token, callback, showProgressDialog, context);
    }

    public static void login(String username, String password, Callback callback, boolean showProgressDialog, Context context) {
        get(String.format(URL_LOGIN, username, password), null, callback, showProgressDialog, context);
    }

    public static void updateUser(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_UPDATE_USER, json, token, callback, showProgressDialog, context);
    }

    public static void checkExistsUsername(String username, Callback callback, boolean showProgressDialog, Context context) {
        get(String.format(URL_CHECK_EXISTS_USERNAME, username), null, callback, showProgressDialog, context);
    }

    public static void createGroup(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_CREATE_GROUP, json, token, callback, showProgressDialog, context);
    }

    public static void updateGroup(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_UPDATE_GROUP, json, token, callback, showProgressDialog, context);
    }

    public static void getListGroupOfUser(String token, Callback callback, boolean showProgressDialog, Context context) {
        get(URL_GET_LIST_GROUP_OF_USER, token, callback, showProgressDialog, context);
    }

    public static void getUserListOfGroup(long gId, String token, Callback callback, boolean showProgressDialog, Context context) {
        get(String.format(URL_GET_LIST_USER_OF_GROUP, gId), token, callback, showProgressDialog, context);
    }

    public static void getCodeGroupShare(long gId, String token, Callback callback, boolean showProgressDialog, Context context) {
        get(String.format(URL_GET_CODE_GROUP_SHARE, gId), token, callback, showProgressDialog, context);
    }

    public static void joinGroup(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_JOIN_GROUP, json, token, callback, showProgressDialog, context);
    }

    public static void createAppointment(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_CREATE_APPOINTMENT, json, token, callback, showProgressDialog, context);
    }

    public static void appointmentOfGroup(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_APPOINTMENT_OF_GROUP, json, token, callback, showProgressDialog, context);
    }

    public static void removeAppointment(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_REMOVE_APPOINTMENT, json, token, callback, showProgressDialog, context);
    }

    public static void leaveGroup(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_LEAVE_GROUP, json, token, callback, showProgressDialog, context);
    }

    public static void kickUserOfGroup(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_KICK_USER_OF_GROUP, json, token, callback, showProgressDialog, context);
    }

    public static void requestFriend(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_REQUEST_FRIEND, json, token, callback, showProgressDialog, context);
    }

    public static void acceptFriend(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_ACCEPT_FRIEND, json, token, callback, showProgressDialog, context);
    }

    public static void denyFriend(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_DENY_FRIEND, json, token, callback, showProgressDialog, context);
    }

    public static void getListRequested(String token, Callback callback, boolean showProgressDialog, Context context) {
        get(URL_GET_LIST_REQUESTED, token, callback, showProgressDialog, context);
    }

    public static void getListRequesting(String token, Callback callback, boolean showProgressDialog, Context context) {
        get(URL_GET_LIST_REQUESTING, token, callback, showProgressDialog, context);
    }

    public static void getListFriend(String token, Callback callback, boolean showProgressDialog, Context context) {
        get(URL_GET_LIST_FRIEND, token, callback, showProgressDialog, context);
    }

    public static void unfriend(String json, String token, Callback callback, boolean showProgressDialog, Context context) {
        post(URL_UNFRIEND, json, token, callback, showProgressDialog, context);
    }

}

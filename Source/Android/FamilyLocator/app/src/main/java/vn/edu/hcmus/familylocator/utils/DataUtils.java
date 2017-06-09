package vn.edu.hcmus.familylocator.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmus.familylocator.core.JSONStringBuilder;
import vn.edu.hcmus.familylocator.core.MainApplication;
import vn.edu.hcmus.familylocator.models.UserPost;

/**
 * Created by quangcat on 5/1/17.
 */

public class DataUtils {

    private static final String KEY_UID = "uid";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_FIRST_NAME = "frist_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_UNCOMPLETE_POST = "post";


    private static final SharedPreferences PREF = PreferenceManager.getDefaultSharedPreferences(MainApplication.getAppContext());
    private static final SharedPreferences.Editor EDITOR = PREF.edit();

    public static long getUID() {
        return PREF.getLong(KEY_UID, 0);
    }

    public static String getToken() {
        return PREF.getString(KEY_TOKEN, null);
    }

    public static String getFirstName() {
        return PREF.getString(KEY_FIRST_NAME, null);
    }

    public static void setFirstName(String firstName) {
        EDITOR.putString(KEY_FIRST_NAME, firstName);
        EDITOR.apply();
    }

    public static String getLastName() {
        return PREF.getString(KEY_LAST_NAME, null);
    }

    public static void setLastName(String lastName) {
        EDITOR.putString(KEY_LAST_NAME, lastName);
        EDITOR.apply();
    }

    public static String getEmail() {
        return PREF.getString(KEY_EMAIL, null);
    }

    public static String getPhoneNumber() {
        return PREF.getString(KEY_PHONE_NUMBER, null);
    }

    public static String getAvatar() {
        return PREF.getString(KEY_AVATAR, null);
    }

    public static void saveLoginInfo(long uid, String token) {
        EDITOR.putLong(KEY_UID, uid);
        EDITOR.putString(KEY_TOKEN, token);
        EDITOR.apply();
    }

    public static void saveUserInfo(long uid, String firstName, String lastName, String email, String phoneNumber, String avatar) {
        EDITOR.putLong(KEY_UID, uid);
        EDITOR.putString(KEY_FIRST_NAME, firstName);
        EDITOR.putString(KEY_LAST_NAME, lastName);
        EDITOR.putString(KEY_EMAIL, email);
        EDITOR.putString(KEY_PHONE_NUMBER, phoneNumber);
        EDITOR.putString(KEY_AVATAR, avatar);
        EDITOR.apply();
    }

    public static void clear() {
        EDITOR.clear();
        EDITOR.apply();
    }

    public static UserPost getUncompletePost() {
        UserPost userPost = null;
        try {
            JSONObject json = new JSONObject(PREF.getString(KEY_UNCOMPLETE_POST, ""));
            userPost = new UserPost();
            userPost.lat = json.optDouble("lat");
            userPost.lon = json.optDouble("lon");
            userPost.from = json.optLong("from");
            userPost.to = json.optLong("to");
            userPost.completed = json.optBoolean("completed");
        } catch (JSONException e) {
            DataUtils.remove(KEY_UNCOMPLETE_POST);
            e.printStackTrace();
        }
        return userPost;
    }

    public static void saveUncompletePost(UserPost userPost) {
        if (userPost == null) {
            EDITOR.putString(KEY_UNCOMPLETE_POST, null);
            return;
        }
        String json = new JSONStringBuilder()
                .put("lat", userPost.lat)
                .put("lon", userPost.lon)
                .put("from", userPost.from)
                .put("to", userPost.to)
                .put("completed", userPost.completed)
                .create();
        EDITOR.putString(KEY_UNCOMPLETE_POST, json);
        EDITOR.apply();
    }

    public static void remove(String key) {
        EDITOR.remove(key);
        EDITOR.apply();
    }

}

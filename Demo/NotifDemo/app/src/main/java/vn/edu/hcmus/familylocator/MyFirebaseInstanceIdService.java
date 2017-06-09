package vn.edu.hcmus.familylocator;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by quangcat on 6/2/17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Intent intent = new Intent("token_refresh");
        intent.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}

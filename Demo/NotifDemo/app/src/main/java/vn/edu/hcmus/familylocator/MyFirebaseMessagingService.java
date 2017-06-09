package vn.edu.hcmus.familylocator;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by quangcat on 6/2/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent("notification");

        if (remoteMessage.getNotification() != null) {
            intent.putExtra("message_text", remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            intent.putExtra("message_data", remoteMessage.getData().toString());
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}

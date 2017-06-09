package vn.edu.hcmus.familylocator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textView = new TextView(this);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(10);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String token = FirebaseInstanceId.getInstance().getToken();
                if (TextUtils.isEmpty(token)) {
                    Toast.makeText(getBaseContext(), "Token is null. Nothing to copy.", Toast.LENGTH_SHORT).show();
                } else {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setPrimaryClip(ClipData.newPlainText("token", token));
                    Toast.makeText(getBaseContext(), "Token copied.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        setContentView(textView);

        log("LONG TOUCH SCREEN TO COPY TOKEN!!!");

        String token = FirebaseInstanceId.getInstance().getToken();
        if (!TextUtils.isEmpty(token)) {
            log("Token: " + token);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("token_refresh"));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("notification"));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("token_refresh")) {
                log("Token: " + intent.getStringExtra("token"));
            } else if (action.equals("notification")) {
                log("NOTIFICATION RECEIVED");
                if (intent.hasExtra("message_text")) {
                    log(intent.getStringExtra("message_text"));
                }
                if (intent.hasExtra("message_data")) {
                    log(intent.getStringExtra("message_data"));
                }
            }
        }
    };

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    void log(String content) {
        textView.append(simpleDateFormat.format(new Date(System.currentTimeMillis())) + " | " + content + "\n");
    }

}

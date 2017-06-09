package vn.edu.hcmus.familylocator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.adapters.MessageAdapter;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

public class MessageListActivity extends AppCompatActivity {

    private ListView mMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        ViewUtils.setupActionBar(this, "Tin Nhắn", true, 0);

        mMessages = (ListView) findViewById(R.id.list_view);
        mMessages.setAdapter(new MessageAdapter(this));
        mMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MessageListActivity.this, ChatActivity.class));
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_friend_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

}

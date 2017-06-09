package vn.edu.hcmus.familylocator.activities;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.adapters.ConversationAdapter;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mMessageList;
    private EditText mMessage;
    private ConversationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ViewUtils.setupActionBar(this, "Tin Nhắn", true, 0);

        mMessageList = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ConversationAdapter(this);
        mMessageList.setAdapter(mAdapter);
        mMessageList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int itemCount = state.getItemCount();
                int position = parent.getChildAdapterPosition(view);
                if (itemCount > 0) {
                    if (position == itemCount - 1) {
                        outRect.set(0, ViewUtils.dp(5), 0, ViewUtils.dp(5));
                    } else {
                        outRect.set(0, ViewUtils.dp(5), 0, 0);
                    }
                }

            }
        });

        mMessage = (EditText) findViewById(R.id.message);
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

    public void message_click(View v) {
        mMessageList.smoothScrollToPosition(mMessageList.getAdapter().getItemCount() - 1);
    }

    public void send(View v) {
        String msg = mMessage.getText().toString();
        if (!msg.equals("")) {

        }
    }

}

package vn.edu.hcmus.familylocator.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.adapters.UserTimelineAdapter;
import vn.edu.hcmus.familylocator.core.ActionCode;
import vn.edu.hcmus.familylocator.core.SocketAPI;
import vn.edu.hcmus.familylocator.models.UserPost;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

public class TimelineActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener {

    private static final int ITEM_COUNT_PER_PAGE = 15;

    private ListView listView;
    private UserTimelineAdapter adapter;
    private int currentPage = 0;
    private boolean loading = false;
    private int total = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ViewUtils.setupActionBar(this, "Dòng Thời Gian", true, 0);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setOnScrollListener(this);
        adapter = new UserTimelineAdapter(this);
        listView.setAdapter(adapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(String.valueOf(ActionCode.TIMELINE_OF_USER)));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int actionCode = Integer.parseInt(intent.getAction());
            if (actionCode == ActionCode.TIMELINE_OF_USER) {
                if (intent.getBooleanExtra("error", false)) {
                    Toast.makeText(TimelineActivity.this, "Không thể kết nối đến máy chủ.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String json = intent.getStringExtra("data");
                        JSONObject data = new JSONObject(json);
                        data = data.optJSONObject("apiMessage").optJSONObject("data");
                        total = data.optInt("total");
                        ArrayList<UserPost> pageData = new ArrayList<>();
                        JSONArray array = data.optJSONArray("timelines");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            UserPost item = new UserPost();
                            item.id = obj.optLong("id");
                            item.content = obj.optString("content");
                            item.lat = obj.optJSONObject("latLon").optDouble("lat");
                            item.lon = obj.optJSONObject("latLon").optDouble("lon");
                            item.from = obj.optLong("fromTime");
                            item.to = obj.optLong("toTime");
                            item.zone = obj.optInt("zone");
                            item.type = obj.optInt("type");
                            pageData.add(item);
                        }
                        adapter.addAll(pageData);
                        currentPage++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                loading = false;
            }
        }
    };

    private void nextPage() {
        if (loading)
            return;

        int from = currentPage * ITEM_COUNT_PER_PAGE;
        if (from > total)
            return;

        loading = true;
        SocketAPI.getInstance().timelineOfUser(from, ITEM_COUNT_PER_PAGE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount >= totalItemCount - 3) {
            nextPage();
        }
    }

}

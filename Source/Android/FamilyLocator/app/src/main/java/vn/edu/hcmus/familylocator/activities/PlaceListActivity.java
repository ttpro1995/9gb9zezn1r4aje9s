package vn.edu.hcmus.familylocator.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import vn.edu.hcmus.familylocator.adapters.PlaceAdapter;
import vn.edu.hcmus.familylocator.core.ActionCode;
import vn.edu.hcmus.familylocator.core.JSONStringBuilder;
import vn.edu.hcmus.familylocator.core.SocketAPI;
import vn.edu.hcmus.familylocator.models.Place;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

public class PlaceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = PlaceListActivity.class.getSimpleName();
    private static final int ITEM_COUNT_PER_PAGE = 15;

    private ListView listView;
    private PlaceAdapter adapter;
    private long groupId;
    private int currentPage = 0;
    private boolean loading = false;
    private int total = Integer.MAX_VALUE;
    private int deletingPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            groupId = getIntent().getLongExtra("group_id", 0);
            if (groupId == 0)
                throw new Exception();
        } catch (Exception e) {
            Log.e(TAG, "Không có dữ liệu group_id");
            finish();
            return;
        }

        setContentView(R.layout.list_view);
        ViewUtils.setupActionBar(this, "Địa Điểm", true, 0);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        listView.setOnItemLongClickListener(this);
        adapter = new PlaceAdapter(this);
        listView.setAdapter(adapter);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(String.valueOf(ActionCode.PLACE_OF_USER)));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(String.valueOf(ActionCode.REMOVE_PLACE)));
        nextPage();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean error = intent.getBooleanExtra("error", false);
            String response = intent.getStringExtra("data");
            switch (Integer.parseInt(intent.getAction())) {
                case ActionCode.PLACE_OF_USER:
                    if (error) {
                        Toast.makeText(PlaceListActivity.this, "Không thể lấy danh sách địa điểm.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        try {
                            JSONObject data = new JSONObject(response);
                            data = data.optJSONObject("apiMessage").optJSONObject("data");
                            total = data.optInt("total");
                            ArrayList<Place> pageData = new ArrayList<>();
                            JSONArray array = data.optJSONArray("places");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.optJSONObject(i);
                                Place item = new Place();
                                item.groupId = obj.optLong("id");
                                item.name = obj.optString("name");
                                item.lat = obj.optJSONObject("latlon").optDouble("lat");
                                item.lon = obj.optJSONObject("latlon").optDouble("lon");
                                item.zone = obj.optInt("zone");
                                pageData.add(item);
                            }
                            adapter.addAll(pageData);
                            currentPage++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    loading = false;
                    break;
                case ActionCode.REMOVE_PLACE:
                    ViewUtils.closeProgressDialog();
                    ;
                    if (error) {
                        Toast.makeText(PlaceListActivity.this, "Xóa địa điểm thất bại.", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.remove(deletingPosition);
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.new_item:
                Intent intent = new Intent(this, NewOrUpdatePlaceActivity.class);
                intent.putExtra("group_id", groupId);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void nextPage() {
        if (loading)
            return;

        int from = currentPage * ITEM_COUNT_PER_PAGE;
        if (from > total)
            return;

        loading = true;
        SocketAPI.getInstance().placeOfUser(groupId, from, ITEM_COUNT_PER_PAGE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, NewOrUpdatePlaceActivity.class);
        intent.putExtra("new_place_mode", false);
        Place p = (Place) adapter.getItem(position);
        intent.putExtra("data",
                new JSONStringBuilder()
                        .put("group_id", p.groupId)
                        .put("name", p.name)
                        .put("lat", p.lat)
                        .put("lon", p.lon)
                        .put("zone", p.zone)
                        .put("type", p.type)
                        .create());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        ViewUtils.createConfirmDialog(this, "Bạn có muốn xóa địa điểm này?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    ViewUtils.showProgressDialog(PlaceListActivity.this);
                    deletingPosition = position;
                    SocketAPI.getInstance().removePlace(groupId, ((Place) adapter.getItem(position)).groupId);
                }
            }
        }, true).show();
        return true;
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

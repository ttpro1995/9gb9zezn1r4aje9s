package vn.edu.hcmus.familylocator.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import org.json.JSONObject;

import java.util.ArrayList;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.adapters.AppointmentAdapter;
import vn.edu.hcmus.familylocator.core.JSONStringBuilder;
import vn.edu.hcmus.familylocator.core.RestAPI;
import vn.edu.hcmus.familylocator.models.Appointment;
import vn.edu.hcmus.familylocator.utils.DataUtils;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

public class AppointmentListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = AppointmentListActivity.class.getSimpleName();
    private static final int ITEM_COUNT_PER_PAGE = 15;

    private ListView listView;
    private AppointmentAdapter adapter;
    private long groupID;
    private int currentPage = 0;
    private boolean loading = false;
    private int total = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            groupID = getIntent().getLongExtra("group_id", 0);
            if (groupID == 0)
                throw new Exception();
        } catch (Exception e) {
            Log.e(TAG, "Không có dữ liệu group_id");
            finish();
            return;
        }

        setContentView(R.layout.list_view);
        ViewUtils.setupActionBar(this, "Điểm Hẹn", true, 0);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        listView.setOnItemLongClickListener(this);
        adapter = new AppointmentAdapter(this);
        listView.setAdapter(adapter);

        nextPage();
    }

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
                Intent intent = new Intent(this, NewAppointmentActivity.class);
                intent.putExtra("group_id", groupID);
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
        String json = new JSONStringBuilder()
                .put("groupId", groupID)
                .put("from", from)
                .put("size", ITEM_COUNT_PER_PAGE)
                .create();
        RestAPI.appointmentOfGroup(json, DataUtils.getToken(), new RestAPI.Callback() {
            @Override
            public void onDone(JSONObject responseData, boolean hasError) {
                if (hasError) {
                    Toast.makeText(AppointmentListActivity.this, "Không thể lấy danh sách điểm hẹn.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    JSONObject data = responseData.optJSONObject("data");
                    total = data.optInt("total");
                    ArrayList<Appointment> pageData = new ArrayList<>();
                    JSONArray array = data.optJSONArray("appointments");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.optJSONObject(i);
                        Appointment item = new Appointment();
                        item.id = obj.optLong("id");
                        item.name = obj.optString("name");
                        JSONObject latlng = obj.optJSONObject("latlon");
                        item.lat = latlng.optDouble("lat");
                        item.lng = latlng.optDouble("lon");
                        item.time = obj.optLong("time");
                        item.zone = obj.optInt("zone");
                        pageData.add(item);
                    }
                    adapter.addAll(pageData);
                    currentPage++;
                }
                loading = false;
            }
        }, false, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        ViewUtils.createConfirmDialog(this, "Bạn có muốn xóa điểm hẹn này?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    String json = new JSONStringBuilder()
                            .put("groupId", groupID)
                            .put("appointmentId", ((Appointment) adapter.getItem(position)).id)
                            .create();
                    RestAPI.removeAppointment(json, DataUtils.getToken(), new RestAPI.Callback() {
                        @Override
                        public void onDone(JSONObject responseData, boolean hasError) {
                            if (hasError) {
                                Toast.makeText(AppointmentListActivity.this, "Xóa điểm hẹn thất bại.", Toast.LENGTH_SHORT).show();
                            } else {
                                adapter.remove(position);
                            }
                        }
                    }, true, AppointmentListActivity.this);
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

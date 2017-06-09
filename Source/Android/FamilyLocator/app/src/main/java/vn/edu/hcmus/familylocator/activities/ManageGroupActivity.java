package vn.edu.hcmus.familylocator.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.adapters.MemberAdapter;
import vn.edu.hcmus.familylocator.core.JSONStringBuilder;
import vn.edu.hcmus.familylocator.core.RestAPI;
import vn.edu.hcmus.familylocator.models.Member;
import vn.edu.hcmus.familylocator.utils.DataUtils;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

public class ManageGroupActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = ManageGroupActivity.class.getSimpleName();
    public static int RESULT_LEAVE_GROUP = 1;
    public static int RESULT_KICK_MEMBER = 2;

    private ListView listView;
    private MemberAdapter adapter;
    private long groupId;
    private boolean isAdmin = false;

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
        ViewUtils.setupActionBar(this, "Quản Lý Nhóm", true, 0);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        adapter = new MemberAdapter(this);
        listView.setAdapter(adapter);

        getData();
    }

    private void getData() {
        RestAPI.getUserListOfGroup(groupId, DataUtils.getToken(), new RestAPI.Callback() {
            @Override
            public void onDone(JSONObject responseData, boolean hasError) {
                if (hasError) {
                    Toast.makeText(ManageGroupActivity.this, "Không thể lấy danh sách điểm hẹn.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    JSONObject data = responseData.optJSONObject("data");
                    ArrayList<Member> pageData = new ArrayList<>();
                    JSONArray array = data.optJSONArray("userOfGroups");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.optJSONObject(i);
                        Member item = new Member();
                        item.id = obj.optLong("id");
                        item.firstName = obj.optString("firstName");
                        item.lastName = obj.optString("lastName");
                        item.avatar = obj.optString("avatar");
                        item.admin = obj.optBoolean("admin");
                        pageData.add(item);
                    }
                    adapter.addAll(pageData);

                    for (int i = 0; i < pageData.size(); i++) {
                        Member current = pageData.get(i);
                        if (current.id == DataUtils.getUID() && current.admin) {
                            isAdmin = true;
                            break;
                        }
                    }
                }
            }
        }, true, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final Member selectedMember = (Member) adapter.getItem(position);
        final boolean isMe = selectedMember.id == DataUtils.getUID();
        if (isMe) {
            ViewUtils.createConfirmDialog(this, "Bạn muốn rời khỏi nhóm này?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        leaveGroup();
                    }
                }
            }, true).show();
        } else if (isAdmin) {
            ViewUtils.createConfirmDialog(this, "Xóa người này khỏi danh sách?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        kickMember(selectedMember.id, position);
                    }
                }
            }, true).show();
        }
    }

    private void leaveGroup() {
        String json = new JSONStringBuilder()
                .put("groupId", groupId)
                .create();
        RestAPI.leaveGroup(json, DataUtils.getToken(), new RestAPI.Callback() {
            @Override
            public void onDone(JSONObject responseData, boolean hasError) {
                if (hasError) {
                    Toast.makeText(ManageGroupActivity.this, "Đã xảy ra lỗi.", Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_LEAVE_GROUP);
                    finish();
                }
            }
        }, true, ManageGroupActivity.this);
    }

    private void kickMember(long memberId, final int position) {
        String json = new JSONStringBuilder()
                .put("groupId", groupId)
                .put("uid", memberId)
                .create();
        RestAPI.kickUserOfGroup(json, DataUtils.getToken(), new RestAPI.Callback() {
            @Override
            public void onDone(JSONObject responseData, boolean hasError) {
                if (hasError) {
                    Toast.makeText(ManageGroupActivity.this, "Đã xảy ra lỗi.", Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_KICK_MEMBER);
                    adapter.remove(position);
                }
            }
        }, true, ManageGroupActivity.this);
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

}

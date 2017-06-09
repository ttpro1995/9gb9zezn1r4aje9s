package vn.edu.hcmus.familylocator.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.adapters.SectionsPagerAdapter;
import vn.edu.hcmus.familylocator.core.JSONStringBuilder;
import vn.edu.hcmus.familylocator.core.RestAPI;
import vn.edu.hcmus.familylocator.utils.DataUtils;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

public class FriendListActivity extends AppCompatActivity {

    private static final String TAG = FriendListActivity.class.getSimpleName();

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ViewUtils.setupActionBar(this, "Bạn Bè", true, 0);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            tabLayout.getTabAt(i).setIcon(R.drawable.avatar);
//        }
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
                showAddFriendDialog();
                break;
        }

        return true;
    }

    private void showAddFriendDialog() {
        final Dialog dlg = ViewUtils.createDialog(this, R.layout.dialog_add_friend, true);
        dlg.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = ((EditText) dlg.findViewById(R.id.username)).getText().toString();
                String json = new JSONStringBuilder()
                        .put("userName", userName)
                        .create();
                RestAPI.requestFriend(json, DataUtils.getToken(), new RestAPI.Callback() {
                    @Override
                    public void onDone(JSONObject responseData, boolean hasError) {
                        if (hasError) {
                            switch (responseData.optInt("error_code")) {
                                case 1010:
                                    Toast.makeText(FriendListActivity.this, "Tài khoản không tồn tại.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1011:
                                    Toast.makeText(FriendListActivity.this, "Đã thêm bạn người này.", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(FriendListActivity.this, "Thêm bạn thất bại.", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            dlg.dismiss();
                        }
                    }
                }, true, FriendListActivity.this);
            }
        });
        dlg.show();
    }

}

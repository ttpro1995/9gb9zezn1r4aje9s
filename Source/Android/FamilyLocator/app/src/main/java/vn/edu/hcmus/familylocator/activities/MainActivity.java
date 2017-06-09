package vn.edu.hcmus.familylocator.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.adapters.GroupListAdapter;
import vn.edu.hcmus.familylocator.adapters.GroupTimelineAdapter;
import vn.edu.hcmus.familylocator.core.ActionCode;
import vn.edu.hcmus.familylocator.core.MainService;
import vn.edu.hcmus.familylocator.core.SocketAPI;
import vn.edu.hcmus.familylocator.core.JSONStringBuilder;
import vn.edu.hcmus.familylocator.core.RestAPI;
import vn.edu.hcmus.familylocator.miscs.GenericItemHolder;
import vn.edu.hcmus.familylocator.models.GroupPost;
import vn.edu.hcmus.familylocator.utils.DataUtils;
import vn.edu.hcmus.familylocator.utils.ThreadUtils;
import vn.edu.hcmus.familylocator.utils.ViewUtils;
import vn.edu.hcmus.familylocator.views.QDrawerLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GroupListAdapter.GroupListAdapterListener, SocketAPI.ClientSocketListener, GroupTimelineAdapter.GroupTimelineListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int THRESHOLD = 5 * 60 * 1000;
    private static final int REQ_CODE_MANAGE_GROUP = 100;

    //    private GoogleMap mGMap;
//    private DatabaseReference mDbRef;
//    private ArrayList<UserInfo> mPoints = new ArrayList<>();
//    private boolean isFirstTime = true;
//
//    private Handler handler = new Handler();
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            redrawMarkers();
//            handler.postDelayed(this, THRESHOLD);
//        }
//    };
//
//    private String userId;
//    private String userName;


    private QDrawerLayout drawer;
    private RecyclerView recyclerView;
    private GroupListAdapter groupAdapter;
    private ListView listView;
    private GroupTimelineAdapter timelineAdapter;

    private GoogleMap googleMap;

    private SocketAPI socketAPI = SocketAPI.getInstance();

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent serviceIntent = new Intent(MainActivity.this, MainService.class);
            boolean error = intent.getBooleanExtra("error", false);
            switch (Integer.parseInt(intent.getAction())) {
                case ActionCode.LOGIN:
                    ViewUtils.closeProgressDialog();
                    if (error) {
                        stopService(serviceIntent);
                        Toast.makeText(context, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        startService(serviceIntent);
                        groupAdapter = new GroupListAdapter(recyclerView);
                        groupAdapter.setOnItemLongClickListener(MainActivity.this);
                        recyclerView.setAdapter(groupAdapter);
                    }

                    // Check permission state
//                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
//                    }
                    // Check enable state
//                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                    if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//                        ViewUtils.createConfirmDialog(MainActivity.this, "Bạn cần phải kích hoạt GPS trước khi sử dụng ứng dụng này. Nhấn \"Thiết lập\" để đi đến menu kích hoạt GPS.", "Thiết lập", "Hủy bỏ", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (which == DialogInterface.BUTTON_POSITIVE) {
//                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
//                                    finish();
//                                }
//                            }
//                        }, false).show();
//                    }
                    break;

                case ActionCode.USER_OF_GROUP_TIMELINE:
                    if (error) {
                        Log.e(TAG, "Không thể lấy thông tin timeline");
                    } else {
                        try {
                            String json = intent.getStringExtra("data");
                            JSONObject data = new JSONObject(json);
                            JSONArray array = data.optJSONObject("apiMessage").optJSONObject("data").optJSONArray("users");
                            ArrayList<GroupPost> realData = new ArrayList<>();
                            ArrayList<Pair<String, LatLng>> drawData = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject item = array.optJSONObject(i);
                                GroupPost p = new GroupPost();
                                p.id = item.optLong("id");
                                p.firstName = item.optString("firstName");
                                p.lastName = item.optString("lastName");
                                p.avatar = item.optString("avatar");
                                JSONObject location = item.optJSONObject("location");
                                p.lat = location.optDouble("lat");
                                p.lon = location.optDouble("lon");
                                drawData.add(new Pair<>(p.getFullname(), new LatLng(p.lat, p.lon)));
                                p.battery = item.optInt("batery");
                                realData.add(p);
                            }
                            timelineAdapter = new GroupTimelineAdapter(MainActivity.this, realData);
                            timelineAdapter.setGroupTimelineListener(MainActivity.this);
                            listView.setAdapter(timelineAdapter);
                            drawMarker(drawData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(String.valueOf(ActionCode.LOGIN)));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(String.valueOf(ActionCode.USER_OF_GROUP_TIMELINE)));

        // Start socket
        ViewUtils.showProgressDialog(this);
        socketAPI.setClientSocketListener(this);
        socketAPI.start();


    }

    private void initViews() {
        ViewUtils.setupActionBar(this, "Life360", true, R.drawable.ic_menu);

        // Swipe menu
        drawer = (QDrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        recyclerView = (RecyclerView) menu.findItem(R.id.circle_list).getActionView().findViewById(R.id.recycler_view);

        View v = menu.findItem(R.id.circle_panel).getActionView();
        v.findViewById(R.id.create_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewGroupDialog();
                closeSwipeMenu();
            }
        });
        v.findViewById(R.id.join_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinGroupDialog();
                closeSwipeMenu();
            }
        });

        listView = (ListView) findViewById(R.id.list_view);

        final MapView mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(null);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MainActivity.this.googleMap = googleMap;
                mapView.onResume();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        socketAPI.stop();
    }

    private void showNewGroupDialog() {
        final Dialog dialog = ViewUtils.createDialog(this, R.layout.dialog_new_group, true);
        final EditText groupName = (EditText) dialog.findViewById(R.id.group_name);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.suggest_name);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int margin = ViewUtils.dp(2);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int count = state.getItemCount();
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.set(0, 0, margin, 0);
                } else if (count > 0 && position == count - 1) {
                    outRect.set(margin, 0, 0, 0);
                } else {
                    outRect.set(margin, 0, margin, 0);
                }
            }
        });
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            String[] suggestName = new String[]{"Gia đình", "Người yêu", "Bạn bè", "Bạn thân", "Đồng nghiệp", "Hàng xóm"};
            int paddingTopBottom = ViewUtils.dp(3);
            int paddingLeftRight = ViewUtils.dp(6);

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final TextView tv = new TextView(MainActivity.this);
                tv.setPadding(paddingLeftRight, paddingTopBottom, paddingLeftRight, paddingTopBottom);
                tv.setBackgroundResource(R.drawable.rounded_background2);
                tv.setTextColor(Color.WHITE);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        groupName.setText(tv.getText().toString());
                    }
                });
                return new GenericItemHolder(tv);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView).setText(suggestName[position]);
            }

            @Override
            public int getItemCount() {
                return suggestName.length;
            }
        });
        dialog.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = groupName.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên nhóm.", Toast.LENGTH_SHORT).show();
                    groupName.requestFocus();
                    return;
                }

                String json = new JSONStringBuilder()
                        .put("name", name)
                        .create();
                RestAPI.createGroup(json, DataUtils.getToken(), new RestAPI.Callback() {
                    @Override
                    public void onDone(JSONObject responseData, boolean hasError) {
                        if (hasError) {
                            Toast.makeText(MainActivity.this, "Tạo nhóm thất bại.", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();

                            groupAdapter = new GroupListAdapter(MainActivity.this.recyclerView);
                            groupAdapter.setOnItemLongClickListener(MainActivity.this);
                            MainActivity.this.recyclerView.setAdapter(groupAdapter);

                            Toast.makeText(MainActivity.this, "Tạo nhóm thành công.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, true, MainActivity.this);
            }
        });
        dialog.show();
    }

    private void showJoinGroupDialog() {
        final Dialog dialog = ViewUtils.createDialog(this, R.layout.dialog_join_group, true);
        final EditText txtCode = (EditText) dialog.findViewById(R.id.code);
        dialog.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = txtCode.getText().toString();
                if (code.equals("")) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập mã tham gia nhóm.", Toast.LENGTH_SHORT).show();
                    txtCode.requestFocus();
                    return;
                }
                String json = new JSONStringBuilder()
                        .put("code", code)
                        .create();
                RestAPI.joinGroup(json, DataUtils.getToken(), new RestAPI.Callback() {
                    @Override
                    public void onDone(JSONObject responseData, boolean hasError) {
                        if (hasError) {
                            Toast.makeText(MainActivity.this, "Tham gia thất bại.", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();

                            groupAdapter = new GroupListAdapter(recyclerView);
                            groupAdapter.setOnItemLongClickListener(MainActivity.this);
                            recyclerView.setAdapter(groupAdapter);

                            Toast.makeText(MainActivity.this, "Tham gia thành công.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, true, MainActivity.this);
            }
        });
        dialog.show();
    }

    private boolean isSwipeMenuOpen() {
        return drawer.isDrawerOpen(GravityCompat.START);
    }

    private void openSwipeMenu() {
        drawer.openDrawer(GravityCompat.START);
    }

    private void closeSwipeMenu() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    closeSwipeMenu();
                } else {
                    openSwipeMenu();
                }
                break;
            case R.id.manage_group:
                Intent intent = new Intent(this, ManageGroupActivity.class);
                intent.putExtra("group_id", groupAdapter.getCurrentId());
                startActivityForResult(intent, REQ_CODE_MANAGE_GROUP);
                break;
            case R.id.share:
                showSendInviteCodeDialog();
                break;
        }

        return true;
    }

    long currentId = -1;
    String code = "";

    private void showSendInviteCodeDialog() {
        if (currentId != groupAdapter.getCurrentId()) {
            currentId = groupAdapter.getCurrentId();
            RestAPI.getCodeGroupShare(currentId, DataUtils.getToken(), new RestAPI.Callback() {
                @Override
                public void onDone(JSONObject responseData, boolean hasError) {
                    if (hasError) {
                        Toast.makeText(MainActivity.this, "Không thể lấy mã mời tham gia nhóm.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        code = responseData.optJSONObject("data").optString("code");
                        if (!code.equals("")) {
                            final Dialog dlg = ViewUtils.createDialog(MainActivity.this, R.layout.dialog_send_invite_code, true);
                            ((TextView) dlg.findViewById(R.id.code)).setText(code);
                            dlg.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(Intent.EXTRA_TEXT, code);
                                    startActivity(Intent.createChooser(sharingIntent, "Chia sẻ"));
                                    dlg.dismiss();
                                }
                            });
                            dlg.show();
                        }
                    }
                }
            }, true, MainActivity.this);
        } else {
            Dialog dlg = ViewUtils.createDialog(MainActivity.this, R.layout.dialog_send_invite_code, true);
            ((TextView) dlg.findViewById(R.id.code)).setText(code);
            dlg.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, code);
                    startActivity(Intent.createChooser(sharingIntent, "Chia sẻ"));
                }
            });
            dlg.show();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View v1 = (View) recyclerView.getParent();
        View v2 = (View) recyclerView.getParent().getParent();
        drawer.setInterceptRectange(new Rect(v1.getLeft(), v2.getTop(), v1.getRight(), v2.getBottom()));
    }

    private void init() {
        // Init map
//        MapView mapView = (MapView) findViewById(R.id.map_view);
//        mapView.onCreate(null);
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                mGMap = googleMap;
//            }
//        });
//        mapView.onResume();
//
//        // Init firebase
//        mDbRef = FirebaseDatabase.getInstance().getReference().child("users");
//        mDbRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                UserInfo p = dataSnapshot.getValue(UserInfo.class);
//                p.user_id = dataSnapshot.getKey();
//                mPoints.add(p);
//                redrawMarkers();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                UserInfo p = dataSnapshot.getValue(UserInfo.class);
//                p.user_id = dataSnapshot.getKey();
//
//                for (UserInfo i : mPoints) {
//                    if (i.user_id.equals(p.user_id)) {
//                        i.user_id = p.user_id;
//                        i.user_name = p.user_name;
//                        i.location = p.location;
//                        i.friends = p.friends;
//                        redrawMarkers();
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        // GPS tracking
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // Check permission state
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5, locationListener);

        // Check enable state
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            ViewUtils.createConfirmDialog(this, "Bạn cần phải kích hoạt GPS trước khi sử dụng ứng dụng này. Nhấn \"Thiết lập\" để đi đến menu kích hoạt GPS.", "Thiết lập", "Hủy bỏ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        finish();
                    }
                }
            }, false).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.profile:
                intent = new Intent(this, RegisterOrUpdateInfoActivity.class);
                intent.putExtra("register_mode", false);
                startActivity(intent);
                break;
            case R.id.friend_list:
                intent = new Intent(this, FriendListActivity.class);
                intent.putExtra("user_id", "quangcv");
                startActivity(intent);
                break;
            case R.id.appointment_list:
                intent = new Intent(this, AppointmentListActivity.class);
                intent.putExtra("group_id", groupAdapter.getCurrentId());
                startActivity(intent);
                break;
            case R.id.timeline:
                intent = new Intent(this, TimelineActivity.class);
                startActivity(intent);
                break;
            case R.id.inbox:
//                intent = new Intent(this, MessageListActivity.class);
                intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                break;
            case R.id.place_list:
                intent = new Intent(this, PlaceListActivity.class);
                intent.putExtra("group_id", groupAdapter.getCurrentId());
                startActivity(intent);
                break;
            case R.id.logout:
                DataUtils.clear();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        closeSwipeMenu();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isSwipeMenuOpen()) {
            closeSwipeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public void onItemLongClick(final int position, final Pair<Long, String> itemData) {
        final Dialog dialog = ViewUtils.createDialog(this, R.layout.dialog_new_group, true);
        final EditText groupName = (EditText) dialog.findViewById(R.id.group_name);
        groupName.setText(itemData.second);
        dialog.findViewById(R.id.hint).setVisibility(View.GONE);
        dialog.findViewById(R.id.suggest_name).setVisibility(View.GONE);
        Button action = (Button) dialog.findViewById(R.id.action);
        action.setText("Cập nhật");
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String name = groupName.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên nhóm.", Toast.LENGTH_SHORT).show();
                    groupName.requestFocus();
                    return;
                }

                String json = new JSONStringBuilder()
                        .put("name", name)
                        .put("groupId", itemData.first)
                        .create();
                RestAPI.updateGroup(json, DataUtils.getToken(), new RestAPI.Callback() {
                    @Override
                    public void onDone(JSONObject responseData, boolean hasError) {
                        if (hasError) {
                            Toast.makeText(MainActivity.this, "Cập nhật nhóm thất bại.", Toast.LENGTH_SHORT).show();
                        } else {
                            groupAdapter.updateItemDataAtPosition(position, new Pair<>(itemData.first, name));
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Cập nhật nhóm thành công.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, true, MainActivity.this);
            }
        });
        dialog.show();
    }

    @Override
    public void onFirstItemChanged() {
        long groupId = groupAdapter.getCurrentId();
        socketAPI.userOfGroupTimeline(groupId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_MANAGE_GROUP) {
            if (resultCode == ManageGroupActivity.RESULT_LEAVE_GROUP) {
                groupAdapter = new GroupListAdapter(recyclerView);
                groupAdapter.setOnItemLongClickListener(MainActivity.this);
                recyclerView.setAdapter(groupAdapter);
            } else if (resultCode == ManageGroupActivity.RESULT_KICK_MEMBER) {

            }
        }
    }

    @Override
    public void onSocketOpened(boolean isSuccess) {
        if (isSuccess) {
            socketAPI.login();
        } else {
            ThreadUtils.processWithUIThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Không thể kết nối đến máy chủ.", Toast.LENGTH_SHORT).show();
                    ViewUtils.closeProgressDialog();
                    finish();
                }
            });
        }
    }

    @Override
    public void onSocketClosed(boolean reasonFromInside) {
        if (!reasonFromInside) {
            ThreadUtils.processWithUIThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Không thể kết nối đến máy chủ.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }


    //    private void redrawMarkers() {
//        mGMap.clear();
//        if (mPoints.size() > 0) {
//            for (UserInfo p : mPoints) {
//                if (p.location != null && System.currentTimeMillis() - p.location.timestamp <= THRESHOLD) {
//                    Marker marker = mGMap.addMarker(new MarkerOptions().position(
//                            new LatLng(p.location.lat, p.location.lon)));
//                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerIcon(p.user_name)));
//                    if (p.user_id.equals(userId)) {
//                        marker.setTitle("YOU - " + p.user_name);
//                    } else {
//                        marker.setTitle(p.user_name);
//                    }
//                }
//            }
//        }
//    }
//
    private Bitmap getMarkerIcon(String name) {
        if (name.length() < 1) {
            return null;
        }

        name = String.valueOf(name.charAt(0));
        name = name.toUpperCase();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inMutable = true;
        Bitmap markerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.marker, opts);

        Canvas canvas = new Canvas(markerIcon);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#fa1b36"));
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(65);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(name, canvas.getWidth() / 2, 71, paint);

        return markerIcon;
    }

    private void drawMarker(ArrayList<Pair<String, LatLng>> data) {
        if (data.size() > 0) {
            googleMap.clear();
            for (int i = 0; i < data.size(); i++) {
                Pair<String, LatLng> item = data.get(i);
                Marker marker = googleMap.addMarker(new MarkerOptions().position(item.second));
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerIcon(item.first)));
                marker.setTitle(item.first);
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(data.get(0).second, 15f));
        }
    }

    @Override
    public void onItemClick(int position, boolean avatarClicked) {
        if (avatarClicked) {
            GroupPost p = (GroupPost) timelineAdapter.getItem(position);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p.getLocation(), 15f));
        } else {
            startActivity(new Intent(this, TimelineActivity.class));
        }
    }
}

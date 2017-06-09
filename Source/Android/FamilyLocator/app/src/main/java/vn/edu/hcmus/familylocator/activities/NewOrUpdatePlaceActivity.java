package vn.edu.hcmus.familylocator.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.core.ActionCode;
import vn.edu.hcmus.familylocator.core.SocketAPI;
import vn.edu.hcmus.familylocator.utils.Utils;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

public class NewOrUpdatePlaceActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = NewOrUpdatePlaceActivity.class.getSimpleName();

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private GoogleMap googleMap;

    private Button address;
    private EditText name;
    private TextView radius;
    private SeekBar seekBar;

    private boolean isNewPlaceMode = true;
    private vn.edu.hcmus.familylocator.models.Place place;
    private long groupId;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean error = intent.getBooleanExtra("error", false);
            switch (Integer.parseInt(intent.getAction())) {
                case ActionCode.CREATE_PLACE:
                    ViewUtils.closeProgressDialog();
                    if (error) {
                        Toast.makeText(NewOrUpdatePlaceActivity.this, "Tạo địa điểm thất bại.", Toast.LENGTH_SHORT).show();
                    } else {
                        NewOrUpdatePlaceActivity.this.setResult(RESULT_OK);
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        isNewPlaceMode = intent.getBooleanExtra("new_place_mode", true);
        if (isNewPlaceMode) {
            groupId = getIntent().getLongExtra("group_id", 0);
            if (groupId == 0) {
                Log.e(TAG, "Không có dữ liệu group_id");
                finish();
                return;
            }
        } else {
            String data = intent.getStringExtra("data");
            if (TextUtils.isEmpty(data)) {
                Log.e(TAG, "Không có dữ liệu data");
                finish();
                return;
            }
            try {
                JSONObject json = new JSONObject(data);
                place = new vn.edu.hcmus.familylocator.models.Place();
                place.groupId = json.optLong("group_id");
                place.name = json.optString("name");
                place.lat = json.optDouble("lat");
                place.lon = json.optDouble("lon");
                place.zone = json.optInt("zone");
                place.type = json.optInt("type");
            } catch (Exception e) {
                Log.e(TAG, "Không có dữ liệu data");
                finish();
                return;
            }
        }

        setContentView(R.layout.activity_new_place);
        ViewUtils.setupActionBar(this, isNewPlaceMode ? "Tạo Địa Điểm" : "Cập Nhật Địa Điểm", true, 0);

        initViews();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(String.valueOf(ActionCode.CREATE_PLACE)));
    }

    private void initViews() {
        name = (EditText) findViewById(R.id.name);
        address = (Button) findViewById(R.id.address);
        radius = (TextView) findViewById(R.id.radius);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);

        if (!isNewPlaceMode) {
            name.setText(place.name);
            address.setText("Đang lấy thông tin vị trí...");
            Utils.getCompleteAddressString(this, place.lat, place.lon, new Utils.Callback() {
                @Override
                public void onDone(String address) {
                    NewOrUpdatePlaceActivity.this.address.setText(address);
                }
            });
            address.setTag(new LatLng(place.lat, place.lon));
            seekBar.setProgress(convertKmToProgress(place.zone / 1000f));
        }

        final MapView mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(null);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                NewOrUpdatePlaceActivity.this.googleMap = googleMap;
                mapView.onResume();
                if (!isNewPlaceMode) {
                    drawMarker(new LatLng(place.lat, place.lon));
                }
            }
        });
    }

    private float convertProgressToKm(int progress) {
        float[] val = {0.5f, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        return val[progress];
    }

    private int convertKmToProgress(float km) {
        float[] val = {0.5f, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int i = 0; i < val.length; i++) {
            if (val[i] == km) {
                return i;
            }
        }
        return 0;
    }

    public void addressClick(View v) {
        try {
            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            address.setText(place.getName());
            address.setTag(place.getLatLng());
            drawMarker(place.getLatLng());
        }
    }

    private void drawMarker(LatLng location) {
        googleMap.clear();
        Marker marker = googleMap.addMarker(new MarkerOptions().position(location));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_new_appointment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.save:
                String name = this.name.getText().toString();
                String address = this.address.getText().toString();
                LatLng latLng = (LatLng) this.address.getTag();

                if (name.equals("") || address.toLowerCase().equals("set address")) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                    return true;
                }

                ViewUtils.showProgressDialog(this);
                if(isNewPlaceMode) {
                    vn.edu.hcmus.familylocator.models.Place place = new vn.edu.hcmus.familylocator.models.Place();
                    place.groupId = groupId;
                    place.name = name;
                    place.lat = latLng.latitude;
                    place.lon = latLng.longitude;
                    place.zone = (int) (convertProgressToKm(seekBar.getProgress()) * 1000);
                    place.type = 1;
                    SocketAPI.getInstance().createPlace(place);
                } else {
                    
                }
                break;
        }

        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float r = convertProgressToKm(NewOrUpdatePlaceActivity.this.seekBar.getProgress());
        radius.setText(String.valueOf(r) + " km");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

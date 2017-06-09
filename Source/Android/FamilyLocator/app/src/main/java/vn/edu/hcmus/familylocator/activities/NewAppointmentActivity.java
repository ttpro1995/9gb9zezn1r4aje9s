package vn.edu.hcmus.familylocator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.core.JSONStringBuilder;
import vn.edu.hcmus.familylocator.core.RestAPI;
import vn.edu.hcmus.familylocator.utils.DataUtils;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

public class NewAppointmentActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = NewAppointmentActivity.class.getSimpleName();

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private GoogleMap googleMap;

    private Button time;
    private Button address;
    private EditText name;
    private TextView radius;
    private SeekBar seekBar;

    private long groupID;

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

        setContentView(R.layout.activity_new_appointment);
        ViewUtils.setupActionBar(this, "Tạo Điểm Hẹn", true, 0);

        name = (EditText) findViewById(R.id.name);
        time = (Button) findViewById(R.id.time);
        address = (Button) findViewById(R.id.address);
        radius = (TextView) findViewById(R.id.radius);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);

        final MapView mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                NewAppointmentActivity.this.googleMap = googleMap;
                mapView.onResume();
            }
        });
    }

    private float convertProgressToKm(int progress) {
        float[] val = {0.5f, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        return val[progress];
    }

    public void timePickerClick(final View v) {
        // Initialize
        SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Title example",
                "OK",
                "Cancel"
        );

        // Assign values
        dateTimeDialogFragment.startAtCalendarView();
        dateTimeDialogFragment.set24HoursMode(true);

        try {
            dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM",
                    Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e("QC", e.getMessage());
        }

        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                ((Button) v).setText(sdf.format(date));
                v.setTag(date.getTime());
            }

            @Override
            public void onNegativeButtonClick(Date date) {

            }
        });
        dateTimeDialogFragment.show(getSupportFragmentManager(), "dialog_time");
    }

    public void addressClick(View v) {
        try {
            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            address.setText(place.getName());
            address.setTag(place.getLatLng());

            googleMap.clear();
            Marker marker = googleMap.addMarker(new MarkerOptions().position(
                    new LatLng(place.getLatLng().latitude, place.getLatLng().longitude)));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 15f));
        }
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
                String time = this.time.getText().toString();
                String address = this.address.getText().toString();
                LatLng latLng = (LatLng) this.address.getTag();

                if (name.equals("") || time.toLowerCase().equals("set time") || address.toLowerCase().equals("set address")) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                    return true;
                }

                String json = new JSONStringBuilder()
                        .put("groupId", groupID)
                        .put("name", name)
                        .put("lat", latLng.latitude)
                        .put("lon", latLng.longitude)
                        .put("time", this.time.getTag())
                        .put("zone", convertProgressToKm(seekBar.getProgress()) * 1000)
                        .create();
                RestAPI.createAppointment(json, DataUtils.getToken(), new RestAPI.Callback() {
                    @Override
                    public void onDone(JSONObject responseData, boolean hasError) {
                        if (hasError) {
                            Toast.makeText(NewAppointmentActivity.this, "Tạo điểm hẹn thất bại.", Toast.LENGTH_SHORT).show();
                        } else {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                }, true, this);
                break;
        }

        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float r = convertProgressToKm(NewAppointmentActivity.this.seekBar.getProgress());
        radius.setText(String.valueOf(r) + " km");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

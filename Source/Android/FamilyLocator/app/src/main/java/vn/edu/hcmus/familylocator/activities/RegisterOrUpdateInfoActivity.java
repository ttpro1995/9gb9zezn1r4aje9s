package vn.edu.hcmus.familylocator.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.core.JSONStringBuilder;
import vn.edu.hcmus.familylocator.core.RestAPI;
import vn.edu.hcmus.familylocator.utils.DataUtils;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

public class RegisterOrUpdateInfoActivity extends AppCompatActivity {

    private static final String TAG = RegisterOrUpdateInfoActivity.class.getSimpleName();

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private EditText password;
    private Button action;
    private boolean isRegisterMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_or_update_info);

        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.password);
        password.setTransformationMethod(new PasswordTransformationMethod());
        action = (Button) findViewById(R.id.action);

        try {
            isRegisterMode = getIntent().getBooleanExtra("register_mode", true);
        } catch (Exception e) {
            Log.e(TAG, "Không có dữ liệu register_mode");
            finish();
            return;
        }

        ViewUtils.setupActionBar(this, isRegisterMode ? "Đăng Ký" : "Cập Nhật Thông Tin", true, 0);
        action.setText(isRegisterMode ? "Đăng ký" : "Cập nhật");
        if (!isRegisterMode) {
            firstName.setText(DataUtils.getFirstName());
            lastName.setText(DataUtils.getLastName());
            email.setText(DataUtils.getEmail());
            email.setEnabled(false);
            phoneNumber.setText(DataUtils.getPhoneNumber());
            phoneNumber.setEnabled(false);
        }
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

    public void action_click(View v) {
        final String fname = firstName.getText().toString();
        final String lname = lastName.getText().toString();
        final String email = this.email.getText().toString();
        final String phone = phoneNumber.getText().toString();
        final String pwd = password.getText().toString();

        if (fname.equals("") || lname.equals("") || email.equals("") || phone.equals("") || pwd.equals("")) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isRegisterMode) {
            ViewUtils.showProgressDialog(RegisterOrUpdateInfoActivity.this);
            RestAPI.checkExistsUsername(email, new RestAPI.Callback() {
                @Override
                public void onDone(JSONObject responseData, boolean hasError) {
                    if (hasError) {
                        register(fname, lname, email, phone, pwd);
                    } else {
                        Toast.makeText(RegisterOrUpdateInfoActivity.this, "Tài khoản hoặc số điện thoại đã tồn tại.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, false, null);
        } else {
            String json = new JSONStringBuilder()
                    .put("firstName", fname)
                    .put("lastName", lname)
                    .create();
            RestAPI.updateUser(json, DataUtils.getToken(), new RestAPI.Callback() {
                @Override
                public void onDone(JSONObject responseData, boolean hasError) {
                    if (hasError) {
                        Toast.makeText(RegisterOrUpdateInfoActivity.this, "Cập nhật thất bại.", Toast.LENGTH_SHORT).show();
                    } else {
                        DataUtils.setFirstName(fname);
                        DataUtils.setLastName(lname);
                        showSuccessDialog();
                    }
                }
            }, true, RegisterOrUpdateInfoActivity.this);
        }
    }

    private void register(String fname, String lname, String email, String phone, String pwd) {
        String json = new JSONStringBuilder()
                .put("firstName", fname)
                .put("lastName", lname)
                .put("email", email)
                .put("phoneNumber", phone)
                .put("password", pwd)
                .create();
        RestAPI.createUser(json, new RestAPI.Callback() {
            @Override
            public void onDone(JSONObject responseData, boolean hasError) {
                ViewUtils.closeProgressDialog();
                if (hasError) {
                    Toast.makeText(RegisterOrUpdateInfoActivity.this, "Đăng ký thất bại.", Toast.LENGTH_SHORT).show();
                } else {
                    showSuccessDialog();
                }
            }
        }, false, null);
    }

    private void showSuccessDialog() {
        String msg = isRegisterMode ? "Chúc mừng bạn đã đăng ký thành công. Nhấn \"Quay về\" để tiến hành đăng nhập." : "Đã cập nhật thành công. Nhấn \"Quay về\" để tiếp tục sử dụng.";
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("Quay về", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

}

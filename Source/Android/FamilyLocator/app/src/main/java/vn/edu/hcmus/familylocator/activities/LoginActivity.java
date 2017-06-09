package vn.edu.hcmus.familylocator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmus.familylocator.R;
import vn.edu.hcmus.familylocator.core.RestAPI;
import vn.edu.hcmus.familylocator.utils.DataUtils;
import vn.edu.hcmus.familylocator.utils.ViewUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!TextUtils.isEmpty(DataUtils.getToken())) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);
        ViewUtils.setupActionBar(this, "Đăng Nhập", false, 0);

        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());
    }

    public void login_click(View v) {
        final String usn = etUsername.getText().toString();
        final String pwd = etPassword.getText().toString();
        if (usn.equals("") || pwd.equals("")) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        ViewUtils.showProgressDialog(this);
        RestAPI.login(usn, pwd, new RestAPI.Callback() {
            @Override
            public void onDone(JSONObject responseData, boolean hasError) {
                if (hasError) {
                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng.", Toast.LENGTH_SHORT).show();
                    ViewUtils.closeProgressDialog();
                } else {
                    try {
                        JSONObject userData = responseData.getJSONObject("data");
                        long uid = userData.getLong("uid");
                        String token = userData.getString("token");
                        DataUtils.saveLoginInfo(uid, token);
                        getUserInfoAndMoveToMainScreen(uid, token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ViewUtils.closeProgressDialog();
                        Toast.makeText(LoginActivity.this, "Đã có lỗi xảy ra.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, false, null);
    }

    public void register_click(View v) {
        startActivity(new Intent(this, RegisterOrUpdateInfoActivity.class));
    }

    private void getUserInfoAndMoveToMainScreen(long uid, String token) {
        RestAPI.getUser(uid, token, new RestAPI.Callback() {
            @Override
            public void onDone(JSONObject responseData, boolean hasError) {
                ViewUtils.closeProgressDialog();
                try {
                    if (hasError) {
                        Toast.makeText(LoginActivity.this, "Đã có lỗi xảy ra.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        JSONObject userData = responseData.getJSONObject("data");
                        long uid = userData.getLong("id");
                        String firstName = userData.getString("firstName");
                        String lastName = userData.getString("lastName");
                        String email = userData.getString("email");
                        String phoneNumber = userData.getString("phoneNumber");
                        String avatar = userData.getString("avatar");
                        DataUtils.saveUserInfo(uid, firstName, lastName, email, phoneNumber, avatar);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false, null);
    }

}

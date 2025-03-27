package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirmPassword, etMobile, etLocation;
    private Button btnSignup;
    private TextView tvLogin;

    // 🔥 Change this to your actual PHP backend URL
    private static final String SIGNUP_URL = "http://192.168.0.183/api/app/signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etMobile = findViewById(R.id.et_mobile);
        etLocation = findViewById(R.id.et_location);
        btnSignup = findViewById(R.id.btn_signup);
        tvLogin = findViewById(R.id.tv_login);

        btnSignup.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String location = etLocation.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                    || mobile.isEmpty() || location.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(SignupActivity.this, "Enter a valid email!", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 8) {
                Toast.makeText(SignupActivity.this, "Password must be at least 8 characters!", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.PHONE.matcher(mobile).matches() || mobile.length() < 10) {
                Toast.makeText(SignupActivity.this, "Enter a valid mobile number!", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(name, email, password, mobile, location);
            }
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser(String name, String email, String password, String mobile, String location) {
        StringRequest request = new StringRequest(Request.Method.POST, SIGNUP_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if (status.equals("success")) {
                            Toast.makeText(SignupActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            String message = jsonObject.getString("message");
                            Toast.makeText(SignupActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SignupActivity.this, "Signup failed! JSON error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(SignupActivity.this, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("mobile", mobile);
                params.put("location", location);
                return params;
            }
        };

        // Add the request to Volley queue
        RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
        queue.add(request);
    }
}

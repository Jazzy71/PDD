package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etForgotEmail;
    private Button btnResetPassword;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Views
        etForgotEmail = findViewById(R.id.et_forgot_email);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        tvBackToLogin = findViewById(R.id.tv_back_to_login);

        // Handle Reset Password Button Click
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etForgotEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    // Navigate to OTP verification page and pass the email
                    Intent intent = new Intent(ForgotPasswordActivity.this, OtpVerifyActivity.class);
                    intent.putExtra("email", email); // Pass email
                    startActivity(intent);
                    finish(); // Close current activity
                }
            }
        });


        // Handle Back to Login Click
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

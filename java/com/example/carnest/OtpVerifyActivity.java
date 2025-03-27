package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OtpVerifyActivity extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4;
    private Button btnVerify;
    private TextView tvTimer;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        // Initialize views
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        btnVerify = findViewById(R.id.btn_verify);
        tvTimer = findViewById(R.id.tv_timer);
        btnBack = findViewById(R.id.btn_back);

        // Auto move to next OTP field
        setupOtpAutoMove();

        // Countdown Timer (34 seconds)
        new CountDownTimer(34000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("00:" + millisUntilFinished / 1000);
            }
            public void onFinish() {
                tvTimer.setText("Resend OTP");
            }
        }.start();

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Verify OTP
        btnVerify.setOnClickListener(v -> {
            String otpCode = otp1.getText().toString() + otp2.getText().toString() +
                    otp3.getText().toString() + otp4.getText().toString();

            if (otpCode.length() == 4) {
                Toast.makeText(OtpVerifyActivity.this, "OTP Verified!", Toast.LENGTH_SHORT).show();

                // Navigate to ResetPasswordActivity instead of HomeActivity
                Intent intent = new Intent(OtpVerifyActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(OtpVerifyActivity.this, "Invalid OTP!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Function to move cursor automatically to next OTP box
    private void setupOtpAutoMove() {
        otp1.addTextChangedListener(new OtpTextWatcher(otp1, otp2));
        otp2.addTextChangedListener(new OtpTextWatcher(otp2, otp3));
        otp3.addTextChangedListener(new OtpTextWatcher(otp3, otp4));
        otp4.addTextChangedListener(new OtpTextWatcher(otp4, null));
    }

    private class OtpTextWatcher implements TextWatcher {
        private final EditText currentView;
        private final EditText nextView;

        public OtpTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }
}

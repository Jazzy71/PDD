package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText newPasswordInput, confirmPasswordInput;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordInput = findViewById(R.id.et_new_password);
        confirmPasswordInput = findViewById(R.id.et_confirm_password);
        changePasswordButton = findViewById(R.id.btn_change_password);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndChangePassword();
            }
        });
    }

    private void validateAndChangePassword() {
        String newPassword = newPasswordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
        } else if (newPassword.length() < 6) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        } else {
            // TODO: Implement password update logic (e.g., update in Firebase or database)
            Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show();

            // Navigate to Login screen
            startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
            finish();
        }
    }
}

package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to previous activity
            }
        });

        // Initialize account option
        LinearLayout accountLayout = findViewById(R.id.accountLayout);
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AccountActivity
                Intent intent = new Intent(SettingsActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        // Initialize change password option
        LinearLayout changePasswordLayout = findViewById(R.id.changePasswordLayout);
        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AccountActivity
                Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Initialize notification switch
        Switch notificationSwitch = findViewById(R.id.notificationSwitch);
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ? "enabled" : "disabled";
            Toast.makeText(SettingsActivity.this, "Notifications " + status, Toast.LENGTH_SHORT).show();

            // Optional: Save switch status using SharedPreferences
            // SharedPreferences preferences = getSharedPreferences("app_settings", MODE_PRIVATE);
            // preferences.edit().putBoolean("notifications_enabled", isChecked).apply();
        });

        // Initialize help and support option
        LinearLayout helpSupportLayout = findViewById(R.id.helpSupportLayout);
        helpSupportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Help & Support option clicked", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(SettingsActivity.this, HelpSupportActivity.class);
                // startActivity(intent);
            }
        });

        // Initialize logout option
        LinearLayout logoutLayout = findViewById(R.id.logoutLayout);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Logout option clicked", Toast.LENGTH_SHORT).show();
                // Optional: Implement logout confirmation and session clearing
                // logoutUser();
            }
        });
    }
}

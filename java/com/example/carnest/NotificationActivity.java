package com.example.carnest;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout layoutNotificationItem1;
    private LinearLayout layoutNotificationItem2;
    private LinearLayout layoutNotificationItem3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        layoutNotificationItem1 = findViewById(R.id.layoutNotificationItem1);
        layoutNotificationItem2 = findViewById(R.id.layoutNotificationItem2);
        layoutNotificationItem3 = findViewById(R.id.layoutNotificationItem3);

        // Set up click listeners
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Set up notification item click listeners
        setupNotificationItemListeners();
    }

    private void setupNotificationItemListeners() {
        // First notification item
        layoutNotificationItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNotificationClick("Save on your dream car");
            }
        });

        // Second notification item
        layoutNotificationItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNotificationClick("Exclusive offers and discounts");
            }
        });

        // Third notification item
        layoutNotificationItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNotificationClick("Buyer interest notifications");
            }
        });
    }

    private void handleNotificationClick(String notificationType) {
        // Display a toast message when notification is clicked
        Toast.makeText(this, "Enabled: " + notificationType, Toast.LENGTH_SHORT).show();

        // Here you would typically handle the notification preference toggle
        // For example, update the user preferences in SharedPreferences or a database
        saveNotificationPreference(notificationType, true);
    }

    private void saveNotificationPreference(String notificationType, boolean enabled) {
        // Example implementation to save notification preferences
        // In a real app, you would persist this to SharedPreferences or a database
        getSharedPreferences("NotificationPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean(notificationType, enabled)
                .apply();
    }
}
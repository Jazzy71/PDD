package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView tvGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get name from intent
        String name = getIntent().getStringExtra("name");

        // Initialize greeting TextView and set greeting
        tvGreeting = findViewById(R.id.tv_greeting);
        if (name != null && !name.isEmpty()) {
            tvGreeting.setText("Hello, " + name + "!");
        }

        // Initialize buttons
        LinearLayout btnForSale = findViewById(R.id.btn_for_sale);
        LinearLayout btnForBuy = findViewById(R.id.btn_for_buy);
        ImageView btnHelp = findViewById(R.id.iv_help);
        ImageView btnNotification = findViewById(R.id.iv_notification);

        btnForSale.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SellActivity.class);
            startActivity(intent);
        });

        btnForBuy.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, BuyActivity.class);
            startActivity(intent);
        });

        btnHelp.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
            startActivity(intent);
        });

        btnNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        });
    }
}

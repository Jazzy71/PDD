package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BuyProfileActivity extends AppCompatActivity {

    private ImageView backButton;
    private ImageView profileImage;
    private TextView userName;
    private TextView userRole;
    private TextView phoneNumber;
    private TextView emailAddress;
    private LinearLayout phoneContainer;
    private LinearLayout emailContainer;
    private LinearLayout favoritesOption;
    private LinearLayout paymentsOption;
    private LinearLayout tellFriendsOption;
    private LinearLayout settingsOption;
    private LinearLayout logoutOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_profile);

        // Initialize views
        initViews();

        // Set user data
        setUserData();

        // Set click listeners
        setClickListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userRole = findViewById(R.id.userRole);
        phoneNumber = findViewById(R.id.phoneNumber);
        emailAddress = findViewById(R.id.emailAddress);
        phoneContainer = findViewById(R.id.phoneContainer);
        emailContainer = findViewById(R.id.emailContainer);
        favoritesOption = findViewById(R.id.favoritesOption);
        paymentsOption = findViewById(R.id.paymentsOption);
        tellFriendsOption = findViewById(R.id.tellFriendsOption);
        settingsOption = findViewById(R.id.settingsOption);
        logoutOption = findViewById(R.id.logoutOption);
    }

    private void setUserData() {
        // Example user data
        userName.setText("JAZZ");
        userRole.setText("Buyer");
        phoneNumber.setText("+91 89107XXXXX");
        emailAddress.setText("jaz.c78@gmail.com");

        // If using image loading: Glide.with(this).load(url).into(profileImage);
    }

    private void setClickListeners() {
        backButton.setOnClickListener(v -> onBackPressed());

        phoneContainer.setOnClickListener(v ->
                Toast.makeText(this, "Call " + phoneNumber.getText(), Toast.LENGTH_SHORT).show());

        emailContainer.setOnClickListener(v ->
                Toast.makeText(this, "Email " + emailAddress.getText(), Toast.LENGTH_SHORT).show());

        // ✅ Navigate to CarFavoriteActivity
        favoritesOption.setOnClickListener(v -> {
            Intent intent = new Intent(BuyProfileActivity.this, CarFavoriteActivity.class);
            startActivity(intent);
        });

        paymentsOption.setOnClickListener(v ->
                Toast.makeText(this, "Payments clicked", Toast.LENGTH_SHORT).show());

        tellFriendsOption.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app!");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, check out this amazing app!");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        // ✅ Navigate to SettingsActivity
        settingsOption.setOnClickListener(v -> {
            Intent intent = new Intent(BuyProfileActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // ✅ Logout and navigate to LoginActivity
        logoutOption.setOnClickListener(v -> {
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BuyProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears backstack
            startActivity(intent);
            finish();
        });
    }
}

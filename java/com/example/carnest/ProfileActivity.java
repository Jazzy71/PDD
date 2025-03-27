package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    // UI Elements
    private ImageView btnBack;
    private ImageView imgProfile;
    private TextView txtName;
    private TextView txtSellerStatus;
    private TextView txtPhone;
    private TextView txtEmail;

    // Menu options
    private LinearLayout btnMyListings;
    private LinearLayout btnPayments;
    private LinearLayout btnReviews;
    private LinearLayout btnSettings;
    private LinearLayout btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);

        // Initialize UI Elements
        initializeViews();

        // Set up click listeners
        setUpClickListeners();

        // Load user data
        loadUserData();
    }

    private void initializeViews() {
        // Back button
        btnBack = findViewById(R.id.btn_back);

        // Profile section
        imgProfile = findViewById(R.id.img_profile);
        txtName = findViewById(R.id.txt_name);
        txtSellerStatus = findViewById(R.id.txt_seller_status);

        // Contact information
        txtPhone = findViewById(R.id.txt_phone);
        txtEmail = findViewById(R.id.txt_email);

        // Menu options
        btnMyListings = findViewById(R.id.btn_my_listings);
        btnPayments = findViewById(R.id.btn_payments);
        btnReviews = findViewById(R.id.btn_reviews);
        btnSettings = findViewById(R.id.btn_settings);
        btnLogout = findViewById(R.id.btn_logout);
    }

    private void setUpClickListeners() {
        // Back button click listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // My Listings click listener
        btnMyListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMyListings();
            }
        });

        // Payments click listener
        btnPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPayments();
            }
        });

        // Reviews click listener
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToReviews();
            }
        });

        // Settings click listener
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSettings();
            }
        });

        // Logout click listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });
    }

    private void loadUserData() {
        // This would typically be loaded from a database, API, or shared preferences
        // For this example, we're using hardcoded values

        // Set profile image (in a real app, you'd load this from a URL or resource)
        // imgProfile.setImageResource(R.drawable.profile_image);

        // Set user information
        txtName.setText("JAZZ");
        txtSellerStatus.setText("Seller");
        txtPhone.setText("+91 89107XXXXX");
        txtEmail.setText("jaz.c78@gmail.com");
    }

    // Navigation methods
    private void navigateToMyListings() {
        // Example of navigating to another activity
        Intent intent = new Intent(ProfileActivity.this, ListingsActivity.class);
        startActivity(intent);
    }

    private void navigateToPayments() {
        // Example of navigating to another activity
        Intent intent = new Intent(ProfileActivity.this, PaymentsActivity.class);
        startActivity(intent);
    }

    private void navigateToReviews() {
        // Example of navigating to another activity
        Intent intent = new Intent(ProfileActivity.this, ReviewAndRatingActivity.class);
        startActivity(intent);
    }

    private void navigateToSettings() {
        // Example of navigating to another activity
        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void performLogout() {
        // Clear user session data
        // This would typically involve clearing shared preferences, tokens, etc.
        // For this example, we'll just show a toast and navigate back to login

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate to login activity and clear the activity stack
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
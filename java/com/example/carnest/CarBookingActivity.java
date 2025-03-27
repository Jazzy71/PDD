package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CarBookingActivity extends AppCompatActivity {

    private ImageButton btnBack, btnShare, btnFavorite, btnHeartLarge;
    private TextView txtCarName, txtCarTitle, txtCarSpecs, txtCarPrice;
    private Button btnAssured, btnTestDrive, btnBookNow;
    private ImageView imgCarMain, imgThumbnail1, imgThumbnail2, imgThumbnail3;
    private TextView txtPhotoCount;

    private CarModel carData;
    private int currentPhotoIndex = 0;
    private int totalPhotos = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_booking);

        getIntentData();
        initializeUI();
        setupListeners();
        populateUI();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String carTitle = intent.getStringExtra("CAR_TITLE");
            String carSpecs = intent.getStringExtra("CAR_SPECS");
            double price = intent.getDoubleExtra("CAR_PRICE", 0);
            double oldPrice = intent.getDoubleExtra("CAR_OLD_PRICE", 0);

            carData = new CarModel(
                    carTitle != null ? carTitle : "2015 Maruti Wagon",
                    carSpecs != null ? carSpecs : "43,721 km - Petrol",
                    price > 0 ? price : 3.11,
                    oldPrice > 0 ? oldPrice : 3.26,
                    false
            );
        } else {
            carData = new CarModel("2015 Maruti Wagon", "43,721 km - Petrol", 3.11, 3.26, false);
        }
    }

    private void initializeUI() {
        btnBack = findViewById(R.id.btn_back);
        btnShare = findViewById(R.id.btn_share);
        btnFavorite = findViewById(R.id.btn_favorite);
        txtCarName = findViewById(R.id.txt_car_name);
        imgCarMain = findViewById(R.id.img_car_main);
        imgThumbnail1 = findViewById(R.id.img_thumbnail_1);
        imgThumbnail2 = findViewById(R.id.img_thumbnail_2);
        imgThumbnail3 = findViewById(R.id.img_thumbnail_3);
        txtPhotoCount = findViewById(R.id.txt_photo_count);
        txtCarTitle = findViewById(R.id.txt_car_title);
        txtCarSpecs = findViewById(R.id.txt_car_specs);
        txtCarPrice = findViewById(R.id.txt_car_price);
        btnHeartLarge = findViewById(R.id.btn_heart_large);
        btnAssured = findViewById(R.id.btn_assured);
        btnTestDrive = findViewById(R.id.btn_test_drive);
        btnBookNow = findViewById(R.id.btn_book_now);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, carData.getTitle());
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this " + carData.getTitle() +
                    " for ₹" + carData.getPrice() + " Lakh!");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        // ✅ Go to CarFavoriteActivity
        btnFavorite.setOnClickListener(v -> {
            toggleFavorite();
            Intent favIntent = new Intent(CarBookingActivity.this, CarFavoriteActivity.class);
            startActivity(favIntent);
        });

        btnHeartLarge.setOnClickListener(v -> toggleFavorite());

        imgThumbnail1.setOnClickListener(v -> {
            currentPhotoIndex = 0;
            updatePhotoCounter();
        });

        imgThumbnail2.setOnClickListener(v -> {
            currentPhotoIndex = 1;
            updatePhotoCounter();
        });

        imgThumbnail3.setOnClickListener(v -> {
            currentPhotoIndex = 2;
            updatePhotoCounter();
        });

        btnAssured.setOnClickListener(v ->
                Toast.makeText(this, "This car is assured by our service", Toast.LENGTH_SHORT).show()
        );

        btnTestDrive.setOnClickListener(v ->
                Toast.makeText(this, "Test drive scheduled", Toast.LENGTH_SHORT).show()
        );

        btnBookNow.setOnClickListener(v ->
                Toast.makeText(this, "Car booked successfully!", Toast.LENGTH_SHORT).show()
        );

        // Bottom Navigation
        findViewById(R.id.nav_buy).setOnClickListener(v -> showToast("Buy page"));
        findViewById(R.id.nav_favorite).setOnClickListener(v -> showToast("Favorites page"));
        findViewById(R.id.nav_help).setOnClickListener(v -> showToast("Help page"));

        // ✅ Go to HomeActivity
        findViewById(R.id.nav_home).setOnClickListener(v -> {
            Intent homeIntent = new Intent(CarBookingActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        });

        // ✅ Go to ProfileActivity
        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            Intent profileIntent = new Intent(CarBookingActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });
    }

    private void populateUI() {
        String[] nameParts = carData.getTitle().split(" ", 2);
        txtCarName.setText(nameParts.length > 1 ? nameParts[1] : carData.getTitle());
        txtCarTitle.setText(carData.getTitle());
        txtCarSpecs.setText(carData.getDetails());
        txtCarPrice.setText("₹" + carData.getPrice() + " Lakh");

        updateFavoriteIcon();
        updatePhotoCounter();
    }

    private void toggleFavorite() {
        carData.setFavorite(!carData.isFavorite());
        updateFavoriteIcon();
        showToast(carData.isFavorite() ?
                carData.getTitle() + " added to favorites" :
                carData.getTitle() + " removed from favorites");
    }

    private void updateFavoriteIcon() {
        int icon = carData.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.icon_heart;
        btnFavorite.setImageResource(icon);
        btnHeartLarge.setImageResource(icon);
    }

    private void updatePhotoCounter() {
        txtPhotoCount.setText((currentPhotoIndex + 1) + " / " + totalPhotos);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Inner model class
    private static class CarModel {
        private final String title;
        private final String details;
        private final double price;
        private final double oldPrice;
        private boolean favorite;

        public CarModel(String title, String details, double price, double oldPrice, boolean favorite) {
            this.title = title;
            this.details = details;
            this.price = price;
            this.oldPrice = oldPrice;
            this.favorite = favorite;
        }

        public String getTitle() { return title; }
        public String getDetails() { return details; }
        public double getPrice() { return price; }
        public double getOldPrice() { return oldPrice; }
        public boolean isFavorite() { return favorite; }
        public void setFavorite(boolean favorite) { this.favorite = favorite; }
    }
}

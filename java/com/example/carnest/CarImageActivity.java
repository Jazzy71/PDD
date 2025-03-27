package com.example.carnest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CarImageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView carImageView, backButton;
    private Uri selectedImageUri = null;
    private Button chooseImageBtn, continueBtn;

    // Car details from previous activity
    private String carModel, carLocation, carTransmission, kilometers, fuelType, carYear;
    private int brandLogoResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_image);

        // Initialize views
        carImageView = findViewById(R.id.carImageView);
        backButton = findViewById(R.id.backButton);
        chooseImageBtn = findViewById(R.id.chooseImageBtn);
        continueBtn = findViewById(R.id.continueBtn);

        // Get data from previous activity
        Intent intent = getIntent();
        brandLogoResId = intent.getIntExtra("brand_logo", R.drawable.default_car_logo);
        carModel = intent.getStringExtra("car_model");
        carLocation = intent.getStringExtra("selected_location");
        carTransmission = intent.getStringExtra("selected_transmission");
        fuelType = intent.getStringExtra("selected_fuel");
        carYear = intent.getStringExtra("registration_year");
        kilometers = intent.getStringExtra("car_km_range");

        // Back button click
        backButton.setOnClickListener(v -> finish());

        // Choose image button
        chooseImageBtn.setOnClickListener(v -> openImagePicker());

        // Continue button
        continueBtn.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                Intent nextIntent = new Intent(CarImageActivity.this, CarPriceActivity.class);
                nextIntent.putExtra("CAR_MODEL", carModel);
                nextIntent.putExtra("CAR_LOCATION", carLocation);
                nextIntent.putExtra("CAR_TRANSMISSION", carTransmission);
                nextIntent.putExtra("CAR_KILOMETERS", kilometers);
                nextIntent.putExtra("CAR_LOGO", brandLogoResId);
                nextIntent.putExtra("CAR_FUEL", fuelType);
                nextIntent.putExtra("CAR_YEAR", carYear);
                nextIntent.putExtra("CAR_IMAGE_URI", selectedImageUri.toString()); // Pass image URI as String
                startActivity(nextIntent);
            } else {
                Toast.makeText(CarImageActivity.this, "Please select a car image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            carImageView.setImageURI(selectedImageUri);
        }
    }
}

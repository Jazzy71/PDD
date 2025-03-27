package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CarKilometersActivity extends AppCompatActivity {

    private int brandLogoResId;
    private String carModel;
    private String carLocation;
    private String carTransmission;

    private String fuelType;
    private String carYear;
    private String selectedKmRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_kilometers);

        // Get data from previous activity with corrected keys
        Intent intent = getIntent();
        if (intent != null) {
            brandLogoResId = intent.getIntExtra("brand_logo", R.drawable.default_car_logo);
            carModel = intent.getStringExtra("car_model");
            carLocation = intent.getStringExtra("selected_location");
            carTransmission = intent.getStringExtra("selected_transmission");
            fuelType = intent.getStringExtra("selected_fuel");
            carYear = intent.getStringExtra("registration_year");
        }

        // Initialize views
        ImageButton backButton = findViewById(R.id.btn_back);
        ImageButton menuButton = findViewById(R.id.btn_menu);
        TextView modelTextView = findViewById(R.id.tv_model);
        TextView locationTextView = findViewById(R.id.tv_location);
        TextView transmissionTextView = findViewById(R.id.tv_transmission);
        TextView yearTextView = findViewById(R.id.tv_year);
        ImageView logoImageView = findViewById(R.id.img_car_logo);

        // Set car details
        modelTextView.setText(carModel != null ? carModel : "Model");
        locationTextView.setText(carLocation != null ? carLocation : "Location");
        transmissionTextView.setText(carTransmission != null ? carTransmission : "Transmission");
        yearTextView.setText(carYear != null ? carYear : "Year");

        // Set brand logo directly using the passed drawable
        logoImageView.setImageResource(brandLogoResId);

        // Set up kilometer range selection
        setupKilometerRangeSelection();

        // Back button
        backButton.setOnClickListener(v -> finish());

        // Menu button → Profile
        menuButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(CarKilometersActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });
    }

    private void setupKilometerRangeSelection() {
        TextView[] kmOptions = new TextView[7];
        kmOptions[0] = findViewById(R.id.btn_km_option_1);
        kmOptions[1] = findViewById(R.id.btn_km_option_2);
        kmOptions[2] = findViewById(R.id.btn_km_option_3);
        kmOptions[3] = findViewById(R.id.btn_km_option_4);
        kmOptions[4] = findViewById(R.id.btn_km_option_5);
        kmOptions[5] = findViewById(R.id.btn_km_option_6);
        kmOptions[6] = findViewById(R.id.btn_km_option_7);

        for (int i = 0; i < kmOptions.length; i++) {
            final int position = i;
            kmOptions[i].setOnClickListener(v -> {
                for (TextView option : kmOptions) {
                    option.setBackgroundResource(R.drawable.rounded_button);
                }

                kmOptions[position].setBackgroundResource(R.drawable.rounded_button_selected);
                selectedKmRange = kmOptions[position].getText().toString();
                proceedWithSelection();
            });
        }
    }

    private void proceedWithSelection() {
        Intent intent = new Intent(this, CarImageActivity.class);
        intent.putExtra("brand_logo", brandLogoResId);
        intent.putExtra("car_model", carModel);
        intent.putExtra("selected_location", carLocation);
        intent.putExtra("selected_transmission", carTransmission);
        intent.putExtra("selected_fuel", fuelType);
        intent.putExtra("registration_year", carYear);
        intent.putExtra("car_km_range", selectedKmRange);
        startActivity(intent);
    }
}

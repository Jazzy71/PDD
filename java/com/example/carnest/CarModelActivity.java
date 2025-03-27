package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CarModelActivity extends AppCompatActivity {

    private EditText etSearchModel;
    private LinearLayout layoutCreta, layoutVerna, layoutVenue;
    private List<LinearLayout> modelLayouts;
    private List<String> modelNames;

    private String location, transmission, fuelType, registrationYear;
    private int carLogoResId;

    private ImageButton btnBack, btnMenu;
    private TextView tvLocation, tvTransmission, tvYear;
    private ImageView ivCarLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_model);

        // Get data from previous activity
        Intent intent = getIntent();
        if (intent != null) {
            carLogoResId = intent.getIntExtra("brand_logo", R.drawable.default_car_logo);
            location = intent.getStringExtra("selected_location");
            transmission = intent.getStringExtra("selected_transmission");
            fuelType = intent.getStringExtra("selected_fuel");
            registrationYear = intent.getStringExtra("registration_year");
        }

        // Initialize views
        initViews();

        // Display received data
        updateCarDetails();

        // Setup search functionality
        setupSearchFilter();

        // Setup click listeners
        setupClickListeners();
    }

    private void initViews() {
        // Initialize buttons
        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);

        // Initialize text fields and logo
        tvLocation = findViewById(R.id.tv_location);
        tvTransmission = findViewById(R.id.tv_transmission);
        tvYear = findViewById(R.id.tv_year);
        etSearchModel = findViewById(R.id.etSearchModel);
        ivCarLogo = findViewById(R.id.img_car_logo); // Add this ImageView in XML if not present

        // Set logo
        ivCarLogo.setImageResource(carLogoResId);

        // Initialize model layouts
        layoutCreta = findViewById(R.id.layoutCreta);
        layoutVerna = findViewById(R.id.layoutVerna);
        layoutVenue = findViewById(R.id.layoutVenue);

        // Add model layouts to list
        modelLayouts = new ArrayList<>();
        modelLayouts.add(layoutCreta);
        modelLayouts.add(layoutVerna);
        modelLayouts.add(layoutVenue);

        // Add model names to list
        modelNames = new ArrayList<>();
        modelNames.add("Creta");
        modelNames.add("Verna");
        modelNames.add("Venue");
    }

    private void updateCarDetails() {
        if (location != null) {
            tvLocation.setText(location);
        }

        if (transmission != null) {
            tvTransmission.setText(transmission);
        }

        if (registrationYear != null) {
            tvYear.setText(registrationYear);
        }
    }

    private void setupSearchFilter() {
        etSearchModel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterModels(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void filterModels(String query) {
        if (query.isEmpty()) {
            for (LinearLayout layout : modelLayouts) {
                layout.setVisibility(View.VISIBLE);
            }
        } else {
            for (int i = 0; i < modelNames.size(); i++) {
                if (modelNames.get(i).toLowerCase().contains(query)) {
                    modelLayouts.get(i).setVisibility(View.VISIBLE);
                } else {
                    modelLayouts.get(i).setVisibility(View.GONE);
                }
            }
        }
    }

    private void setupClickListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Menu button → Profile
        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(CarModelActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Model selections
        layoutCreta.setOnClickListener(v -> onModelSelected("Creta"));
        layoutVerna.setOnClickListener(v -> onModelSelected("Verna"));
        layoutVenue.setOnClickListener(v -> onModelSelected("Venue"));
    }

    private void onModelSelected(String model) {
        // Highlight selected model
        for (int i = 0; i < modelNames.size(); i++) {
            LinearLayout layout = modelLayouts.get(i);
            if (modelNames.get(i).equals(model)) {
                layout.setBackgroundResource(R.drawable.rounded_button_selected);
            } else {
                layout.setBackgroundResource(R.drawable.rounded_button);
            }
        }

        // Navigate to next activity
        Intent intent = new Intent(this, CarKilometersActivity.class);
        intent.putExtra("brand_logo", carLogoResId);
        intent.putExtra("selected_location", location);
        intent.putExtra("selected_transmission", transmission);
        intent.putExtra("selected_fuel", fuelType);
        intent.putExtra("registration_year", registrationYear);
        intent.putExtra("car_model", model);
        startActivity(intent);
    }
}

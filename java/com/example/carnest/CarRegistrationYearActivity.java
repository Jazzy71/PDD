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

public class CarRegistrationYearActivity extends AppCompatActivity {

    private EditText etSearchYear;
    private LinearLayout layoutYear2023, layoutYear2022, layoutYear2021,
            layoutYear2020, layoutYear2019, layoutYear2018;
    private List<LinearLayout> yearLayouts;
    private List<Integer> years;

    private String location, transmission;
    private int carLogoResId;

    private String fuelType;


    private ImageButton btnBack, btnMenu;
    private ImageView imgBrandLogo;
    private TextView tvLocation, tvTransmission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_registration_year);

        // ✅ Retrieve data from Intent (MATCH keys from CarVariantActivity)
        Intent intent = getIntent();
        if (intent != null) {
            carLogoResId = intent.getIntExtra("brand_logo", R.drawable.default_car_logo);
            location = intent.getStringExtra("selected_location");
            transmission = intent.getStringExtra("selected_transmission");
            fuelType = intent.getStringExtra("selected_fuel");

        }

        // ✅ Initialize views
        initViews();

        // ✅ Setup search + click logic
        setupSearchFilter();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);

        imgBrandLogo = findViewById(R.id.ivCarLogo);
        tvLocation = findViewById(R.id.tvLocation);
        tvTransmission = findViewById(R.id.tvTransmission);
        etSearchYear = findViewById(R.id.etSearchYear);

        // ✅ Set values from intent
        imgBrandLogo.setImageResource(carLogoResId);
        tvLocation.setText(location != null ? location : "Unknown");
        tvTransmission.setText(transmission != null ? transmission : "N/A");

        // ✅ Year layouts
        layoutYear2023 = findViewById(R.id.layoutYear2023);
        layoutYear2022 = findViewById(R.id.layoutYear2022);
        layoutYear2021 = findViewById(R.id.layoutYear2021);
        layoutYear2020 = findViewById(R.id.layoutYear2020);
        layoutYear2019 = findViewById(R.id.layoutYear2019);
        layoutYear2018 = findViewById(R.id.layoutYear2018);

        yearLayouts = new ArrayList<>();
        yearLayouts.add(layoutYear2023);
        yearLayouts.add(layoutYear2022);
        yearLayouts.add(layoutYear2021);
        yearLayouts.add(layoutYear2020);
        yearLayouts.add(layoutYear2019);
        yearLayouts.add(layoutYear2018);

        years = new ArrayList<>();
        years.add(2023);
        years.add(2022);
        years.add(2021);
        years.add(2020);
        years.add(2019);
        years.add(2018);
    }

    private void setupSearchFilter() {
        etSearchYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterYears(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterYears(String query) {
        for (int i = 0; i < years.size(); i++) {
            if (query.isEmpty() || String.valueOf(years.get(i)).contains(query)) {
                yearLayouts.get(i).setVisibility(View.VISIBLE);
            } else {
                yearLayouts.get(i).setVisibility(View.GONE);
            }
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnMenu.setOnClickListener(v -> {
            Intent profileIntent = new Intent(CarRegistrationYearActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });

        for (int i = 0; i < yearLayouts.size(); i++) {
            final int yearIndex = i;
            yearLayouts.get(i).setOnClickListener(v -> onYearSelected(years.get(yearIndex)));
        }
    }

    private void onYearSelected(int year) {
        for (int i = 0; i < years.size(); i++) {
            yearLayouts.get(i).setBackgroundResource(i == years.indexOf(year) ?
                    R.drawable.rounded_button_selected : R.drawable.rounded_button);
        }

        Intent intent = new Intent(this, CarModelActivity.class);
        intent.putExtra("brand_logo", carLogoResId);
        intent.putExtra("selected_location", location);
        intent.putExtra("selected_transmission", transmission);
        intent.putExtra("selected_fuel", fuelType); // ✅ newly added line
        intent.putExtra("registration_year", String.valueOf(year));
        startActivity(intent);

    }
}

package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CarLocationActivity extends AppCompatActivity {

    private EditText etSearchCity;
    private Button btnAgra, btnAllahabad, btnBhopal, btnChandigarh, btnMumbai, btnChennai;
    private ImageButton btnBack, btnMenu;
    private ImageView ivCarLogo;

    private String selectedLocation = "";
    private int selectedLogo = R.drawable.default_car_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_location);

        // Bind views
        etSearchCity = findViewById(R.id.et_search_city);
        btnAgra = findViewById(R.id.btn_agra);
        btnAllahabad = findViewById(R.id.btn_allahabad);
        btnBhopal = findViewById(R.id.btn_bhopal);
        btnChandigarh = findViewById(R.id.btn_chandigarh);
        btnMumbai = findViewById(R.id.btn_mumbai);
        btnChennai = findViewById(R.id.btn_chennai);
        btnBack = findViewById(R.id.btn_back);
        btnMenu = findViewById(R.id.btn_menu);
        ivCarLogo = findViewById(R.id.iv_car_logo);

        // Get brand logo from intent
        int logoFromIntent = getIntent().getIntExtra("brand_logo", -1);
        if (logoFromIntent != -1) {
            selectedLogo = logoFromIntent;
            ivCarLogo.setImageResource(selectedLogo);
        }

        // Navigation
        btnBack.setOnClickListener(v -> finish());
        btnMenu.setOnClickListener(v -> navigateToProfileActivity());

        // Search and Location
        setupSearchFunctionality();
        setupLocationButtons();
    }

    private void setupSearchFunctionality() {
        etSearchCity.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCities(s.toString().toLowerCase());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void filterCities(String searchText) {
        btnAgra.setVisibility(btnAgra.getText().toString().toLowerCase().contains(searchText) ? View.VISIBLE : View.GONE);
        btnAllahabad.setVisibility(btnAllahabad.getText().toString().toLowerCase().contains(searchText) ? View.VISIBLE : View.GONE);
        btnBhopal.setVisibility(btnBhopal.getText().toString().toLowerCase().contains(searchText) ? View.VISIBLE : View.GONE);
        btnChandigarh.setVisibility(btnChandigarh.getText().toString().toLowerCase().contains(searchText) ? View.VISIBLE : View.GONE);
        btnMumbai.setVisibility(btnMumbai.getText().toString().toLowerCase().contains(searchText) ? View.VISIBLE : View.GONE);
        btnChennai.setVisibility(btnChennai.getText().toString().toLowerCase().contains(searchText) ? View.VISIBLE : View.GONE);
    }

    private void setupLocationButtons() {
        updateLocationButtonStyles();

        View.OnClickListener listener = v -> {
            selectedLocation = ((Button) v).getText().toString();
            updateLocationButtonStyles();
            proceedWithSelectedLocation();
        };

        btnAgra.setOnClickListener(listener);
        btnAllahabad.setOnClickListener(listener);
        btnBhopal.setOnClickListener(listener);
        btnChandigarh.setOnClickListener(listener);
        btnMumbai.setOnClickListener(listener);
        btnChennai.setOnClickListener(listener);
    }

    private void updateLocationButtonStyles() {
        btnAgra.setBackgroundResource(R.drawable.rounded_button);
        btnAllahabad.setBackgroundResource(R.drawable.rounded_button);
        btnBhopal.setBackgroundResource(R.drawable.rounded_button);
        btnChandigarh.setBackgroundResource(R.drawable.rounded_button);
        btnMumbai.setBackgroundResource(R.drawable.rounded_button);
        btnChennai.setBackgroundResource(R.drawable.rounded_button);

        switch (selectedLocation) {
            case "Agra":
                btnAgra.setBackgroundResource(R.drawable.rounded_button_selected);
                break;
            case "Allahabad":
                btnAllahabad.setBackgroundResource(R.drawable.rounded_button_selected);
                break;
            case "Bhopal":
                btnBhopal.setBackgroundResource(R.drawable.rounded_button_selected);
                break;
            case "Chandigarh":
                btnChandigarh.setBackgroundResource(R.drawable.rounded_button_selected);
                break;
            case "Mumbai":
                btnMumbai.setBackgroundResource(R.drawable.rounded_button_selected);
                break;
            case "Chennai":
                btnChennai.setBackgroundResource(R.drawable.rounded_button_selected);
                break;
        }
    }

    private void proceedWithSelectedLocation() {
        Toast.makeText(this, "Location selected: " + selectedLocation, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(CarLocationActivity.this, CarVariantActivity.class);
        intent.putExtra("brand_logo", selectedLogo);
        intent.putExtra("selected_location", selectedLocation);
        startActivity(intent);
    }

    private void navigateToProfileActivity() {
        Intent intent = new Intent(CarLocationActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}

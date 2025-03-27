package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CarVariantActivity extends AppCompatActivity {

    private TextView tvSelectedLocation;
    private ImageView ivCarLogo;
    private Button btnAutomatic, btnManual, btnPetrol, btnDiesel, btnElectric;
    private ImageButton btnBack, btnMenu;

    private String selectedTransmission = "";
    private String selectedFuel = "";
    private int selectedLogo = R.drawable.default_car_logo;
    private String selectedLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_variant);

        // Bind views
        tvSelectedLocation = findViewById(R.id.tv_selected_location);
        ivCarLogo = findViewById(R.id.iv_car_logo);
        btnAutomatic = findViewById(R.id.btn_automatic);
        btnManual = findViewById(R.id.btn_manual);
        btnPetrol = findViewById(R.id.btn_petrol);
        btnDiesel = findViewById(R.id.btn_diesel);
        btnElectric = findViewById(R.id.btn_electric);
        btnBack = findViewById(R.id.btn_back);
        btnMenu = findViewById(R.id.btn_menu);

        // Receive data from CarLocationActivity
        Intent intent = getIntent();
        if (intent != null) {
            selectedLogo = intent.getIntExtra("brand_logo", R.drawable.default_car_logo);
            selectedLocation = intent.getStringExtra("selected_location");
        }

        // Set logo and location
        ivCarLogo.setImageResource(selectedLogo);
        tvSelectedLocation.setText(selectedLocation != null ? selectedLocation : "");

        // Back and menu listeners
        btnBack.setOnClickListener(v -> finish());
        btnMenu.setOnClickListener(v -> {
            Intent profileIntent = new Intent(CarVariantActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });

        setupTransmissionButtons();
        setupFuelTypeButtons();
    }

    private void setupTransmissionButtons() {
        updateTransmissionButtonStyles();

        View.OnClickListener listener = v -> {
            selectedTransmission = ((Button) v).getText().toString();
            updateTransmissionButtonStyles();
        };

        btnAutomatic.setOnClickListener(listener);
        btnManual.setOnClickListener(listener);
    }

    private void updateTransmissionButtonStyles() {
        btnAutomatic.setBackgroundResource(R.drawable.rounded_button);
        btnManual.setBackgroundResource(R.drawable.rounded_button);

        if (selectedTransmission.equals(btnAutomatic.getText().toString())) {
            btnAutomatic.setBackgroundResource(R.drawable.rounded_button_selected);
        } else if (selectedTransmission.equals(btnManual.getText().toString())) {
            btnManual.setBackgroundResource(R.drawable.rounded_button_selected);
        }
    }

    private void setupFuelTypeButtons() {
        updateFuelTypeButtonStyles();

        View.OnClickListener listener = v -> {
            selectedFuel = ((Button) v).getText().toString();
            updateFuelTypeButtonStyles();
            proceedWithSelections();
        };

        btnPetrol.setOnClickListener(listener);
        btnDiesel.setOnClickListener(listener);
        btnElectric.setOnClickListener(listener);
    }

    private void updateFuelTypeButtonStyles() {
        btnPetrol.setBackgroundResource(R.drawable.rounded_button);
        btnDiesel.setBackgroundResource(R.drawable.rounded_button);
        btnElectric.setBackgroundResource(R.drawable.rounded_button);

        if (selectedFuel.equals(btnPetrol.getText().toString())) {
            btnPetrol.setBackgroundResource(R.drawable.rounded_button_selected);
        } else if (selectedFuel.equals(btnDiesel.getText().toString())) {
            btnDiesel.setBackgroundResource(R.drawable.rounded_button_selected);
        } else if (selectedFuel.equals(btnElectric.getText().toString())) {
            btnElectric.setBackgroundResource(R.drawable.rounded_button_selected);
        }
    }

    private void proceedWithSelections() {
        Intent intent = new Intent(CarVariantActivity.this, CarRegistrationYearActivity.class);
        intent.putExtra("brand_logo", selectedLogo);
        intent.putExtra("selected_location", selectedLocation);
        intent.putExtra("selected_transmission", selectedTransmission);
        intent.putExtra("selected_fuel", selectedFuel);
        startActivity(intent);
    }
}

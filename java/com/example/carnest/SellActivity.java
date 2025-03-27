package com.example.carnest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SellActivity extends AppCompatActivity {

    private LinearLayout selectedBrand = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_page); // Make sure this matches your XML

        // Find Views
        Button btnSwitchToBuy = findViewById(R.id.btn_switch_to_buy);

        // Car brand selection buttons
        LinearLayout btnHyundai = findViewById(R.id.brand_hyundai);
        LinearLayout btnTata = findViewById(R.id.brand_tata);
        LinearLayout btnHonda = findViewById(R.id.brand_honda);
        LinearLayout btnSuzuki = findViewById(R.id.brand_suzuki);
        LinearLayout btnRenault = findViewById(R.id.brand_renault);
        LinearLayout btnToyota = findViewById(R.id.brand_toyota);

        // Bottom navigation buttons
        LinearLayout navSell = findViewById(R.id.nav_sell);
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navHelp = findViewById(R.id.nav_help);
        LinearLayout navProfile = findViewById(R.id.nav_profile);

        // Switch to Buy Page
        btnSwitchToBuy.setOnClickListener(v -> {
            Intent intent = new Intent(SellActivity.this, BuyActivity.class);
            startActivity(intent);
        });

        View.OnClickListener brandClickListener = v -> {
            String brand = "";
            int logoResId = 0;

            int id = v.getId();

            if (id == R.id.brand_hyundai) {
                brand = "Hyundai";
                logoResId = R.drawable.hyundai_logo;
            } else if (id == R.id.brand_tata) {
                brand = "Tata";
                logoResId = R.drawable.tata_logo;
            } else if (id == R.id.brand_honda) {
                brand = "Honda";
                logoResId = R.drawable.honda_logo;
            } else if (id == R.id.brand_suzuki) {
                brand = "Suzuki";
                logoResId = R.drawable.suzuki_logo;
            } else if (id == R.id.brand_renault) {
                brand = "Renault";
                logoResId = R.drawable.renault_logo;
            } else if (id == R.id.brand_toyota) {
                brand = "Toyota";
                logoResId = R.drawable.toyota_logo;
            }

            selectBrand((LinearLayout) v);
            goToCarLocationActivity(brand, logoResId);
        };


        btnHyundai.setOnClickListener(brandClickListener);
        btnTata.setOnClickListener(brandClickListener);
        btnHonda.setOnClickListener(brandClickListener);
        btnSuzuki.setOnClickListener(brandClickListener);
        btnRenault.setOnClickListener(brandClickListener);
        btnToyota.setOnClickListener(brandClickListener);

        // Bottom Navigation Handling
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(SellActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(SellActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        navHelp.setOnClickListener(v -> {
            Intent intent = new Intent(SellActivity.this, HelpActivity.class);
            startActivity(intent);
        });
    }

    // Highlight selected brand and reset others
    private void selectBrand(LinearLayout newSelectedBrand) {
        if (selectedBrand != null) {
            selectedBrand.setBackgroundColor(Color.TRANSPARENT);
        }
        newSelectedBrand.setBackgroundColor(Color.LTGRAY);
        selectedBrand = newSelectedBrand;
    }

    // Navigate to CarLocationActivity with brand data
    private void goToCarLocationActivity(String brandName, int logoResId) {
        Intent intent = new Intent(SellActivity.this, CarLocationActivity.class);
        intent.putExtra("brand_logo", logoResId);
        startActivity(intent);
    }
}

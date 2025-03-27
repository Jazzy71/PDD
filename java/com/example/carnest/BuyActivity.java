package com.example.carnest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BuyActivity extends AppCompatActivity {

    private ImageView backButton, notificationIcon, profileIcon, searchButton;
    private EditText searchEditText;
    private Button switchToSellButton;
    private LinearLayout hyundaiLayout, tataLayout, hondaLayout, suzukiLayout, renaultLayout, toyotaLayout;
    private LinearLayout carCard1, carCard2;

    // Bottom Navigation Layouts
    private LinearLayout navHome, navFavorite, navHelp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_page);

        // Top Navigation
        backButton = findViewById(R.id.btn_back);
        profileIcon = findViewById(R.id.img_profile);

        // Bottom Navigation
        navHome = findViewById(R.id.btn_home);
        navFavorite = findViewById(R.id.nav_favorite);
        navHelp = findViewById(R.id.nav_help);

        // Search
        searchEditText = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.img_search);

        // Switch to Sell
        switchToSellButton = findViewById(R.id.btn_switch_to_sell);

        // Car Brands Layouts
        hyundaiLayout = findViewById(R.id.brand_hyundai);
        tataLayout = findViewById(R.id.brand_tata);
        hondaLayout = findViewById(R.id.brand_honda);
        suzukiLayout = findViewById(R.id.brand_suzuki);
        renaultLayout = findViewById(R.id.brand_renault);
        toyotaLayout = findViewById(R.id.brand_toyota);

        // Similar Cars
        carCard1 = findViewById(R.id.car_card_1);
        carCard2 = findViewById(R.id.car_card_2);

        // Search functionality
        searchButton.setOnClickListener(view -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                showToast("Searching for: " + query);
            } else {
                showToast("Please enter a search term");
            }
        });

        // Top Nav
        backButton.setOnClickListener(view -> onBackPressed());
        profileIcon.setOnClickListener(view -> {
            Intent intent = new Intent(BuyActivity.this, BuyProfileActivity.class);
            startActivity(intent);
        });

        // Bottom Nav
        navHome.setOnClickListener(view -> startActivity(new Intent(BuyActivity.this, HomeActivity.class)));
        navFavorite.setOnClickListener(view -> startActivity(new Intent(BuyActivity.this, CarFavoriteActivity.class)));
        navHelp.setOnClickListener(view -> startActivity(new Intent(BuyActivity.this, HelpActivity.class)));

        // Switch to Sell Mode
        switchToSellButton.setOnClickListener(view -> {
            Intent intent = new Intent(BuyActivity.this, SellActivity.class);
            startActivity(intent);
        });

        // Car Brand Click Listeners with Logos
        hyundaiLayout.setOnClickListener(view -> openCarSelectingActivity("Hyundai", R.drawable.hyundai_logo));
        tataLayout.setOnClickListener(view -> openCarSelectingActivity("Tata", R.drawable.tata_logo));
        hondaLayout.setOnClickListener(view -> openCarSelectingActivity("Honda", R.drawable.honda_logo));
        suzukiLayout.setOnClickListener(view -> openCarSelectingActivity("Suzuki", R.drawable.suzuki_logo));
        renaultLayout.setOnClickListener(view -> openCarSelectingActivity("Renault", R.drawable.renault_logo));
        toyotaLayout.setOnClickListener(view -> openCarSelectingActivity("Toyota", R.drawable.toyota_logo));

        // Similar Cars
        carCard1.setOnClickListener(view -> openCarSelectingActivity("SimilarCar1", R.drawable.default_car_logo));
        carCard2.setOnClickListener(view -> openCarSelectingActivity("SimilarCar2", R.drawable.default_car_logo));
    }

    private void openCarSelectingActivity(String brandName, int logoResId) {
        Intent intent = new Intent(BuyActivity.this, CarSelectingActivity.class);
        intent.putExtra("selected_brand", brandName);
        intent.putExtra("brand_logo", logoResId);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

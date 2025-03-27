package com.example.carnest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CarFavoriteActivity extends AppCompatActivity implements View.OnClickListener {

    // UI Components
    private ImageView backButton;
    private EditText etSearchCar;

    // Car items
    private List<CarItemView> carItems = new ArrayList<>();

    // Bottom Navigation
    private LinearLayout navBuy, navFavorite, navHelp, navHome, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_car);

        // Initialize views
        initViews();

        // Set up data
        setupCarData();

        // Set up listeners
        setupListeners();

        // Focus and show keyboard when clicking search bar
        handleSearchInput();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        etSearchCar = findViewById(R.id.et_search_car);

        carItems.add(new CarItemView(
                findViewById(R.id.carItem1),
                findViewById(R.id.carImage1),
                findViewById(R.id.favoriteIcon1),
                findViewById(R.id.carTitle1),
                findViewById(R.id.carDetails1),
                findViewById(R.id.carPrice1),
                findViewById(R.id.carOriginalPrice1),
                findViewById(R.id.buyNowButton1)
        ));

        carItems.add(new CarItemView(
                findViewById(R.id.carItem2),
                findViewById(R.id.carImage2),
                findViewById(R.id.favoriteIcon2),
                findViewById(R.id.carTitle2),
                findViewById(R.id.carDetails2),
                findViewById(R.id.carPrice2),
                findViewById(R.id.carOriginalPrice2),
                findViewById(R.id.buyNowButton2)
        ));

        carItems.add(new CarItemView(
                findViewById(R.id.carItem3),
                findViewById(R.id.carImage3),
                findViewById(R.id.favoriteIcon3),
                findViewById(R.id.carTitle3),
                findViewById(R.id.carDetails3),
                findViewById(R.id.carPrice3),
                findViewById(R.id.carOriginalPrice3),
                findViewById(R.id.buyNowButton3)
        ));

        carItems.add(new CarItemView(
                findViewById(R.id.carItem4),
                findViewById(R.id.carImage4),
                findViewById(R.id.favoriteIcon4),
                findViewById(R.id.carTitle4),
                findViewById(R.id.carDetails4),
                findViewById(R.id.carPrice4),
                findViewById(R.id.carOriginalPrice4),
                findViewById(R.id.buyNowButton4)
        ));

        // Bottom navigation
        navBuy = findViewById(R.id.navBuy);
        navFavorite = findViewById(R.id.navFavorite);
        navHelp = findViewById(R.id.navHelp);
        navHome = findViewById(R.id.navHome);
        navProfile = findViewById(R.id.navProfile);
    }

    private void setupCarData() {
        CarData carData = new CarData(
                "2015 Maruti Wagon",
                "43,721 km · Petrol · Manual",
                "₹3.11 Lakh",
                "₹3.26 Lakh",
                R.drawable.car_wagon
        );

        for (CarItemView carItem : carItems) {
            carItem.bind(carData);
        }
    }

    private void setupListeners() {
        backButton.setOnClickListener(this);

        for (CarItemView carItem : carItems) {
            carItem.setListeners(this);
        }

        navBuy.setOnClickListener(this);
        navFavorite.setOnClickListener(this);
        navHelp.setOnClickListener(this);
        navHome.setOnClickListener(this);
        navProfile.setOnClickListener(this);
    }

    private void handleSearchInput() {
        etSearchCar.setOnClickListener(v -> {
            etSearchCar.setCursorVisible(true);
            etSearchCar.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etSearchCar, InputMethodManager.SHOW_IMPLICIT);
        });

        etSearchCar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO: Filter the list of cars based on search text
                // Example: filterCarList(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.backButton) {
            onBackPressed();
            return;
        }

        if (id == R.id.navBuy) {
            navigateTo(BuyActivity.class);
        } else if (id == R.id.navFavorite) {
            // Already on this screen
        } else if (id == R.id.navHelp) {
            navigateTo(HelpActivity.class);
        } else if (id == R.id.navHome) {
            navigateTo(HomeActivity.class);
        } else if (id == R.id.navProfile) {
            navigateTo(BuyProfileActivity.class);
        }

        for (CarItemView carItem : carItems) {
            if (id == carItem.buyNowButton.getId()) {
                Intent intent = new Intent(this, CarBookingActivity.class);
                intent.putExtra("carTitle", carItem.titleView.getText().toString());
                intent.putExtra("carDetails", carItem.detailsView.getText().toString());
                intent.putExtra("carPrice", carItem.priceView.getText().toString());
                intent.putExtra("carOriginalPrice", carItem.originalPriceView.getText().toString());
                intent.putExtra("carImageResId", carItem.carImageView.getTag() != null ?
                        (int) carItem.carImageView.getTag() : R.drawable.car_wagon);
                startActivity(intent);
                return;
            } else if (id == carItem.favoriteIcon.getId()) {
                toggleFavorite(carItem);
                return;
            } else if (id == carItem.containerView.getId()) {
                Toast.makeText(this, "Car details for " + carItem.titleView.getText(), Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void toggleFavorite(CarItemView carItem) {
        carItem.isFavorite = !carItem.isFavorite;
        carItem.favoriteIcon.setImageResource(
                carItem.isFavorite ? R.drawable.ic_heart_filled : R.drawable.icon_heart
        );
        Toast.makeText(this,
                (carItem.isFavorite ? "Added to" : "Removed from") + " favorites",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    // ---------- Helper Inner Class ----------
    private static class CarItemView {
        LinearLayout containerView;
        ImageView carImageView;
        ImageView favoriteIcon;
        TextView titleView;
        TextView detailsView;
        TextView priceView;
        TextView originalPriceView;
        Button buyNowButton;
        boolean isFavorite = true;

        CarItemView(
                LinearLayout containerView,
                ImageView carImageView,
                ImageView favoriteIcon,
                TextView titleView,
                TextView detailsView,
                TextView priceView,
                TextView originalPriceView,
                Button buyNowButton
        ) {
            this.containerView = containerView;
            this.carImageView = carImageView;
            this.favoriteIcon = favoriteIcon;
            this.titleView = titleView;
            this.detailsView = detailsView;
            this.priceView = priceView;
            this.originalPriceView = originalPriceView;
            this.buyNowButton = buyNowButton;
        }

        void bind(CarData data) {
            titleView.setText(data.title);
            detailsView.setText(data.details);
            priceView.setText(data.price);
            originalPriceView.setText(data.originalPrice);
            carImageView.setImageResource(data.imageResId);
            carImageView.setTag(data.imageResId);
        }

        void setListeners(View.OnClickListener listener) {
            containerView.setOnClickListener(listener);
            favoriteIcon.setOnClickListener(listener);
            buyNowButton.setOnClickListener(listener);
        }
    }

    // ---------- Data Class ----------
    private static class CarData {
        String title, details, price, originalPrice;
        int imageResId;

        CarData(String title, String details, String price, String originalPrice, int imageResId) {
            this.title = title;
            this.details = details;
            this.price = price;
            this.originalPrice = originalPrice;
            this.imageResId = imageResId;
        }
    }
}

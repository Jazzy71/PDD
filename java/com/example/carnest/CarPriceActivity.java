package com.example.carnest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CarPriceActivity extends AppCompatActivity {

    private int brandLogoResId;
    private String carModel;
    private String carLocation;
    private String carTransmission;
    private String fuelType;
    private String carYear;
    private String carKmRange;
    private String minPrice;
    private String maxPrice;
    private Uri carImageUri; // ✅ NEW: for car image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_price);

        // Get data from previous activity
        Intent intent = getIntent();
        if (intent != null) {
            brandLogoResId = intent.getIntExtra("brand_logo", R.drawable.default_car_logo);
            carModel = intent.getStringExtra("car_model");
            carLocation = intent.getStringExtra("selected_location");
            carTransmission = intent.getStringExtra("selected_transmission");
            fuelType = intent.getStringExtra("fuel_type");
            carYear = intent.getStringExtra("registration_year");
            carKmRange = intent.getStringExtra("car_km_range");

            // ✅ Get car image URI
            String imageUriString = intent.getStringExtra("car_image_uri");
            if (imageUriString != null) {
                carImageUri = Uri.parse(imageUriString);
            }
        }

        // Initialize views
        ImageButton backButton = findViewById(R.id.btn_back);
        ImageButton menuButton = findViewById(R.id.btn_menu);
        TextView editButton = findViewById(R.id.btn_edit);
        TextView modelTextView = findViewById(R.id.tv_model);
        TextView locationTextView = findViewById(R.id.tv_location);
        TextView transmissionTextView = findViewById(R.id.tv_transmission);
        TextView yearTextView = findViewById(R.id.tv_year);
        TextView kilometersTextView = findViewById(R.id.tv_kilometers);
        TextView priceRangeTextView = findViewById(R.id.tv_price_range);
        TextView bookEvaluationButton = findViewById(R.id.btn_book_evaluation);
        ImageView logoImageView = findViewById(R.id.img_car_logo); // For brand logo
        ImageView carImageView = findViewById(R.id.img_car_image); // ✅ Add this in XML too

        // Set data to views
        modelTextView.setText(carModel != null ? carModel : "Verna");
        locationTextView.setText(carLocation != null ? carLocation : "Unknown");
        transmissionTextView.setText(carTransmission != null ? carTransmission : "Manual");
        yearTextView.setText(carYear != null ? carYear : "1999");

        // Format and set kilometer range
        String formattedKmRange = formatKilometerRange(carKmRange);
        kilometersTextView.setText(formattedKmRange);

        // Set brand logo from drawable
        logoImageView.setImageResource(brandLogoResId);

        // ✅ Set car image if available
        if (carImageUri != null) {
            carImageView.setImageURI(carImageUri);
        }

        // Calculate and display price
        calculateCarPrice();
        priceRangeTextView.setText("\u20B9 " + minPrice + " - \u20B9 " + maxPrice);

        // Button actions
        backButton.setOnClickListener(v -> finish());

        menuButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(CarPriceActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });

        editButton.setOnClickListener(v -> finish());

        bookEvaluationButton.setOnClickListener(v -> {
            Intent bookIntent = new Intent(CarPriceActivity.this, CarListingSuccessActivity.class);
            bookIntent.putExtra("CAR_MODEL", carModel);
            bookIntent.putExtra("CAR_LOCATION", carLocation);
            bookIntent.putExtra("CAR_PRICE_MIN", minPrice);
            bookIntent.putExtra("CAR_PRICE_MAX", maxPrice);
            bookIntent.putExtra("CAR_TRANSMISSION", carTransmission);
            bookIntent.putExtra("CAR_KILOMETERS", formattedKmRange);
            bookIntent.putExtra("CAR_LOGO", brandLogoResId);

            // ✅ Pass image URI forward if needed
            if (carImageUri != null) {
                bookIntent.putExtra("CAR_IMAGE_URI", carImageUri.toString());
            }

            startActivity(bookIntent);
        });
    }

    private String formatKilometerRange(String kmRange) {
        if (kmRange == null) return "10k - 20k km";

        if (kmRange.contains("10,000 km - 20,000")) return "10k - 20k km";
        else if (kmRange.contains("0km - 10,000")) return "0 - 10k km";
        else if (kmRange.contains("20,000km - 30,000")) return "20k - 30k km";
        else if (kmRange.contains("30,000km - 40,000")) return "30k - 40k km";
        else if (kmRange.contains("40,000km - 50,000")) return "40k - 50k km";
        else if (kmRange.contains("50,000km - 60,000")) return "50k - 60k km";
        else if (kmRange.contains("60,000km - 70,000")) return "60k - 70k km";

        return kmRange;
    }

    private void calculateCarPrice() {
        double basePrice = 1500000;
        double yearFactor = 1.0;
        double kmFactor = 1.0;
        double locationFactor = 1.0;
        double transmissionFactor = 1.0;

        if (carYear != null) {
            try {
                int year = Integer.parseInt(carYear);
                int currentYear = 2025;
                int age = currentYear - year;
                yearFactor = Math.max(0.4, 1.0 - (age * 0.06));
            } catch (NumberFormatException ignored) {}
        }

        if (carKmRange != null) {
            if (carKmRange.contains("0km - 10,000")) kmFactor = 1.0;
            else if (carKmRange.contains("10,000 km - 20,000")) kmFactor = 0.95;
            else if (carKmRange.contains("20,000km - 30,000")) kmFactor = 0.9;
            else if (carKmRange.contains("30,000km - 40,000")) kmFactor = 0.85;
            else if (carKmRange.contains("40,000km - 50,000")) kmFactor = 0.8;
            else if (carKmRange.contains("50,000km - 60,000")) kmFactor = 0.75;
            else if (carKmRange.contains("60,000km - 70,000")) kmFactor = 0.7;
        }

        if ("Allahabad".equalsIgnoreCase(carLocation)) locationFactor = 0.95;
        else if ("Delhi".equalsIgnoreCase(carLocation) || "Mumbai".equalsIgnoreCase(carLocation))
            locationFactor = 1.05;

        if ("Manual".equalsIgnoreCase(carTransmission)) transmissionFactor = 0.95;
        else if ("Automatic".equalsIgnoreCase(carTransmission)) transmissionFactor = 1.05;

        double calculatedPrice = basePrice * yearFactor * kmFactor * locationFactor * transmissionFactor;

        double minPriceValue = calculatedPrice * 0.95;
        double maxPriceValue = calculatedPrice * 1.05;

        minPrice = formatIndianCurrency(minPriceValue);
        maxPrice = formatIndianCurrency(maxPriceValue);
    }

    private String formatIndianCurrency(double amount) {
        long roundedAmount = Math.round(amount);
        String amountStr = String.valueOf(roundedAmount);
        StringBuilder formatted = new StringBuilder();

        int len = amountStr.length();
        for (int i = 0; i < len; i++) {
            formatted.append(amountStr.charAt(i));
            int remainingDigits = len - i - 1;
            if (remainingDigits == 3 || (remainingDigits > 3 && (remainingDigits - 3) % 2 == 0)) {
                if (i < len - 1) {
                    formatted.append(",");
                }
            }
        }
        return formatted.toString();
    }
}

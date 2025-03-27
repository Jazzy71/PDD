package com.example.carnest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;

public class CarListingSuccessActivity extends AppCompatActivity {

    private static final long DISPLAY_TIME = 3000;

    private String carModel, carLocation, minPrice, maxPrice, carTransmission, carKilometers;
    private int carLogoResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_lisiting_success);

        // ✅ Get all car details passed from CarPriceActivity
        Intent intent = getIntent();
        carModel = intent.getStringExtra("CAR_MODEL");
        carLocation = intent.getStringExtra("CAR_LOCATION");
        minPrice = intent.getStringExtra("CAR_PRICE_MIN");
        maxPrice = intent.getStringExtra("CAR_PRICE_MAX");
        carTransmission = intent.getStringExtra("CAR_TRANSMISSION");
        carKilometers = intent.getStringExtra("CAR_KILOMETERS");
        carLogoResId = intent.getIntExtra("CAR_LOGO", R.drawable.default_car_logo);

        // ✅ Save car listing to server/database
        saveCarListingData();

        // Auto-navigate to SellActivity after 3 seconds
        new Handler().postDelayed(() -> {
            startActivity(new Intent(CarListingSuccessActivity.this, SellActivity.class));
            finish();
        }, DISPLAY_TIME);
    }

    private void saveCarListingData() {
        // ✅ Create API client
        /*
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // ✅ Prepare the API call
        Call<Void> call = apiInterface.saveCarListing(
                carModel,
                carLocation,
                minPrice,
                maxPrice,
                carTransmission,
                carKilometers,
                carLogoResId
        );

        // ✅ Make the API call
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(CarListingSuccessActivity.this, "Car listing saved successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CarListingSuccessActivity.this, "Failed to save listing: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });**/
    }
}

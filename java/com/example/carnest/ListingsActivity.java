package com.example.carnest;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListingsActivity extends AppCompatActivity {

    // UI Components
    private ImageView backButton;
    private EditText searchEditText;
    private ImageView searchIcon;

    // Car listing components
    private ImageView carMainImage;
    private TextView paginationIndicator;
    private ImageView[] thumbnails = new ImageView[3];
    private TextView carModel;
    private TextView carDetails;
    private TextView carPrice;
    private TextView priceType;
    private TextView statusButton;

    // Data variables
    private List<String> carImageUrls;
    private int currentImageIndex = 0;
    private int totalImages = 5;
    private boolean isCarSold = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        // Initialize UI components
        initializeViews();

        // Load sample data (in a real app, this would come from a database or API)
        setupSampleData();

        // Set up click listeners
        setupClickListeners();

        // Set up text watchers
        setupTextWatchers();
    }

    private void initializeViews() {
        // Header components
        backButton = findViewById(R.id.back_button);

        // Search components
        searchEditText = findViewById(R.id.search_edit_text);
        searchIcon = findViewById(R.id.search_icon);

        // Car listing components
        carMainImage = findViewById(R.id.car_main_image);
        paginationIndicator = findViewById(R.id.pagination_indicator);

        // Thumbnails
        thumbnails[0] = findViewById(R.id.thumbnail_1);
        thumbnails[1] = findViewById(R.id.thumbnail_2);
        thumbnails[2] = findViewById(R.id.thumbnail_3);

        // Car details
        carModel = findViewById(R.id.car_model);
        carDetails = findViewById(R.id.car_details);
        carPrice = findViewById(R.id.car_price);
        priceType = findViewById(R.id.price_type);
        statusButton = findViewById(R.id.status_button);
    }

    private void setupSampleData() {
        // Set up sample car images (in a real app, these would be loaded from a server)
        carImageUrls = new ArrayList<>();
        carImageUrls.add("car_image_1"); // These would be actual URLs or resource IDs
        carImageUrls.add("car_image_2");
        carImageUrls.add("car_image_3");
        carImageUrls.add("car_image_4");
        carImageUrls.add("car_image_5");

        // Update pagination indicator
        updatePaginationIndicator();

        // Set car details
        carModel.setText("2015 Maruti wagon");
        carDetails.setText("43,721 km -petrol");
        carPrice.setText("₹3.11 Lakh");
        priceType.setText("Fixed on road price");

        // Set status
        updateCarStatus();
    }

    private void setupClickListeners() {
        // Back button
        backButton.setOnClickListener(v -> finish());

        // Search icon
        searchIcon.setOnClickListener(v -> performSearch());

        // Car main image (for swiping to next image)
        carMainImage.setOnClickListener(v -> showNextImage());

        // Thumbnails
        for (int i = 0; i < thumbnails.length; i++) {
            final int position = i;
            thumbnails[i].setOnClickListener(v -> selectImage(position));
        }

        // Status button
        statusButton.setOnClickListener(v -> toggleCarStatus());
    }

    private void setupTextWatchers() {
        // Search text watcher
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // In a real app, this would filter the listings
                // For this example, we just show a message
                if (s.length() > 0) {
                    searchIcon.setImageResource(R.drawable.icon_search);
                } else {
                    searchIcon.setImageResource(R.drawable.icon_search);
                }
            }
        });
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            // In a real app, this would perform the search
            // For this example, we just show a message
            Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
        } else if (searchIcon.getTag() != null && searchIcon.getTag().equals("clear")) {
            // Clear the search if the icon is set to "clear"
            searchEditText.setText("");
            searchIcon.setTag(null);
        }
    }

    private void showNextImage() {
        currentImageIndex = (currentImageIndex + 1) % totalImages;
        updatePaginationIndicator();
        // In a real app, you would load the next image here
        // For this example, we just update the indicator
    }

    private void selectImage(int position) {
        // In a real app, this would select and display the image at the given position
        // For this example, we just update the current index and indicator
        if (position < totalImages) {
            currentImageIndex = position;
            updatePaginationIndicator();
            Toast.makeText(this, "Selected image " + (position + 1), Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePaginationIndicator() {
        paginationIndicator.setText((currentImageIndex + 1) + " / " + totalImages);
    }

    private void toggleCarStatus() {
        isCarSold = !isCarSold;
        updateCarStatus();
    }

    private void updateCarStatus() {
        if (isCarSold) {
            statusButton.setText("Sold");
            statusButton.setBackgroundResource(R.drawable.rounded_green_button); // You would need to create this drawable
        } else {
            statusButton.setText("Not sold");
            statusButton.setBackgroundResource(R.drawable.button_gray);
        }
    }

    // In a real app, you would have methods to load images from URLs or resources
    private void loadImageFromUrl(ImageView imageView, String url) {
        // Implementation would depend on the image loading library you use
        // (e.g., Glide, Picasso, etc.)
        // For this example, we'll just set a placeholder
        imageView.setImageResource(R.drawable.car_main_image);
    }
}
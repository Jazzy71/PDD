package com.example.carnest;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewAndRatingActivity extends AppCompatActivity {

    // Declaration of UI elements
    private ImageView backButton;
    private TextView reviewsTitle;
    private TextView averageRatingText;
    private TextView totalReviewsText;
    private ImageView[] ratingStars = new ImageView[5];

    // Rating bars
    private View rating5Bar;
    private View rating4Bar;
    private View rating3Bar;
    private View rating2Bar;
    private View rating1Bar;

    // Review items
    private LinearLayout reviewItem1;
    private LinearLayout reviewItem2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_rating);

        // Initialize UI elements
        initializeViews();

        // Set up rating bar widths based on data
        setupRatingBars();

        // Set click listeners
        setupClickListeners();

        // Populate reviews data (in a real app, this would come from a database or API)
        populateReviewsData();
    }

    private void initializeViews() {
        // Top bar elements
        backButton = findViewById(R.id.back_button);
        reviewsTitle = findViewById(R.id.reviews_title);

        // Rating summary elements
        averageRatingText = findViewById(R.id.average_rating_text);
        totalReviewsText = findViewById(R.id.total_reviews_text);

        // Rating stars
        ratingStars[0] = findViewById(R.id.star1);
        ratingStars[1] = findViewById(R.id.star2);
        ratingStars[2] = findViewById(R.id.star3);
        ratingStars[3] = findViewById(R.id.star4);
        ratingStars[4] = findViewById(R.id.star5);

        // Rating bars
        rating5Bar = findViewById(R.id.rating_5_bar);
        rating4Bar = findViewById(R.id.rating_4_bar);
        rating3Bar = findViewById(R.id.rating_3_bar);
        rating2Bar = findViewById(R.id.rating_2_bar);
        rating1Bar = findViewById(R.id.rating_1_bar);

        // Review items
        reviewItem1 = findViewById(R.id.review_item_1);
        reviewItem2 = findViewById(R.id.review_item_2);
    }

    private void setupRatingBars() {
        // In a real app, these values would come from actual data
        // For this example, we'll use fixed values that match the design

        // Get the total width of the parent to calculate the appropriate width
        LinearLayout ratingBarsContainer = findViewById(R.id.rating_bars_container);
        ratingBarsContainer.post(() -> {
            int totalWidth = ratingBarsContainer.getWidth();
            int barAreaWidth = totalWidth - 80; // Account for star and number width

            // Set widths based on percentage (these should match the image)
            LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) rating5Bar.getLayoutParams();
            params5.width = (int) (barAreaWidth * 0.75);
            rating5Bar.setLayoutParams(params5);

            LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) rating4Bar.getLayoutParams();
            params4.width = (int) (barAreaWidth * 0.60);
            rating4Bar.setLayoutParams(params4);

            LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) rating3Bar.getLayoutParams();
            params3.width = (int) (barAreaWidth * 0.45);
            rating3Bar.setLayoutParams(params3);

            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) rating2Bar.getLayoutParams();
            params2.width = (int) (barAreaWidth * 0.30);
            rating2Bar.setLayoutParams(params2);

            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) rating1Bar.getLayoutParams();
            params1.width = (int) (barAreaWidth * 0.15);
            rating1Bar.setLayoutParams(params1);
        });
    }

    private void setupClickListeners() {
        // Set up back button functionality
        backButton.setOnClickListener(v -> finish());

        // Set up more options menu for reviews
        ImageView reviewer1Menu = findViewById(R.id.reviewer_1_menu);
        reviewer1Menu.setOnClickListener(v -> showReviewOptions(v));

        ImageView reviewer2Menu = findViewById(R.id.reviewer_2_menu);
        reviewer2Menu.setOnClickListener(v -> showReviewOptions(v));
    }

    private void showReviewOptions(View view) {
        // In a real app, this would show a popup menu with options
        // such as report, share, etc.
        // For this example, we'll just print to the log
        System.out.println("Review options menu clicked");
    }

    private void populateReviewsData() {
        // In a real app, this would come from a database or API
        // For this example, the data is already set in the layout XML

        // Just to demonstrate how you might update the reviews dynamically:
        TextView reviewer1Name = findViewById(R.id.reviewer_1_name);
        TextView reviewer1Comment = findViewById(R.id.reviewer_1_comment);
        TextView reviewer1Date = findViewById(R.id.reviewer_1_date);

        TextView reviewer2Name = findViewById(R.id.reviewer_2_name);
        TextView reviewer2Comment = findViewById(R.id.reviewer_2_comment);
        TextView reviewer2Date = findViewById(R.id.reviewer_2_date);

        // In a real app, you would set these from your data source
        reviewer1Name.setText("Alex");
        reviewer1Comment.setText("Had a great experience! The seller was very honest about the car's condition, provided all necessary documents, and even allowed a test drive. Highly recommended!");
        reviewer1Date.setText("Weeks ago");

        reviewer2Name.setText("Neha");
        reviewer2Comment.setText("The car had some minor scratches and issues that weren't mentioned in the listing. The seller was polite but didn't disclose everything upfront. Be sure to check properly before buying.");
        reviewer2Date.setText("Month ago");
    }
}
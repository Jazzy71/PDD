package com.example.carnest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout layoutHelpItem1;
    private LinearLayout layoutHelpItem2;
    private LinearLayout layoutHelpItem4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        layoutHelpItem1 = findViewById(R.id.layoutHelpItem1);
        layoutHelpItem2 = findViewById(R.id.layoutHelpItem2);
        layoutHelpItem4 = findViewById(R.id.layoutHelpItem4);

        // Set up click listeners
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Set up help item click listeners
        setupHelpItemListeners();
    }

    private void setupHelpItemListeners() {
        // FAQ section
        layoutHelpItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFAQSection();
            }
        });

        // Contact support
        layoutHelpItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactSupport();
            }
        });

        // Provide feedback
        layoutHelpItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provideFeedback();
            }
        });
    }

    private void navigateToFAQSection() {
        // Navigate to FAQ activity
        Intent intent = new Intent(this, FAQActivity.class);
        startActivity(intent);
    }

    private void contactSupport() {
        // Open email client with support email address
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:support@carapp.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No email app installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void provideFeedback() {
        // Open feedback form
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
    }

    // These would be actual separate files in a real project
    public static class FAQActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Implementation would go here
        }
    }

    public static class FeedbackActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Implementation would go here
        }
    }
}

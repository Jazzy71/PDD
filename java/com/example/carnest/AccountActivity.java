package com.example.carnest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    private EditText usernameInput, emailInput, mobileInput, locationInput;
    private Button saveButton;
    private ImageView editProfileButton;
    private boolean isEditMode = false;
    private String baseUrl = "http://192.168.0.183/api/app/signup.php"; // ⬅️ Change this to your server URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Initialize UI elements
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        mobileInput = findViewById(R.id.mobileInput);
        locationInput = findViewById(R.id.locationInput);
        saveButton = findViewById(R.id.saveButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        ImageView backButton = findViewById(R.id.backButton);
        LinearLayout deleteAccountLayout = findViewById(R.id.deleteAccountLayout);
        ImageView editImageButton = findViewById(R.id.editImageButton);

        // Load user data from server
        loadUserData();

        backButton.setOnClickListener(v -> finish());

        editProfileButton.setOnClickListener(v -> toggleEditMode());

        saveButton.setOnClickListener(v -> saveUserData());

        editImageButton.setOnClickListener(v ->
                Toast.makeText(AccountActivity.this, "Change profile picture", Toast.LENGTH_SHORT).show()
        );

        deleteAccountLayout.setOnClickListener(v -> showDeleteAccountConfirmation());
    }

    private void loadUserData() {
        String url = baseUrl + "/get_user.php"; // Your endpoint to fetch user info

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("success")) {
                            JSONObject user = json.getJSONObject("user");

                            usernameInput.setText(user.getString("name"));
                            emailInput.setText(user.getString("email"));
                            mobileInput.setText(user.getString("mobile"));
                            locationInput.setText(user.getString("location"));

                            // Disable editing by default
                            toggleEditMode(); // To disable
                            toggleEditMode(); // and stay disabled
                        } else {
                            Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void saveUserData() {
        String mobile = mobileInput.getText().toString().trim();
        String location = locationInput.getText().toString().trim();

        if (mobile.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Mobile and location cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = baseUrl + "/profile.php"; // Your PHP to update profile

        StringRequest request = new StringRequest(Request.Method.PUT, url,
                response -> {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    toggleEditMode(); // Exit edit mode
                },
                error -> Toast.makeText(this, "Update failed: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mobile", mobile);
                map.put("location", location);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void toggleEditMode() {
        isEditMode = !isEditMode;
        mobileInput.setEnabled(isEditMode);
        locationInput.setEnabled(isEditMode);
        saveButton.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
    }

    private void showDeleteAccountConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteAccount())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAccount() {
        // TODO: Call API to delete account if needed
        Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
        finishAffinity();
    }
}

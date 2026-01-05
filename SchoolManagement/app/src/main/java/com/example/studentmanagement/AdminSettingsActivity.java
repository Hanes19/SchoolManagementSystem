package com.example.studentmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

// Use SwitchCompat for better compatibility
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class AdminSettingsActivity extends AppCompatActivity {

    private SessionManager session;
    private DatabaseHelper db;
    private String userId;

    // UI Elements
    private TextView tvName, tvEmail, tvCurrentLanguage;
    private SwitchCompat switch2FA, switchNotifications, switchDarkMode;
    private LinearLayout btnLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_settings);

        // 1. Initialize Helpers
        db = new DatabaseHelper(this);
        session = new SessionManager(this);
        userId = session.getUserId();

        // 2. Initialize Views
        initViews();

        // 3. Load Data & Preferences
        loadUserProfile();
        loadPreferences();

        // 4. Setup Click Listeners
        setupClickListeners();
    }

    private void initViews() {
        // Text Views
        tvName = findViewById(R.id.tv_profile_name);
        tvEmail = findViewById(R.id.tv_profile_email);
        tvCurrentLanguage = findViewById(R.id.tv_current_language);

        // Switches - Find and cast to SwitchCompat
        // We look for the IDs defined in the XML
        switch2FA = findViewById(R.id.switch_2fa);
        switchNotifications = findViewById(R.id.switch_notifications);
        switchDarkMode = findViewById(R.id.switch_dark_mode);

        // Buttons / Layouts
        btnLanguage = findViewById(R.id.btn_language);
    }

    private void loadUserProfile() {
        if (userId == null) return;

        Cursor cursor = db.getUserDetails(userId);
        if (cursor != null && cursor.moveToFirst()) {
            // Populate Name
            int nameIndex = cursor.getColumnIndex("full_name");
            if (nameIndex != -1 && tvName != null) {
                tvName.setText(cursor.getString(nameIndex));
            }

            // Populate Email
            int emailIndex = cursor.getColumnIndex("email");
            if (emailIndex != -1 && tvEmail != null) {
                String email = cursor.getString(emailIndex);
                tvEmail.setText((email != null && !email.isEmpty()) ? email : "No Email Set");
            }

            // Populate 2FA Switch State
            int twoFactorIndex = cursor.getColumnIndex("is_2fa_enabled");
            if (twoFactorIndex != -1 && switch2FA != null) {
                boolean is2FA = cursor.getInt(twoFactorIndex) == 1;
                switch2FA.setOnCheckedChangeListener(null); // Prevent trigger
                switch2FA.setChecked(is2FA);
                setup2FAListener(); // Re-attach
            }
            cursor.close();
        }
    }

    private void loadPreferences() {
        SharedPreferences prefs = getSharedPreferences("SchoolAppSettings", MODE_PRIVATE);

        // Notifications
        if (switchNotifications != null) {
            boolean notifsEnabled = prefs.getBoolean("notifications_enabled", true);
            switchNotifications.setChecked(notifsEnabled);
        }

        // Dark Mode
        if (switchDarkMode != null) {
            boolean darkModeEnabled = prefs.getBoolean("dark_mode_enabled", false);
            switchDarkMode.setChecked(darkModeEnabled);
        }

        // Language
        if (tvCurrentLanguage != null) {
            String lang = prefs.getString("language", "English");
            tvCurrentLanguage.setText(lang);
        }
    }

    private void setupClickListeners() {
        // Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Profile Click
        View cardProfile = findViewById(R.id.card_school_profile);
        if (cardProfile != null) {
            cardProfile.setOnClickListener(v -> startActivity(new Intent(this, AdminSchoolProfileActivity.class)));
        }

        // Change Password
        View btnChangePass = findViewById(R.id.btn_change_password);
        if (btnChangePass != null) {
            btnChangePass.setOnClickListener(v -> startActivity(new Intent(this, ChangePasswordActivity.class)));
        }

        // Support Links
        setupLink(R.id.btn_help, AdminHelpCenterActivity.class);
        setupLink(R.id.btn_privacy, AdminPrivacyPolicyActivity.class);

        // Logout
        View btnLogout = findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                session.logoutUser();
                finish();
            });
        }

        // Switch Listeners
        setup2FAListener();

        if (switchNotifications != null) {
            switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                getSharedPreferences("SchoolAppSettings", MODE_PRIVATE)
                        .edit().putBoolean("notifications_enabled", isChecked).apply();
                Toast.makeText(this, "Notifications " + (isChecked ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
            });
        }

        if (switchDarkMode != null) {
            switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                getSharedPreferences("SchoolAppSettings", MODE_PRIVATE)
                        .edit().putBoolean("dark_mode_enabled", isChecked).apply();

                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            });
        }

        // Language Dialog
        if (btnLanguage != null) {
            btnLanguage.setOnClickListener(v -> showLanguageDialog());
        }
    }

    private void setup2FAListener() {
        if (switch2FA != null) {
            switch2FA.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Ensure the database helper has the method 'updateTwoFactorStatus'
                boolean success = false;
                try {
                    success = db.updateTwoFactorStatus(userId, isChecked);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Database update failed. Check DatabaseHelper.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (success) {
                    Toast.makeText(this, "Two-Factor Auth " + (isChecked ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
                } else {
                    switch2FA.setChecked(!isChecked); // Revert
                    Toast.makeText(this, "Failed to update 2FA settings", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupLink(int id, Class<?> cls) {
        View view = findViewById(id);
        if (view != null) {
            view.setOnClickListener(v -> startActivity(new Intent(this, cls)));
        }
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "Spanish", "French", "Filipino", "Chinese"};

        new AlertDialog.Builder(this)
                .setTitle("Select Language")
                .setItems(languages, (dialog, which) -> {
                    String selectedLang = languages[which];
                    if (tvCurrentLanguage != null) {
                        tvCurrentLanguage.setText(selectedLang);
                    }
                    getSharedPreferences("SchoolAppSettings", MODE_PRIVATE)
                            .edit().putString("language", selectedLang).apply();
                    Toast.makeText(this, "Language set to " + selectedLang, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
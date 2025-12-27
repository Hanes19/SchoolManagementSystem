package com.example.studentmanagement;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminUserAccConfigActivity extends AppCompatActivity {

    // Views
    private Switch switchStrongPass, switch2fa;
    private LinearLayout btnTimeout15, btnTimeout30, btnTimeout60;
    private TextView tvTimeout15, tvTimeout30, tvTimeout60;

    // Data
    private String selectedTimeout = "30"; // Default 30 mins
    private static final String PREFS_NAME = "SchoolSystemConfig";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_acc_config);

        // Header Back Button
        findViewById(R.id.header).setOnClickListener(v -> finish());

        // Initialize Views
        switchStrongPass = findViewById(R.id.switch_password_policy);
        switch2fa = findViewById(R.id.switch_2fa);

        btnTimeout15 = findViewById(R.id.btn_timeout_15);
        btnTimeout30 = findViewById(R.id.btn_timeout_30);
        btnTimeout60 = findViewById(R.id.btn_timeout_60);

        tvTimeout15 = findViewById(R.id.tv_timeout_15);
        tvTimeout30 = findViewById(R.id.tv_timeout_30);
        tvTimeout60 = findViewById(R.id.tv_timeout_60);

        // Load Saved Settings
        loadSettings();

        // Listeners for Timeout Selection
        btnTimeout15.setOnClickListener(v -> updateTimeoutSelection("15"));
        btnTimeout30.setOnClickListener(v -> updateTimeoutSelection("30"));
        btnTimeout60.setOnClickListener(v -> updateTimeoutSelection("60"));

        // Save Button Listener
        findViewById(R.id.btn_save_security).setOnClickListener(v -> saveSettings());
    }

    private void updateTimeoutSelection(String timeout) {
        selectedTimeout = timeout;

        // Reset all to default (Gray)
        tvTimeout15.setTextColor(Color.parseColor("#A3AED0"));
        tvTimeout30.setTextColor(Color.parseColor("#A3AED0"));
        tvTimeout60.setTextColor(Color.parseColor("#A3AED0"));

        // Set Selected to Blue
        if (timeout.equals("15")) {
            tvTimeout15.setTextColor(Color.parseColor("#4318FF"));
        } else if (timeout.equals("30")) {
            tvTimeout30.setTextColor(Color.parseColor("#4318FF"));
        } else {
            tvTimeout60.setTextColor(Color.parseColor("#4318FF"));
        }
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();

        editor.putBoolean("strong_password", switchStrongPass.isChecked());
        editor.putBoolean("enable_2fa", switch2fa.isChecked());
        editor.putString("session_timeout", selectedTimeout);

        editor.apply();

        Toast.makeText(this, "Security Settings Saved Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        boolean strongPass = prefs.getBoolean("strong_password", true);
        boolean twoFactor = prefs.getBoolean("enable_2fa", false);
        selectedTimeout = prefs.getString("session_timeout", "30");

        switchStrongPass.setChecked(strongPass);
        switch2fa.setChecked(twoFactor);

        updateTimeoutSelection(selectedTimeout);
    }
}
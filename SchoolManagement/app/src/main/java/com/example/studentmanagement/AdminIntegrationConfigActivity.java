package com.example.studentmanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminIntegrationConfigActivity extends AppCompatActivity {

    private Switch switchStripe, switchPaypal, switchTwilio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_integration_config);

        // 1. Back Button
        LinearLayout header = findViewById(R.id.header);
        header.setOnClickListener(v -> finish());

        // 2. Initialize Views (Add these IDs to your XML!)
        switchStripe = findViewById(R.id.switch_stripe);
        switchPaypal = findViewById(R.id.switch_paypal);
        switchTwilio = findViewById(R.id.switch_twilio);

        // 3. Load Saved Preferences
        loadPreferences();

        // 4. Set Listeners to Save Changes
        if(switchStripe != null) switchStripe.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("stripe_enabled", isChecked));
        if(switchPaypal != null) switchPaypal.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("paypal_enabled", isChecked));
        if(switchTwilio != null) switchTwilio.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("twilio_enabled", isChecked));
    }

    private void loadPreferences() {
        SharedPreferences prefs = getSharedPreferences("SchoolConfig", MODE_PRIVATE);
        if(switchStripe != null) switchStripe.setChecked(prefs.getBoolean("stripe_enabled", false));
        if(switchPaypal != null) switchPaypal.setChecked(prefs.getBoolean("paypal_enabled", false));
        if(switchTwilio != null) switchTwilio.setChecked(prefs.getBoolean("twilio_enabled", false));
    }

    private void savePreference(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences("SchoolConfig", MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
    }
}
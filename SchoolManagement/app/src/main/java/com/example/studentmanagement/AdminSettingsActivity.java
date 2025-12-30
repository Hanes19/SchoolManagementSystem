package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_settings);

        setupOption(R.id.card_school_profile, AdminSchoolProfileActivity.class);
        setupOption(R.id.card_change_password, ChangePasswordActivity.class);
        setupOption(R.id.card_backup, AdminBackupRestoreActivity.class);

        // System Config / Privacy Policy could be static pages or placeholders
        // setupOption(R.id.card_system_config, AdminSystemConfigActivity.class);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Logout Logic
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            // Clear sessions here
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupOption(int id, Class<?> cls) {
        CardView card = findViewById(id);
        if (card != null) {
            card.setOnClickListener(v -> startActivity(new Intent(this, cls)));
        }
    }
}
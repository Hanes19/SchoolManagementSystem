package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminSystemConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_system_config);

        // 1. Header / Back Button Logic
        LinearLayout header = findViewById(R.id.header);
        header.setOnClickListener(v -> finish());

        // 2. Setup placeholders for the config buttons
        setupClickListeners();
    }

    private void setupClickListeners() {
        // School Profile
        findViewById(R.id.btn_config_school_profile).setOnClickListener(v ->
                Toast.makeText(this, "School Profile Configuration", Toast.LENGTH_SHORT).show());

        // Academic Years
        findViewById(R.id.btn_config_academic_years).setOnClickListener(v ->
                Toast.makeText(this, "Academic Sessions Management", Toast.LENGTH_SHORT).show());

        // Roles & Permissions
        findViewById(R.id.btn_config_roles).setOnClickListener(v ->
                Toast.makeText(this, "Roles & Permissions", Toast.LENGTH_SHORT).show());

        // Login Settings
        findViewById(R.id.btn_config_login).setOnClickListener(v ->
                Toast.makeText(this, "User Account Settings", Toast.LENGTH_SHORT).show());

        // Integrations
        findViewById(R.id.btn_config_integrations).setOnClickListener(v ->
                Toast.makeText(this, "System Integrations", Toast.LENGTH_SHORT).show());

        // Backup
        findViewById(R.id.btn_config_backup).setOnClickListener(v ->
                Toast.makeText(this, "Backup & Restore", Toast.LENGTH_SHORT).show());
    }
}
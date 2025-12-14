package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminSystemConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_system_config);

        LinearLayout header = findViewById(R.id.header);
        header.setOnClickListener(v -> finish());

        setupClickListeners();
    }

    private void setupClickListeners() {
        // School Profile
        findViewById(R.id.btn_config_school_profile).setOnClickListener(v ->
                startActivity(new Intent(this, AdminSchoolProfileActivity.class)));

        // Academic Years
        findViewById(R.id.btn_config_academic_years).setOnClickListener(v ->
                startActivity(new Intent(this, AdminAcademicSessionActivity.class)));

        // Roles & Permissions
        findViewById(R.id.btn_config_roles).setOnClickListener(v ->
                startActivity(new Intent(this, AdminRolesPermissionActivity.class)));

        // Login Settings (Placeholder as no XML was provided for this specific one)
        findViewById(R.id.btn_config_login).setOnClickListener(v ->
                Toast.makeText(this, "User Account Settings", Toast.LENGTH_SHORT).show());

        // Integrations
        findViewById(R.id.btn_config_integrations).setOnClickListener(v ->
                startActivity(new Intent(this, AdminIntegrationConfigActivity.class)));

        // Backup
        findViewById(R.id.btn_config_backup).setOnClickListener(v ->
                startActivity(new Intent(this, AdminBackupRestoreActivity.class)));
    }
}
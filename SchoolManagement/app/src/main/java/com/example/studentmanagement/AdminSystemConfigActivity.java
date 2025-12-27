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
        // 1. School Profile
        findViewById(R.id.btn_config_school_profile).setOnClickListener(v ->
                startActivity(new Intent(this, AdminSchoolProfileActivity.class)));

        // 2. Academic Years
        findViewById(R.id.btn_config_academic_years).setOnClickListener(v ->
                startActivity(new Intent(this, AdminAcademicSessionActivity.class)));

        // 3. Roles & Permissions
        findViewById(R.id.btn_config_roles).setOnClickListener(v ->
                startActivity(new Intent(this, AdminRolesPermissionActivity.class)));

        // 4. User Accounts / Login Settings (CONNECTED)
        findViewById(R.id.btn_config_login).setOnClickListener(v ->
                startActivity(new Intent(this, AdminUserAccConfigActivity.class)));

        // 5. Integrations
        findViewById(R.id.btn_config_integrations).setOnClickListener(v ->
                startActivity(new Intent(this, AdminIntegrationConfigActivity.class)));

        // 6. Backup & Restore
        findViewById(R.id.btn_config_backup).setOnClickListener(v ->
                startActivity(new Intent(this, AdminBackupRestoreActivity.class)));
    }
}
package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        session = new SessionManager(this);

        // Security Check
        if (!session.isLoggedIn() || !"Admin".equals(session.getRole())) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // --- 1. Users & Staff Module Group ---
        // Opens the User Directory we set up earlier
        findViewById(R.id.btn_users_staff).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminUserDirectoryActivity.class));
        });

        // --- 2. Academic Structure Group ---
        // Changed to open Class List directly since bottom_sheet_academic layout does not exist
        findViewById(R.id.btn_classes_sections).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminClassListActivity.class));
        });

        // --- 3. Fees & Billing Group ---
        findViewById(R.id.btn_fees_billing).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminFeesBillingActivity.class));
        });


        findViewById(R.id.btn_system_config).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminSystemConfigActivity.class));
        });
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        // Home button (Already here)
        findViewById(R.id.btn_home).setOnClickListener(v -> { });

        // Calendar
        findViewById(R.id.btn_calendar).setOnClickListener(v ->
                startActivity(new Intent(this, AdminCalendarActivity.class)));

        // Notifications
        findViewById(R.id.btn_bell).setOnClickListener(v ->
                startActivity(new Intent(this, AdminNotificationActivity.class)));

        // Main Menu
        findViewById(R.id.btnMain_menu).setOnClickListener(v ->
                startActivity(new Intent(this, AdminMainMenuActivity.class)));
    }
}
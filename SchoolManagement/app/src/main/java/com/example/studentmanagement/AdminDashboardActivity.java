package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboardActivity extends AppCompatActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard); // Ensure this matches your XML file name

        session = new SessionManager(this);

        if (!session.isLoggedIn() || !session.getRole().equals("Admin")) {
            Toast.makeText(this, "Security Alert: Unauthorized Access!", Toast.LENGTH_LONG).show();
            session.logoutUser();
            finish();
            return;
        }

        // --- NEW BUTTON INITIALIZATION ---

        // 1. Burger Menu Button
        // This will now work because we added btn_burger_menu to the XML


        // 2. Settings Button
        // This will now work because we renamed btnSettings to btn_settings in the XML
        ImageView btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v -> {
            Toast.makeText(AdminDashboardActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminDashboardActivity.this, AdminSettingsActivity.class);
            startActivity(intent);
        });

        // --- END NEW BUTTONS ---

        // 1. Users Module
        CardView btnUsers = findViewById(R.id.btn_users_staff);
        btnUsers.setOnClickListener(v -> {
            Toast.makeText(this, "Opening User Management...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AdminStudentListActivity.class);
            startActivity(intent);
        });

        // 2. Classes & Timetable Module
        CardView btnClasses = findViewById(R.id.btn_classes_sections);
        btnClasses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminClassListActivity.class);
            startActivity(intent);
        });

        CardView btnFees = findViewById(R.id.btn_fees_billing);
        btnFees.setOnClickListener(v -> {
            // UPDATED: Now opens the Fees Activity
            Intent intent = new Intent(AdminDashboardActivity.this, AdminFeesBillingActivity.class);
            startActivity(intent);
        });

        // 4. System Config
        CardView btnConfig = findViewById(R.id.btn_system_config);
        btnConfig.setOnClickListener(v -> {
            Toast.makeText(this, "System logs coming soon", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminDashboardActivity.this, AdminSystemConfigActivity.class);
            startActivity(intent);
        });

        // FAB Add
        CardView fabAdd = findViewById(R.id.fab_add_new);
        fabAdd.setOnClickListener(v -> {
            // Quick action dialog or generic add
        });
    }
}
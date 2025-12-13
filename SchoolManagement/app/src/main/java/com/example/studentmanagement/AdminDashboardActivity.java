package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView; // Import CardView

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

        // --- BUTTON INITIALIZATION ---

        // 1. Users Module
        CardView btnUsers = findViewById(R.id.btn_users_staff);
        btnUsers.setOnClickListener(v -> {
            Toast.makeText(this, "Opening User Management...", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, AdminUserDirectoryActivity.class);
            // startActivity(intent);
        });

        // 2. Classes & Timetable Module
        CardView btnClasses = findViewById(R.id.btn_classes_sections);
        btnClasses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminClassListActivity.class);
            startActivity(intent);
        });

        // 3. Fees Module
        CardView btnFees = findViewById(R.id.btn_fees_billing);
        btnFees.setOnClickListener(v -> {
            Toast.makeText(this, "Fees module coming soon", Toast.LENGTH_SHORT).show();
        });

        // 4. System Config
        CardView btnConfig = findViewById(R.id.btn_system_config);
        btnConfig.setOnClickListener(v -> {
            Toast.makeText(this, "System logs coming soon", Toast.LENGTH_SHORT).show();
        });

        // FAB Add
        CardView fabAdd = findViewById(R.id.fab_add_new);
        fabAdd.setOnClickListener(v -> {
            // Quick action dialog or generic add
        });
    }
}
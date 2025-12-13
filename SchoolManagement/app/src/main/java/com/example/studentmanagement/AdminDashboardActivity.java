package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboardActivity extends AppCompatActivity {

    // UI Components
    CardView btnUsersStaff, btnClasses, btnFees, btnConfig, fabAddNew;
    TextView tvTotalStudents, tvFeesCollected;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. Link to the correct XML layout
        setContentView(R.layout.admin_dashboard);

        session = new SessionManager(this);

        // 2. SECURITY CHECK (The "Gatekeeper")
        if (!session.isLoggedIn() || !"Admin".equals(session.getRole())) {
            Toast.makeText(this, "Security Alert: Unauthorized Access!", Toast.LENGTH_LONG).show();
            session.logoutUser(); // Kick them out
            finish();
            return;
        }

        // 3. Initialize UI Components
        btnUsersStaff = findViewById(R.id.btn_users_staff);
        btnClasses = findViewById(R.id.btn_classes_sections);
        btnFees = findViewById(R.id.btn_fees_billing);
        btnConfig = findViewById(R.id.btn_system_config);
        fabAddNew = findViewById(R.id.fab_add_new);

        tvTotalStudents = findViewById(R.id.text_total_students);
        tvFeesCollected = findViewById(R.id.text_fees_collected);

        // 4. Set Dynamic Data (Placeholders for now)
        // In the future, you will fetch these numbers from DatabaseHelper
        tvTotalStudents.setText("1,250 Students Active");
        tvFeesCollected.setText("$12,400 Fees Collected");

        // 5. Setup Click Listeners


        btnUsersStaff.setOnClickListener(v -> {
            // OLD: Intent intent = new Intent(AdminDashboardActivity.this, AdminRoleActivity.class);

            // NEW: Open Students Directory directly
            Intent intent = new Intent(AdminDashboardActivity.this, AdminStudentListActivity.class);
            startActivity(intent);
        });

        // --- Module 2: Classes & Timetable ---
        btnClasses.setOnClickListener(v -> {
            Toast.makeText(this, "Classes & Timetable: Coming Soon", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, AdminClassActivity.class);
            // startActivity(intent);
        });

        // --- Module 3: Fees & Billing ---
        btnFees.setOnClickListener(v -> {
            Toast.makeText(this, "Fees Module: Coming Soon", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, AdminFeesActivity.class);
            // startActivity(intent);
        });

        // --- Module 4: System Config & Logs ---
        btnConfig.setOnClickListener(v -> {
            // Navigate to System Logs
            Intent intent = new Intent(AdminDashboardActivity.this, SystemLogActivity.class);
            startActivity(intent);
        });

        // --- Floating Action Button (Quick Add) ---
        fabAddNew.setOnClickListener(v -> {
            Toast.makeText(this, "Quick Add Menu Clicked", Toast.LENGTH_SHORT).show();
            // You can show a BottomSheetDialog here later
        });
    }
}
package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StaffDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_dashboard);

        // Navigation
        setupNav(R.id.card_leave, StaffLeaveActivity.class);
        setupNav(R.id.card_payslip, StaffPayslipActivity.class);
        setupNav(R.id.card_fees, StaffFeesCollectionActivity.class);

        // Reuse existing Library Module
        setupNav(R.id.card_library, LibraryDashboardActivity.class);

        // Logout or Profile (Assuming IDs exist)
        // setupNav(R.id.card_profile, StaffProfileActivity.class);
    }

    private void setupNav(int id, Class<?> cls) {
        CardView card = findViewById(id);
        if (card != null) {
            card.setOnClickListener(v -> startActivity(new Intent(this, cls)));
        }
    }
}
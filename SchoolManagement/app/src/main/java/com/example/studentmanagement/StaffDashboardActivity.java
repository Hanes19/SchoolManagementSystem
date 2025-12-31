package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View; // Import generic View
import androidx.appcompat.app.AppCompatActivity;

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
    }

    // Changed CardView to View to support LinearLayouts too
    private void setupNav(int id, Class<?> cls) {
        View view = findViewById(id);
        if (view != null) {
            view.setOnClickListener(v -> startActivity(new Intent(this, cls)));
        }
    }
}
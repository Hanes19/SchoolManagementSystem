package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StaffDashboardActivity extends AppCompatActivity {

    private SessionManager session;
    private DatabaseHelper db;
    private TextView tvName, tvRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_dashboard);

        session = new SessionManager(this);
        db = new DatabaseHelper(this);

        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize Header Views (Ensure you added IDs to your XML as recommended)
        tvName = findViewById(R.id.tv_welcome_name); // Add android:id="@+id/tv_welcome_name" to the name TextView
        tvRole = findViewById(R.id.tv_staff_role);   // Add android:id="@+id/tv_staff_role" to the role TextView

        loadUserData();

        // --- 1. MODULES (GridLayout) ---

        // Library Card
        CardView btnLibrary = findViewById(R.id.card_library);
        if (btnLibrary != null) {
            btnLibrary.setOnClickListener(v ->
                    startActivity(new Intent(this, LibraryDashboardActivity.class)));
            // Assumes LibraryDashboardActivity handles staff access
        }

        // Fees Card
        CardView btnFees = findViewById(R.id.card_fees);
        if (btnFees != null) {
            btnFees.setOnClickListener(v ->
                    startActivity(new Intent(this, StaffFeesCollectionActivity.class)));
        }

        // --- 2. QUICK LINKS (LinearLayout) ---

        // Leave Application
        View btnLeave = findViewById(R.id.card_leave); // Using View because it might be a LinearLayout in XML
        if (btnLeave != null) {
            btnLeave.setOnClickListener(v ->
                    startActivity(new Intent(this, StaffLeaveActivity.class)));
        }

        // Payslips
        View btnPayslip = findViewById(R.id.card_payslip);
        if (btnPayslip != null) {
            btnPayslip.setOnClickListener(v ->
                    startActivity(new Intent(this, StaffPayslipActivity.class)));
        }

        // --- 3. Header Icons ---

        // Notifications
        View btnNotif = findViewById(R.id.btn_notification); // Ensure ID exists in XML header
        if (btnNotif != null) {
            btnNotif.setOnClickListener(v ->
                    startActivity(new Intent(this, StaffNotificationActivity.class)));
        }

        // Profile
        View btnProfile = findViewById(R.id.btn_profile); // Ensure ID exists in XML header
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v ->
                    startActivity(new Intent(this, StaffProfileActivity.class)));
        }
    }

    private void loadUserData() {
        if (tvName != null) {
            String name = db.getUserName(session.getUserId());
            tvName.setText(name != null ? name : "Staff Member");
        }
        if (tvRole != null) {
            tvRole.setText(session.getRole());
        }
    }
}
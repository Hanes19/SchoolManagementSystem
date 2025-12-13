package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StaffDashboardActivity extends AppCompatActivity {

    // UI Components
    TextView tvWelcome, tvRole;
    Button btnLogout;

    // Helpers
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_dashboard); // We will create this XML next

        // 1. Initialize Session
        session = new SessionManager(getApplicationContext());

        // 2. SECURITY CHECK: Is user logged in?
        if (!session.isLoggedIn()) {
            session.logoutUser(); // Redirects to LoginActivity
            finish();
            return;
        }

        // 3. ROLE CHECK: Is user actually Staff?
        // This prevents Students/Teachers from accessing this page if they somehow get the Intent
        String role = session.getRole();
        if (!"Staff".equals(role)) {
            Toast.makeText(this, "Access Denied: You are not Staff", Toast.LENGTH_LONG).show();
            session.logoutUser();
            finish();
            return;
        }

        // 4. Bind UI Views
        tvWelcome = findViewById(R.id.tvWelcome);
        tvRole = findViewById(R.id.tvRole);
        btnLogout = findViewById(R.id.btnLogout);

        // 5. Display Data
        String userId = session.getUserId();
        tvWelcome.setText("Welcome, " + userId);
        tvRole.setText("Current Role: " + role);

        // 6. Logout Functionality
        btnLogout.setOnClickListener(v -> {
            session.logoutUser();
            finish(); // Close this activity
        });
    }
}
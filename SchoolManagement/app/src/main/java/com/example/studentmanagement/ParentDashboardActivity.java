package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ParentDashboardActivity extends AppCompatActivity {

    TextView tvWelcome, tvRole;
    Button btnLogout;
    SessionManager session;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_dashboard);

        // Initialize Session and Database
        session = new SessionManager(getApplicationContext());
        db = new DatabaseHelper(this);

        // Check if logged in
        if (!session.isLoggedIn()) {
            session.logoutUser();
            finish();
            return;
        }

        // Check if role is correct
        String role = session.getRole();
        if (!"Parent".equals(role)) {
            Toast.makeText(this, "Access Denied: You are not a Parent", Toast.LENGTH_LONG).show();
            session.logoutUser();
            finish();
            return;
        }

        // Bind Views
        tvWelcome = findViewById(R.id.tvWelcome);
        tvRole = findViewById(R.id.tvRole);
        btnLogout = findViewById(R.id.btn_logout);

        // Fetch user data
        String userId = session.getUserId();
        String realName = db.getUserName(userId);

        // Set UI Data
        tvWelcome.setText("Welcome, " + realName);
        tvRole.setText("Guardian / Parent");

        // Setup Logout Listener
        btnLogout.setOnClickListener(v -> {
            session.logoutUser();
            finish();
        });
    }
}
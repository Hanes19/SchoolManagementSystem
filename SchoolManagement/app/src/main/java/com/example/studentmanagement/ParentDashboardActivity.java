package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ParentDashboardActivity extends AppCompatActivity {

    // Corrected: btnLogout changed to TextView to match XML type
    TextView tvWelcome, btnLogout;
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
        // Corrected: Uses the actual ID from parent_dashboard.xml (tv_parent_name)
        tvWelcome = findViewById(R.id.tv_parent_name);

        // Corrected: Cast as TextView because it is a TextView in the XML
        btnLogout = findViewById(R.id.btn_logout);

        // Fetch user data
        String userId = session.getUserId();
        String realName = db.getUserName(userId);

        // Set UI Data
        // Note: The "Welcome," text is static in the XML, so we only set the name here.
        tvWelcome.setText(realName);

        // Removed: tvRole.setText(...) because there is no TextView with id 'tvRole' in your XML.

        // Setup Logout Listener
        btnLogout.setOnClickListener(v -> {
            session.logoutUser();
            finish();
        });
    }
}
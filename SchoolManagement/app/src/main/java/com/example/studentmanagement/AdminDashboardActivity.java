package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roles_and_permissions_userdirectory);

        session = new SessionManager(this);

        // --- SECURITY CHECK (The "Gatekeeper") ---
        if (!session.isLoggedIn() || !session.getRole().equals("Admin")) {
            Toast.makeText(this, "Security Alert: Unauthorized Access!", Toast.LENGTH_LONG).show();
            session.logoutUser(); // Kick them out
            finish();
            return;
        }

        // Initialize buttons from your XML
        // Button btnManageStudent = findViewById(R.id.button24);
        // ... add logic to open student lists etc.
    }
}
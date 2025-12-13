package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etId, etPassword;
    Button btnLogin;
    DatabaseHelper db;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_login);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        // --- REMOVED Auto-Login Logic ---
        // The check "if (session.isLoggedIn()) { routeUser(session.getRole()); }" has been removed.
        // This prevents the app from automatically skipping the login screen.

        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // --- REMOVED Register Button Logic Here ---

        btnLogin.setOnClickListener(v -> {
            String userId = etId.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter ID and Password", Toast.LENGTH_SHORT).show();
            } else {
                // Check Credentials
                if (db.checkUser(userId, password)) {
                    String role = db.getUserRole(userId);

                    // Log the action
                    db.logAction(userId, "User Logged In");

                    session.createLoginSession(userId, role);
                    routeUser(role);
                } else {
                    Toast.makeText(this, "Invalid Credentials or Account Inactive", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void routeUser(String role) {
        Intent intent = null;
        if (role == null) return;

        switch (role) {
            case "Admin":
                intent = new Intent(this, AdminDashboardActivity.class);
                break;
            case "Teacher":
                intent = new Intent(this, TeacherDashboardActivity.class);
                break;
            case "Student":
                intent = new Intent(this, StudentDashboardActivity.class);
                break;
            case "Parent":
                intent = new Intent(this, ParentDashboardActivity.class);
                break;
            case "Staff":
                intent = new Intent(this, StaffDashboardActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }
}
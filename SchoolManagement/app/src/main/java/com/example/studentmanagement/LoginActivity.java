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
        setContentView(R.layout.activity_login); // References your XML file

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        // 1. Check if user is ALREADY logged in
        if (session.isLoggedIn()) {
            // Skip login screen and go to dashboard
            routeUser(session.getRole());
        }

        // 2. Bind Views
        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // 3. Login Logic
        btnLogin.setOnClickListener(v -> {
            String userId = etId.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter ID and Password", Toast.LENGTH_SHORT).show();
            } else {
                // Secure check against DB
                if (db.checkUser(userId, password)) {
                    String role = db.getUserRole(userId);

                    // Create Session
                    session.createLoginSession(userId, role);

                    // Navigate
                    routeUser(role);
                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void routeUser(String role) {
        Intent intent = null;

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
                // intent = new Intent(this, ParentDashboardActivity.class);
                break;
            case "Staff":
                // intent = new Intent(this, StaffDashboardActivity.class);
                break;
            default:
                // Fallback for unknown roles
                Toast.makeText(this, "Role not recognized", Toast.LENGTH_SHORT).show();
                return;
        }

        if (intent != null) {
            startActivity(intent);
            finish(); // Prevents user from pressing 'Back' to go to Login
        }
    }
}
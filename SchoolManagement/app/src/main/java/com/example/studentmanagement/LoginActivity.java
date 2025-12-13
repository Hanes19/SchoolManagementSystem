package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // Imported TextView
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etId, etPassword;
    Button btnLogin;
    TextView tvRegister, tvForgotPass; // 1. Added these variables
    DatabaseHelper db;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_login);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Check if already logged in
        if (session.isLoggedIn()) {
            routeUser(session.getRole());
        }

        // Bind Views
        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);     // 2. Connected to XML
        tvForgotPass = findViewById(R.id.tvForgotPass); // 2. Connected to XML

        // Login Listener
        btnLogin.setOnClickListener(v -> {
            String userId = etId.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter ID and Password", Toast.LENGTH_SHORT).show();
            } else {
                // Check Credentials
                if (db.checkUser(userId, password)) {
                    String role = db.getUserRole(userId);

                    // --- LOG THE LOGIN ACTION ---
                    db.logAction(userId, "User Logged In");

                    session.createLoginSession(userId, role);
                    routeUser(role);
                } else {
                    Toast.makeText(this, "Invalid Credentials or Account Inactive", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 3. Register Listener (Placeholder for now)
        tvRegister.setOnClickListener(v -> {
            Toast.makeText(this, "Redirect to Register Activity", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            // startActivity(intent);
        });

        // 4. Forgot Password Listener (Placeholder for now)
        tvForgotPass.setOnClickListener(v -> {
            Toast.makeText(this, "Redirect to Forgot Password Activity", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            // startActivity(intent);
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
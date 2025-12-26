package com.example.studentmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etId, etPassword;
    Button btnLogin;
    TextView tvForgotPass;
    DatabaseHelper db;
    SessionManager session;
    boolean isPasswordVisible = false; // Flag to track password visibility

    @SuppressLint("ClickableViewAccessibility") // Suppress warning for onTouchListener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_login);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Auto-login if session exists
        if (session.isLoggedIn()) {
            routeUser(session.getRole());
        }

        // Bind Views
        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPass = findViewById(R.id.tvForgotPass);

        // -----------------------------------------------------------
        // 1. FORGOT PASSWORD LOGIC
        // -----------------------------------------------------------
        tvForgotPass.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // -----------------------------------------------------------
        // 2. SHOW / HIDE PASSWORD LOGIC
        // -----------------------------------------------------------
        etPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2; // Index for the drawable on the right (end)

            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Check if the touch is within the bounds of the drawable
                if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                    togglePasswordVisibility();
                    return true; // Consume the event
                }
            }
            return false;
        });

        // -----------------------------------------------------------
        // 3. LOGIN BUTTON LOGIC
        // -----------------------------------------------------------
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

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide Password
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            // Optional: Change icon to "eye_off" if you have that drawable
            // etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_icon, 0);
            isPasswordVisible = false;
        } else {
            // Show Password
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            // Optional: Change icon to "eye_on" if you have that drawable
            isPasswordVisible = true;
        }
        // Move cursor to the end of text
        etPassword.setSelection(etPassword.getText().length());
    }

    private void routeUser(String role) {
        Intent intent = null;
        if (role == null) return;

        switch (role) {
            case "Admin":
                intent = new Intent(this, AdminDashboardActivity.class);
                break;
            case "Parent":
                intent = new Intent(this, ParentDashboardActivity.class);
                break;
            // Add other roles (Teacher/Student) here as needed
        }

        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }
}
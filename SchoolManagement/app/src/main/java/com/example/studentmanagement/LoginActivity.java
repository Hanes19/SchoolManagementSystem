package com.example.studentmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etId, etPassword;
    Button btnLogin;
    TextView tvForgotPass;
    DatabaseHelper db;
    SessionManager session;
    boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_login);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Check if user is already logged in
        if (session.isLoggedIn()) {
            routeUser(session.getRole());
        }

        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPass = findViewById(R.id.tvForgotPass);

        tvForgotPass.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        // Toggle Password Visibility
        etPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });

        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String userId = etId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (userId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter ID and Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.checkUser(userId, password)) {
            // Check if 2FA is enabled
            boolean is2FA = false;
            try {
                // is2FA = db.is2FAEnabled(userId);
            } catch (Exception e) { e.printStackTrace(); }

            if (is2FA) {
                show2FADialog(userId);
            } else {
                completeLogin(userId);
            }
        } else {
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void show2FADialog(String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Two-Factor Authentication");
        builder.setMessage("Please enter the OTP sent to your email/device.");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Verify", (dialog, which) -> {
            String otp = input.getText().toString();
            if (otp.equals("1234")) {
                completeLogin(userId);
            } else {
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void completeLogin(String userId) {
        String role = db.getUserRole(userId);
        db.logAction(userId, "Login Successful");
        session.createLoginSession(userId, role);
        routeUser(role);
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPasswordVisible = false;
        } else {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPasswordVisible = true;
        }
        etPassword.setSelection(etPassword.getText().length());
    }

    private void routeUser(String role) {
        Intent intent = null;
        if (role == null) return;
        switch (role) {
            case "Admin": intent = new Intent(this, AdminDashboardActivity.class); break;
            case "Parent": intent = new Intent(this, ParentDashboardActivity.class); break;
            case "Teacher": intent = new Intent(this, TeacherDashboardActivity.class); break;
            case "Student": intent = new Intent(this, StudentDashboardActivity.class); break;
            case "Staff": intent = new Intent(this, StaffDashboardActivity.class); break;
            // --- ADDED LIBRARIAN CASE ---
            case "Librarian": intent = new Intent(this, LibraryDashboardActivity.class); break;
        }
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }
}
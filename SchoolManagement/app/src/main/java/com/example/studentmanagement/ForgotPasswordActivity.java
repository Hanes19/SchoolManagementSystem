package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etFullName, etDob;
    Button btnVerify;
    TextView tvBackToLogin;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_forgot_pass);

        db = new DatabaseHelper(this);

        // Bind Views
        etFullName = findViewById(R.id.etFullName);
        etDob = findViewById(R.id.etDob);
        btnVerify = findViewById(R.id.btnVerify);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // Back to Login Logic
        tvBackToLogin.setOnClickListener(v -> {
            finish(); // Closes this activity and returns to the previous one (Login)
        });

        // Verify Button Logic
        btnVerify.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String dob = etDob.getText().toString().trim();

            if (fullName.isEmpty() || dob.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Implement actual database verification here.
                // Note: Currently, the database table 'users' does not store Date of Birth.
                // You would need to add a 'dob' column to the users table to verify this accurately.

                Toast.makeText(this, "Verification request sent for: " + fullName, Toast.LENGTH_LONG).show();
            }
        });
    }
}
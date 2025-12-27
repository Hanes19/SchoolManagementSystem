package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminSettingsActivity extends AppCompatActivity {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_settings);

        session = new SessionManager(this);

        // 1. HEADER / BACK BUTTON
        LinearLayout header = findViewById(R.id.header);
        header.setOnClickListener(v -> finish());

        // 2. LOG OUT BUTTON
        TextView btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(AdminSettingsActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            session.logoutUser();
            finish();
        });

        // 3. CHANGE PASSWORD
        findViewById(R.id.btn_change_password).setOnClickListener(v -> {
            // Placeholder: You can create a ChangePasswordActivity later if needed
            Toast.makeText(this, "Change Password clicked", Toast.LENGTH_SHORT).show();
        });

        // 4. HELP CENTER (New)
        // Ensure you have created AdminHelpCenterActivity.java
        findViewById(R.id.btn_help).setOnClickListener(v -> {
            Intent intent = new Intent(AdminSettingsActivity.this, AdminHelpCenterActivity.class);
            startActivity(intent);
        });

        // 5. PRIVACY POLICY (New)
        // Ensure you have created AdminPrivacyPolicyActivity.java
        findViewById(R.id.btn_privacy).setOnClickListener(v -> {
            Intent intent = new Intent(AdminSettingsActivity.this, AdminPrivacyPolicyActivity.class);
            startActivity(intent);
        });

        // Optional: 2FA Toggle (Visual only for now)
        findViewById(R.id.btn_2fa).setOnClickListener(v -> {
            Toast.makeText(this, "2FA settings clicked", Toast.LENGTH_SHORT).show();
        });
    }
}
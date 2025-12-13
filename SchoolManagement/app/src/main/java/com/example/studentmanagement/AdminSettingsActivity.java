package com.example.studentmanagement;

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
            // Display message
            Toast.makeText(AdminSettingsActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();

            // SessionManager handles the Intent to LoginActivity internally
            session.logoutUser();

            // Close this activity so it doesn't stay in the background
            finish();
        });

        // 3. Optional: Settings click listeners
        findViewById(R.id.btn_change_password).setOnClickListener(v -> {
            Toast.makeText(this, "Change Password clicked", Toast.LENGTH_SHORT).show();
        });
    }
}
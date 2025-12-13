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
        setContentView(R.layout.admin_settings); // Connects to your XML

        session = new SessionManager(this);

        // 1. HEADER / BACK BUTTON LOGIC
        // Using the header container as the back button trigger for larger touch area
        LinearLayout header = findViewById(R.id.header);
        header.setOnClickListener(v -> finish()); // Closes this activity and goes back

        // 2. LOG OUT BUTTON LOGIC
        TextView btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            session.logoutUser(); // Clear session
            Toast.makeText(AdminSettingsActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();

            // Redirect to Login Activity and clear the back stack
            Intent intent = new Intent(AdminSettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 3. Optional: Add click listeners for other settings here (e.g., Change Password)
        findViewById(R.id.btn_change_password).setOnClickListener(v -> {
            Toast.makeText(this, "Change Password clicked", Toast.LENGTH_SHORT).show();
        });
    }
}
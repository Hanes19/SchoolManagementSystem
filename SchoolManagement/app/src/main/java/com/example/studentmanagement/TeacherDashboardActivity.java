package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_dashboard);

        session = new SessionManager(this);

        // --- SECURITY CHECK ---
        if (!session.isLoggedIn() || !session.getRole().equals("Teacher")) {
            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
            session.logoutUser();
            finish();
            return;
        }

        // Logout Button Logic (ID: button43 from your XML)
        Button btnLogout = findViewById(R.id.button43);
        btnLogout.setOnClickListener(v -> {
            session.logoutUser();
            finish();
        });
    }
}
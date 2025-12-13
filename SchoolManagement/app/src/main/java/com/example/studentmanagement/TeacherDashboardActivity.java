package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

    TextView tvWelcome, tvRole;
    Button btnLogout;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_dashboard); // Connection to XML

        session = new SessionManager(getApplicationContext());

        // 1. Security Check
        if (!session.isLoggedIn()) {
            session.logoutUser();
            finish();
            return;
        }

        // 2. Role Check
        String role = session.getRole();
        if (!"Teacher".equals(role)) {
            Toast.makeText(this, "Access Denied: You are not a Teacher", Toast.LENGTH_LONG).show();
            session.logoutUser();
            finish();
            return;
        }

        tvWelcome = findViewById(R.id.tvWelcome);
        tvRole = findViewById(R.id.tvRole);
        btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText("Hello, " + session.getUserId());
        tvRole.setText("Role: " + role);

        btnLogout.setOnClickListener(v -> {
            session.logoutUser();
            finish();
        });
    }
}
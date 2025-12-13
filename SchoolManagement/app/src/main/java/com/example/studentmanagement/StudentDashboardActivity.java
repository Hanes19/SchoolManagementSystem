package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboardActivity extends AppCompatActivity {

    TextView tvWelcome, tvRole;
    Button btnLogout;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            session.logoutUser();
            finish();
            return;
        }

        String role = session.getRole();
        if (!"Student".equals(role)) {
            Toast.makeText(this, "Access Denied: You are not a Student", Toast.LENGTH_LONG).show();
            session.logoutUser();
            finish();
            return;
        }

        tvWelcome = findViewById(R.id.tvWelcome);
        tvRole = findViewById(R.id.tvRole);
        btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText("Hi, " + session.getUserId());
        tvRole.setText("Student Account");

        btnLogout.setOnClickListener(v -> {
            session.logoutUser();
            finish();
        });
    }
}
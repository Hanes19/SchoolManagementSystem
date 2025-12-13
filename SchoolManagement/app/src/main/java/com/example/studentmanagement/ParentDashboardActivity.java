package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ParentDashboardActivity extends AppCompatActivity {

    TextView tvWelcome, tvRole;
    Button btnLogout;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_dashboard);

        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            session.logoutUser();
            finish();
            return;
        }

        String role = session.getRole();
        if (!"Parent".equals(role)) {
            Toast.makeText(this, "Access Denied: You are not a Parent", Toast.LENGTH_LONG).show();
            session.logoutUser();
            finish();
            return;
        }

        tvWelcome = findViewById(R.id.tvWelcome);
        tvRole = findViewById(R.id.tvRole);
        btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText("Welcome, " + session.getUserId());
        tvRole.setText("Guardian / Parent");

        btnLogout.setOnClickListener(v -> {
            session.logoutUser();
            finish();
        });
    }
}
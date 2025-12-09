package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

    SessionManager session;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_dashboard); // References your XML

        session = new SessionManager(this);

        // Security Check: If user isn't logged in, kick them out
        if (!session.isLoggedIn()) {
            session.logoutUser();
            finish();
        }

        // Bind Logout Button (ID from your XML is button43)
        btnLogout = findViewById(R.id.button43);

        btnLogout.setOnClickListener(v -> {
            // This clears the data and sends them back to LoginActivity
            session.logoutUser();
            finish();
        });
    }
}
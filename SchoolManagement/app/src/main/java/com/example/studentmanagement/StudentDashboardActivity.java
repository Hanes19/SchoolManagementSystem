package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboardActivity extends AppCompatActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);

        session = new SessionManager(this);

        // --- SECURITY CHECK ---
        if (!session.isLoggedIn() || !session.getRole().equals("Student")) {
            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
            session.logoutUser();
            finish();
        }
    }
}
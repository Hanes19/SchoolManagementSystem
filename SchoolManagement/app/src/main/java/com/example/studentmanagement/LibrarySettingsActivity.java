package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LibrarySettingsActivity extends AppCompatActivity {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_settings);

        session = new SessionManager(getApplicationContext());

        Button btnLogout = findViewById(R.id.btn_logout);
        Button btnBack = findViewById(R.id.btn_back);

        btnLogout.setOnClickListener(v -> {
            session.logoutUser(); // Clears session and redirects to LoginActivity
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminAcademicSessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_academic_session); // Make sure this matches your XML name

        // 1. Back Button
        LinearLayout header = findViewById(R.id.header);
        header.setOnClickListener(v -> finish());

        // 2. Add Session FAB
        CardView btnAddSession = findViewById(R.id.btn_add_session);
        btnAddSession.setOnClickListener(v -> {
            // Logic to open a dialog or new activity to add a session
            Toast.makeText(this, "Open Add Session Dialog", Toast.LENGTH_SHORT).show();
        });

        // 3. Load Session Data (Mock implementation)
        loadSessions();
    }

    private void loadSessions() {
        // You would typically fetch this from DatabaseHelper
        // and dynamically inflate views into the LinearLayout container
    }
}
package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        // 1. Initialize Views
        TextView tvName = findViewById(R.id.tv_profile_name);
        TextView tvId = findViewById(R.id.tv_profile_id);
        TextView tvDept = findViewById(R.id.tv_profile_dept);
        TextView tvEmail = findViewById(R.id.tv_profile_email);
        Button btnLogout = findViewById(R.id.btn_logout);
        ImageView btnBack = findViewById(R.id.btn_back);

        // 2. Set Data (Static for now, matching the Dashboard)
        tvName.setText("Mr. Walter White");
        tvId.setText("ID: TCH-2025-088");
        tvDept.setText("Chemistry Department");
        tvEmail.setText("walter.white@school.edu");

        // 3. Logout Logic
        btnLogout.setOnClickListener(v -> {
            // Optional: Clear SharedPreferences or SessionManager here
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(TeacherProfileActivity.this, LoginActivity.class);
            // Clear back stack so user can't go back to profile
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 4. Back Navigation
        btnBack.setOnClickListener(v -> finish());
    }
}
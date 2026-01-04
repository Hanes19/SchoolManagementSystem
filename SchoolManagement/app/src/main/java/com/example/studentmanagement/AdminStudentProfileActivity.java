package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminStudentProfileActivity extends AppCompatActivity {

    private String currentStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_student_profile);

        // --- FIX: Get ID from Intent ---
        if (getIntent().hasExtra("STUDENT_ID")) {
            currentStudentId = getIntent().getStringExtra("STUDENT_ID");
        } else {
            currentStudentId = "stud01"; // Fallback
            Toast.makeText(this, "No Student ID provided, defaulting to stud01", Toast.LENGTH_SHORT).show();
        }

        ImageView btnBack = findViewById(R.id.btn_back_profile);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Find Cards
        CardView cardAttendance = findViewById(R.id.card_attendance);
        CardView cardGpa = findViewById(R.id.card_gpa);
        CardView cardFees = findViewById(R.id.card_fees);
        CardView cardSchedule = findViewById(R.id.card_schedule);

        // --- NAVIGATION ---
        // Pass the dynamic ID to sub-activities

        cardAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentAttendanceActivity.class);
            intent.putExtra("STUDENT_ID", currentStudentId);
            startActivity(intent);
        });

        cardGpa.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentGradesActivity.class);
            intent.putExtra("STUDENT_ID", currentStudentId);
            startActivity(intent);
        });

        cardFees.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentFeesActivity.class);
            intent.putExtra("STUDENT_ID", currentStudentId);
            startActivity(intent);
        });

        cardSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentScheduleActivity.class);
            intent.putExtra("STUDENT_ID", currentStudentId);
            startActivity(intent);
        });
    }
}
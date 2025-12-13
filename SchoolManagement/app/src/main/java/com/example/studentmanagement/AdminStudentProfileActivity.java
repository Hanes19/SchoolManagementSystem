package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminStudentProfileActivity extends AppCompatActivity {

    private String currentStudentId = "stud01"; // Dynamically pass this in a real app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_student_profile);


        ImageView btnBack = findViewById(R.id.btn_back_profile);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Find Cards
        CardView cardAttendance = findViewById(R.id.card_attendance);
        CardView cardGpa = findViewById(R.id.card_gpa);
        CardView cardFees = findViewById(R.id.card_fees);
        CardView cardSchedule = findViewById(R.id.card_schedule);

        // --- NAVIGATION ---

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
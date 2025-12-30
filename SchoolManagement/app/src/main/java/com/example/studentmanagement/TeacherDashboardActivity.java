package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TeacherDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_dashboard);

        // Navigation: Attendance
        CardView btnAttendance = findViewById(R.id.btn_attendance_tool);
        btnAttendance.setOnClickListener(v -> {
            // Intent intent = new Intent(this, TeacherAttendanceActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Attendance Module Coming Soon", Toast.LENGTH_SHORT).show();
        });

        // Navigation: Gradebook
        CardView btnGradebook = findViewById(R.id.btn_gradebook_tool);
        btnGradebook.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherGradebookActivity.class);
            startActivity(intent);
        });

        // Navigation: Messages
        CardView btnMessages = findViewById(R.id.btn_messages_tool);
        btnMessages.setOnClickListener(v -> {
            Toast.makeText(this, "Messages Module Coming Soon", Toast.LENGTH_SHORT).show();
        });
    }
}
package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudentAttendanceActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvPercentage, tvSummary;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_attendance);

        db = new DatabaseHelper(this);
        studentId = getIntent().getStringExtra("STUDENT_ID");

        tvPercentage = findViewById(R.id.tv_attendance_percentage);
        tvSummary = findViewById(R.id.tv_attendance_summary);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish()); // Check ID in XML

        loadAttendanceData();
    }

    private void loadAttendanceData() {
        // Reuse the method we created for Parents
        Cursor cursor = db.getStudentAttendance(studentId);

        int total = 0;
        int present = 0;

        if (cursor.moveToFirst()) {
            do {
                total++;
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                if ("Present".equalsIgnoreCase(status)) present++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (total > 0) {
            int percent = (present * 100) / total;
            tvPercentage.setText(percent + "%");
            tvSummary.setText("You have attended " + present + " out of " + total + " days.");

            if (percent < 75) {
                tvPercentage.setTextColor(Color.RED);
            } else {
                tvPercentage.setTextColor(Color.parseColor("#4CAF50")); // Green
            }
        } else {
            tvPercentage.setText("0%");
            tvSummary.setText("No attendance records found.");
        }
    }
}
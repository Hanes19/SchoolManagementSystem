package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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

        // These IDs now exist in the XML
        tvPercentage = findViewById(R.id.tv_attendance_percentage);
        tvSummary = findViewById(R.id.tv_attendance_summary);

        // Fixed: ID changed from btn_back to btn_back_attendance
        findViewById(R.id.btn_back_attendance).setOnClickListener(v -> finish());

        loadAttendanceData();
    }

    private void loadAttendanceData() {
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
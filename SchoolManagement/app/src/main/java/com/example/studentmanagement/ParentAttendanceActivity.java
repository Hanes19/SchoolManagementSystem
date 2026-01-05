package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ParentAttendanceActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvPercentage, tvStatus;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_attendance_view);

        studentId = getIntent().getStringExtra("STUDENT_ID");
        db = new DatabaseHelper(this);

        tvPercentage = findViewById(R.id.tv_attendance_percentage);
        tvStatus = findViewById(R.id.tv_today_status);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        calculateAndDisplayAttendance();
    }

    private void calculateAndDisplayAttendance() {
        Cursor cursor = db.getStudentAttendance(studentId);
        int totalDays = 0;
        int presentDays = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                totalDays++;
                // Assuming database has a 'status' column with "Present", "Absent", etc.
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                if ("Present".equalsIgnoreCase(status)) {
                    presentDays++;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Calculate Percentage
        if (totalDays > 0) {
            int percentage = (presentDays * 100) / totalDays;
            tvPercentage.setText(percentage + "%");
        } else {
            tvPercentage.setText("N/A"); // No records yet
        }

        tvStatus.setText(presentDays + " Days Present");
    }
}
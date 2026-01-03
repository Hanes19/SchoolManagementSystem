package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class StudentAttendanceActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvPercentage, tvSummary;
    private RecyclerView recyclerView; // <--- Add this
    private String studentId;
    private ArrayList<AttendanceModel> attendanceList; // <--- List to hold data
    private StudentHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_attendance);

        db = new DatabaseHelper(this);
        studentId = getIntent().getStringExtra("STUDENT_ID");
        if (studentId == null) studentId = "stud01";

        tvPercentage = findViewById(R.id.tv_attendance_percentage);
        tvSummary = findViewById(R.id.tv_attendance_summary);

        // 1. Find the RecyclerView
        recyclerView = findViewById(R.id.calendar_recycler_view);

        // 2. Setup Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btn_back_attendance).setOnClickListener(v -> finish());

        loadAttendanceSummary(); // Kept your old logic here
        loadAttendanceList();    // <--- NEW METHOD TO SHOW DATA
    }

    private void loadAttendanceSummary() {
        // ... (Keep your existing percentage calculation logic here) ...
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
            if (percent < 75) tvPercentage.setTextColor(Color.RED);
            else tvPercentage.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            tvPercentage.setText("0%");
            tvSummary.setText("No attendance records found.");
        }
    }

    // --- NEW METHOD ---
    private void loadAttendanceList() {
        attendanceList = new ArrayList<>();
        Cursor cursor = db.getStudentAttendance(studentId);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                int remarkIndex = cursor.getColumnIndex("remarks");
                String remarks = (remarkIndex != -1) ? cursor.getString(remarkIndex) : "";

                // --- CHANGED SECTION ---
                // Use the empty constructor and setters to avoid the error
                AttendanceModel model = new AttendanceModel();
                model.setDate(date);
                model.setStatus(status);
                model.setRemarks(remarks);

                attendanceList.add(model);
                // -----------------------

            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new StudentHistoryAdapter(attendanceList);
        recyclerView.setAdapter(adapter);
    }
}
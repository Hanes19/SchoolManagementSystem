package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class StudentAttendanceActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView recyclerView;
    private StudentHistoryAdapter adapter;
    private ArrayList<AttendanceModel> attendanceList;
    private String studentId;
    private TextView tvSummary, tvPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_attendance);

        db = new DatabaseHelper(this);

        // --- 1. GET STUDENT ID ---
        // Try to get from Intent (if passed from Admin)
        if (getIntent().hasExtra("STUDENT_ID")) {
            studentId = getIntent().getStringExtra("STUDENT_ID");
        } else {
            // FALLBACK: Use a known seeded ID for testing if no intent passed
            // In a real app, use: studentId = sessionManager.getUserId();
            studentId = "stud01";
        }

        // --- 2. SETUP VIEWS ---
        tvSummary = findViewById(R.id.tv_attendance_summary);
        tvPercentage = findViewById(R.id.tv_attendance_percentage);
        recyclerView = findViewById(R.id.calendar_recycler_view);

        ImageView btnBack = findViewById(R.id.btn_back_attendance);
        if(btnBack != null) btnBack.setOnClickListener(v -> finish());

        // --- 3. SETUP RECYCLERVIEW ---
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- 4. LOAD DATA ---
        loadAttendanceData();
    }

    private void loadAttendanceData() {
        attendanceList = new ArrayList<>();

        // Fetch from DB
        Cursor cursor = db.getStudentAttendance(studentId);

        if (cursor != null && cursor.moveToFirst()) {
            // DATA FOUND
            int presentCount = 0;
            int totalCount = 0;

            do {
                // Column indices based on table: att_id, student_id, date, status, class_name, remarks
                String date = cursor.getString(2); // Date is 3rd column
                String status = cursor.getString(3); // Status is 4th column

                attendanceList.add(new AttendanceModel(date, status));

                if ("Present".equalsIgnoreCase(status)) {
                    presentCount++;
                }
                totalCount++;
            } while (cursor.moveToNext());
            cursor.close();

            // Calculate Percentage
            if (totalCount > 0) {
                int percentage = (presentCount * 100) / totalCount;
                tvPercentage.setText(percentage + "%");
                tvSummary.setText(presentCount + " Present out of " + totalCount + " days");
            }

            // Bind to Adapter
            // FIX: Passing 'this' as Context for the Edit button to work
            adapter = new StudentHistoryAdapter(this, attendanceList);
            recyclerView.setAdapter(adapter);

        } else {
            // NO DATA FOUND
            tvPercentage.setText("N/A");
            tvSummary.setText("No attendance records found.");
            Toast.makeText(this, "No records found for ID: " + studentId, Toast.LENGTH_LONG).show();
        }
    }
}
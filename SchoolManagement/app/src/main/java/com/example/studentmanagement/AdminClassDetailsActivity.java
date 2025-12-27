package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminClassDetailsActivity extends AppCompatActivity {

    private TextView tvClassName, tvRoomName, tvTeacherName, tvTotalStudents, tvBoys, tvGirls;
    private LinearLayout containerStudentList;
    private DatabaseHelper db;
    private String classId;
    private String className, teacherName, roomNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_class_details);

        // 1. Initialize DB
        db = new DatabaseHelper(this);

        // 2. Initialize Views
        tvClassName = findViewById(R.id.tv_class_name);
        tvRoomName = findViewById(R.id.tv_room_name);
        tvTeacherName = findViewById(R.id.tv_teacher_name);
        tvTotalStudents = findViewById(R.id.tv_total_students);
        tvBoys = findViewById(R.id.tv_boys_count);
        tvGirls = findViewById(R.id.tv_girls_count);
        containerStudentList = findViewById(R.id.container_student_list);

        ImageView btnBack = findViewById(R.id.btn_back);
        View btnTimetable = findViewById(R.id.btn_view_timetable);
        View btnAttendance = findViewById(R.id.btn_view_attendance);

        // 3. Get Data from Intent (passed from AdminClassListActivity)
        Intent intent = getIntent();
        if (intent != null) {
            classId = intent.getStringExtra("CLASS_ID");
            className = intent.getStringExtra("CLASS_NAME");
            roomNum = intent.getStringExtra("ROOM_NUM");
            teacherName = intent.getStringExtra("TEACHER_NAME");
        }

        // 4. Set Initial Data
        if (className != null) tvClassName.setText(className);
        if (roomNum != null) tvRoomName.setText("Room: " + roomNum);
        if (teacherName != null) tvTeacherName.setText(teacherName);

        // 5. Load Students
        loadClassData();

        // 6. Click Listeners
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        if (btnTimetable != null) {
            btnTimetable.setOnClickListener(v -> {
                Intent tIntent = new Intent(AdminClassDetailsActivity.this, AdminClassTimetableActivity.class);
                tIntent.putExtra("CLASS_ID", classId);
                startActivity(tIntent);
            });
        }

        // Placeholder for attendance logic
        if (btnAttendance != null) {
            btnAttendance.setOnClickListener(v -> {
                Toast.makeText(this, "Attendance details coming soon", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void loadClassData() {
        if (classId == null) return;

        // Fetch students in this class
        Cursor cursor = db.getStudentsByClass(classId);

        containerStudentList.removeAllViews();
        int total = 0;

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                total++;
                String studentName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String studentId = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));

                // Inflate Row
                View row = LayoutInflater.from(this).inflate(R.layout.item_student_row, containerStudentList, false);

                TextView tvName = row.findViewById(R.id.tv_student_name);
                TextView tvId = row.findViewById(R.id.tv_student_id);

                tvName.setText(studentName);
                tvId.setText("ID: " + studentId);

                // Click to view student profile
                row.setOnClickListener(v -> {
                    Intent intent = new Intent(AdminClassDetailsActivity.this, AdminStudentProfileActivity.class);
                    intent.putExtra("STUDENT_ID", studentId);
                    startActivity(intent);
                });

                containerStudentList.addView(row);
            }
            cursor.close();
        } else {
            TextView empty = new TextView(this);
            empty.setText("No students enrolled.");
            empty.setPadding(20,20,20,20);
            containerStudentList.addView(empty);
        }

        // Update Stats
        tvTotalStudents.setText(String.valueOf(total));

        // Note: Since 'gender' column doesn't exist in your current DatabaseHelper,
        // we leave Boys/Girls as '-' or hidden for now.
    }
}
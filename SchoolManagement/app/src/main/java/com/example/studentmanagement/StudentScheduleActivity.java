package com.example.studentmanagement;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudentScheduleActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;
    private TextView tvCurrentDay;
    private String studentId;
    private String currentDay = "Monday";
    private final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_schedule);

        db = new DatabaseHelper(this);
        studentId = getIntent().getStringExtra("STUDENT_ID");

        llList = findViewById(R.id.ll_schedule_list);
        tvCurrentDay = findViewById(R.id.tv_current_day);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Filter Click
        findViewById(R.id.btn_day_filter).setOnClickListener(v -> showDayPicker()); // Ensure this ID exists in layout

        loadSchedule(currentDay);
    }

    private void showDayPicker() {
        new AlertDialog.Builder(this)
                .setTitle("Select Day")
                .setItems(days, (dialog, which) -> {
                    currentDay = days[which];
                    tvCurrentDay.setText(currentDay);
                    loadSchedule(currentDay);
                })
                .show();
    }

    private void loadSchedule(String day) {
        llList.removeAllViews();
        // 1. Get Student's Class
        String className = db.getStudentClass(studentId);

        // 2. Get Schedule for that Class
        Cursor cursor = db.getClassSchedule(className, day);
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String start = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String end = cursor.getString(cursor.getColumnIndexOrThrow("end_time")); // Optional
                String room = cursor.getString(cursor.getColumnIndexOrThrow("room_no"));

                // Reuse item_teacher_schedule_card.xml or create similar
                View view = inflater.inflate(R.layout.item_teacher_schedule_card, llList, false);
                TextView tvTime = view.findViewById(R.id.tv_start_time);
                TextView tvSubject = view.findViewById(R.id.tv_subject);
                TextView tvDetails = view.findViewById(R.id.tv_details);

                tvTime.setText(start);
                tvSubject.setText(subject);
                tvDetails.setText("Room: " + room);

                llList.addView(view);
            } while (cursor.moveToNext());
        } else {
            // Empty State
            TextView empty = new TextView(this);
            empty.setText("No classes for " + day);
            empty.setPadding(30, 30, 30, 30);
            llList.addView(empty);
        }
        cursor.close();
    }
}
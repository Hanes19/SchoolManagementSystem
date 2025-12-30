package com.example.studentmanagement;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherScheduleActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llScheduleContainer;
    private TextView tvCurrentFilter;
    private String currentDay = "Monday";
    private final String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_schedule);

        db = new DatabaseHelper(this);
        llScheduleContainer = findViewById(R.id.ll_schedule_container);
        tvCurrentFilter = findViewById(R.id.tv_current_filter);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Day Filter Logic
        findViewById(R.id.btn_day_filter).setOnClickListener(v -> showDaySelector());

        loadSchedule(currentDay);
    }

    private void showDaySelector() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Day");
        builder.setItems(daysOfWeek, (dialog, which) -> {
            currentDay = daysOfWeek[which];
            tvCurrentFilter.setText(currentDay);
            loadSchedule(currentDay);
        });
        builder.show();
    }

    private void loadSchedule(String day) {
        llScheduleContainer.removeAllViews();
        // Assuming logged in teacher ID is 'teach01' for demo.
        // In real app, get from SessionManager.
        Cursor cursor = db.getTeacherSchedule("teach01", day);

        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                // String endTime = cursor.getString(cursor.getColumnIndexOrThrow("end_time")); // Optional
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String room = cursor.getString(cursor.getColumnIndexOrThrow("room"));
                String grade = cursor.getString(cursor.getColumnIndexOrThrow("grade_level"));
                String section = cursor.getString(cursor.getColumnIndexOrThrow("section_name"));

                View itemView = inflater.inflate(R.layout.item_teacher_schedule_card, llScheduleContainer, false);

                TextView tvTime = itemView.findViewById(R.id.tv_start_time);
                TextView tvAmPm = itemView.findViewById(R.id.tv_am_pm);
                TextView tvSubject = itemView.findViewById(R.id.tv_subject);
                TextView tvDetails = itemView.findViewById(R.id.tv_details);

                // Simple parsing for AM/PM (assuming format HH:mm or HH:mm AM)
                if(startTime.contains(" ")) {
                    String[] parts = startTime.split(" ");
                    tvTime.setText(parts[0]);
                    tvAmPm.setText(parts[1]);
                } else {
                    tvTime.setText(startTime);
                    tvAmPm.setText("");
                }

                tvSubject.setText(subject);
                tvDetails.setText(grade + " - " + section + " • " + room);

                llScheduleContainer.addView(itemView);

            } while (cursor.moveToNext());
        } else {
            // Add Empty State View
            TextView emptyView = new TextView(this);
            emptyView.setText("No classes scheduled for " + day);
            emptyView.setPadding(0, 50, 0, 0);
            emptyView.setGravity(android.view.Gravity.CENTER);
            llScheduleContainer.addView(emptyView);
        }
        cursor.close();
    }
}
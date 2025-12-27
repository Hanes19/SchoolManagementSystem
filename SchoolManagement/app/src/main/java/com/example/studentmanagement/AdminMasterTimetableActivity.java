package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminMasterTimetableActivity extends AppCompatActivity {

    // Views
    CardView btnFilterDay, btnFilterGrade, btnFilterTeacher;
    TextView tvFilterDay, tvFilterGrade, tvFilterTeacher;
    LinearLayout containerTimetable;

    // Data
    DatabaseHelper db;
    String selectedDay = "Monday";
    String selectedGrade = "All Grades";
    String selectedTeacher = "All Teachers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_master_timetable);

        db = new DatabaseHelper(this);

        btnFilterDay = findViewById(R.id.filter_day);
        btnFilterGrade = findViewById(R.id.filter_grade);
        btnFilterTeacher = findViewById(R.id.filter_teacher);
        tvFilterDay = findViewById(R.id.tv_filter_day);
        tvFilterGrade = findViewById(R.id.tv_filter_grade);
        tvFilterTeacher = findViewById(R.id.tv_filter_teacher);

        // Ensure you added this ID to your XML ScrollView -> LinearLayout
        containerTimetable = findViewById(R.id.container_timetable);

        findViewById(R.id.header).setOnClickListener(v -> finish());

        btnFilterDay.setOnClickListener(this::showDayPopup);
        btnFilterGrade.setOnClickListener(this::showGradePopup);
        btnFilterTeacher.setOnClickListener(this::showTeacherPopup);

        loadTimetableData();
    }

    private void showDayPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String day : days) popup.getMenu().add(day);

        popup.setOnMenuItemClickListener(item -> {
            selectedDay = item.getTitle().toString();
            tvFilterDay.setText(selectedDay);
            loadTimetableData();
            return true;
        });
        popup.show();
    }

    private void showGradePopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add("All Grades");

        Cursor cursor = db.getAllClasses();
        Set<String> uniqueGrades = new HashSet<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int colIndex = cursor.getColumnIndex("grade_level");
                if(colIndex != -1) uniqueGrades.add(cursor.getString(colIndex));
            } while (cursor.moveToNext());
            cursor.close();
        }

        List<String> sortedGrades = new ArrayList<>(uniqueGrades);
        Collections.sort(sortedGrades);
        for (String grade : sortedGrades) popup.getMenu().add(grade);

        popup.setOnMenuItemClickListener(item -> {
            selectedGrade = item.getTitle().toString();
            tvFilterGrade.setText(selectedGrade);
            loadTimetableData();
            return true;
        });
        popup.show();
    }

    private void showTeacherPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add("All Teachers");

        Cursor cursor = db.getTeachers();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int colIndex = cursor.getColumnIndex("full_name");
                if(colIndex != -1) popup.getMenu().add(cursor.getString(colIndex));
            } while (cursor.moveToNext());
            cursor.close();
        }

        popup.setOnMenuItemClickListener(item -> {
            selectedTeacher = item.getTitle().toString();
            tvFilterTeacher.setText(selectedTeacher);
            loadTimetableData();
            return true;
        });
        popup.show();
    }

    private void loadTimetableData() {
        if(containerTimetable == null) return;
        containerTimetable.removeAllViews();

        Cursor cursor = db.getScheduleForDay(selectedDay);
        int count = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Safe Column Fetching
                int idxGrade = cursor.getColumnIndex("grade_level");
                int idxTeacher = cursor.getColumnIndex("teacher_name");
                int idxSection = cursor.getColumnIndex("section_name");
                int idxSubject = cursor.getColumnIndex("subject");
                int idxRoom = cursor.getColumnIndex("room");
                int idxStart = cursor.getColumnIndex("start_time");

                // Skip if columns missing (DB mismatch)
                if (idxGrade == -1 || idxTeacher == -1) continue;

                String grade = cursor.getString(idxGrade);
                String teacher = cursor.getString(idxTeacher);

                // Filters
                if (!selectedGrade.equals("All Grades") && !grade.equals(selectedGrade)) continue;
                if (!selectedTeacher.equals("All Teachers") && !teacher.equals(selectedTeacher)) continue;

                count++;

                String section = cursor.getString(idxSection);
                String subject = cursor.getString(idxSubject);
                String room = cursor.getString(idxRoom);
                String startTime = cursor.getString(idxStart);

                // Inflate Layout
                View row = getLayoutInflater().inflate(R.layout.item_schedule_class_row, containerTimetable, false);

                TextView tvTime = row.findViewById(R.id.tv_schedule_time);
                TextView tvMeridiem = row.findViewById(R.id.tv_schedule_meridiem);
                TextView tvSubject = row.findViewById(R.id.tv_schedule_subject);
                TextView tvLocation = row.findViewById(R.id.tv_schedule_location);
                View indicator = row.findViewById(R.id.view_status_indicator);

                // Format Time
                String[] timeParts = startTime.split(":");
                int hour = Integer.parseInt(timeParts[0]);
                String meridiem = (hour >= 12) ? "PM" : "AM";
                if(hour > 12) hour -= 12;
                tvTime.setText(String.format("%02d:%s", hour, timeParts[1]));
                tvMeridiem.setText(meridiem);

                tvSubject.setText(subject + " (" + grade + "-" + section + ")");
                tvLocation.setText(room + " • " + teacher);

                // Color Coding
                if(grade.contains("10")) indicator.setBackgroundColor(Color.parseColor("#4318FF"));
                else if(grade.contains("11")) indicator.setBackgroundColor(Color.parseColor("#FFB547"));
                else indicator.setBackgroundColor(Color.parseColor("#05CD99"));

                containerTimetable.addView(row);

            } while (cursor.moveToNext());
            cursor.close();
        }

        // DEBUG: Show user if data is missing
        if (count == 0) {
            TextView emptyView = new TextView(this);
            emptyView.setText("No classes found for " + selectedDay);
            emptyView.setTextColor(Color.GRAY);
            emptyView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            emptyView.setPadding(0, 50, 0, 0);
            containerTimetable.addView(emptyView);
            Toast.makeText(this, "No data found in DB for " + selectedDay, Toast.LENGTH_SHORT).show();
        }
    }
}
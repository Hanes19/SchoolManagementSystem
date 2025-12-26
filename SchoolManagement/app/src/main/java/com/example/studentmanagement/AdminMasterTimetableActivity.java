package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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

        // Bind Views
        btnFilterDay = findViewById(R.id.filter_day);
        btnFilterGrade = findViewById(R.id.filter_grade);
        btnFilterTeacher = findViewById(R.id.filter_teacher);

        tvFilterDay = findViewById(R.id.tv_filter_day);
        tvFilterGrade = findViewById(R.id.tv_filter_grade);
        tvFilterTeacher = findViewById(R.id.tv_filter_teacher);

        // Back Button
        findViewById(R.id.header).setOnClickListener(v -> finish());

        // Set Listeners
        btnFilterDay.setOnClickListener(this::showDayPopup);
        btnFilterGrade.setOnClickListener(this::showGradePopup);
        btnFilterTeacher.setOnClickListener(this::showTeacherPopup);

        // Initial Load
        loadTimetableData();
    }

    private void showDayPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        for (String day : days) {
            popup.getMenu().add(day);
        }

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

        // Fetch distinct grades from Classes table
        Cursor cursor = db.getAllClasses();
        Set<String> uniqueGrades = new HashSet<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Assuming index 1 is 'grade_level' based on query: SELECT class_id, grade_level...
                String grade = cursor.getString(1);
                if (grade != null) uniqueGrades.add(grade);
            } while (cursor.moveToNext());
            cursor.close();
        }

        List<String> sortedGrades = new ArrayList<>(uniqueGrades);
        Collections.sort(sortedGrades);

        for (String grade : sortedGrades) {
            popup.getMenu().add(grade);
        }

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

        // Fetch teachers from Users table
        Cursor cursor = db.getTeachers();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Column 1 is 'full_name' in getTeachers query
                String teacherName = cursor.getString(1);
                popup.getMenu().add(teacherName);
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
        // TODO: Implement the logic to refresh the layout (R.id.container_timetable)
        // based on selectedDay, selectedGrade, and selectedTeacher.
        // Currently, it just logs a toast for verification.

        String message = "Loading: " + selectedDay + " | " + selectedGrade + " | " + selectedTeacher;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
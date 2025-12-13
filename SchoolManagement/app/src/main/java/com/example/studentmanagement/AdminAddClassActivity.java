package com.example.studentmanagement;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AdminAddClassActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText etGrade, etSection, etRoom;
    TextView tvSelectedTeacher;
    String selectedTeacherId = null;

    // Store teacher data
    List<String> teacherNames = new ArrayList<>();
    List<String> teacherIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_class);

        db = new DatabaseHelper(this);

        // Bind Views (Make sure you added IDs in XML!)
        etGrade = findViewById(R.id.et_grade_level);
        etSection = findViewById(R.id.et_section_name);
        etRoom = findViewById(R.id.et_room_number);
        tvSelectedTeacher = findViewById(R.id.tv_selected_teacher);

        // Back Button
        findViewById(R.id.header).setOnClickListener(v -> finish());

        // Teacher Selector
        findViewById(R.id.layout_select_teacher).setOnClickListener(v -> showTeacherSelectionDialog());

        // Create Button
        findViewById(R.id.btn_create).setOnClickListener(v -> saveClass());
    }

    private void showTeacherSelectionDialog() {
        teacherNames.clear();
        teacherIds.clear();

        Cursor cursor = db.getTeachers();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                teacherIds.add(cursor.getString(0)); // user_id
                teacherNames.add(cursor.getString(1)); // full_name
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (teacherNames.isEmpty()) {
            Toast.makeText(this, "No teachers found in database", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] namesArr = teacherNames.toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Assign Teacher");
        builder.setItems(namesArr, (dialog, which) -> {
            tvSelectedTeacher.setText(namesArr[which]);
            tvSelectedTeacher.setTextColor(getResources().getColor(android.R.color.black));
            selectedTeacherId = teacherIds.get(which);
        });
        builder.show();
    }

    private void saveClass() {
        String grade = etGrade.getText().toString().trim();
        String section = etSection.getText().toString().trim();
        String room = etRoom.getText().toString().trim();

        if (grade.isEmpty() || section.isEmpty() || room.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = db.addClass(grade, section, room, selectedTeacherId);
        if (success) {
            Toast.makeText(this, "Class Created Successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Return to list
        } else {
            Toast.makeText(this, "Error creating class", Toast.LENGTH_SHORT).show();
        }
    }
}
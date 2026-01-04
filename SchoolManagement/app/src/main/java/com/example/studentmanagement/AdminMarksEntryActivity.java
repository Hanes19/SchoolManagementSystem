package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AdminMarksEntryActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Spinner spCategory, spClass, spSubject;
    private LinearLayout llStudentList;
    private List<String> examIds = new ArrayList<>();
    private List<StudentMarkRow> studentRows = new ArrayList<>();

    // Helper class to track dynamic views
    private class StudentMarkRow {
        String studentId;
        EditText etScore;
        public StudentMarkRow(String id, EditText et) { studentId = id; etScore = et; }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_marks_entry);

        db = new DatabaseHelper(this);

        spCategory = findViewById(R.id.sp_exam_category);
        spClass = findViewById(R.id.sp_class_select);
        spSubject = findViewById(R.id.sp_subject_select);
        llStudentList = findViewById(R.id.ll_student_marks_list);
        Button btnLoad = findViewById(R.id.btn_load_students);
        Button btnSave = findViewById(R.id.btn_save_marks);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadSpinners();

        btnLoad.setOnClickListener(v -> loadStudents());
        btnSave.setOnClickListener(v -> saveMarks());
    }

    private void loadSpinners() {
        // 1. Exams
        List<String> exams = new ArrayList<>();
        examIds.clear();
        Cursor cExams = db.getAllExamCategories();
        if (cExams != null && cExams.moveToFirst()) {
            do {
                examIds.add(cExams.getString(0));
                exams.add(cExams.getString(1));
            } while (cExams.moveToNext());
            cExams.close();
        }
        // FIX: Use R.layout.spinner_item here
        ArrayAdapter<String> examAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, exams);
        examAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(examAdapter);

        // 2. Classes
        List<String> classes = new ArrayList<>();
        Cursor cClasses = db.getAllClasses();
        if (cClasses != null && cClasses.moveToFirst()) {
            do {
                // Formatting class name as "Grade 10-Emerald"
                classes.add(cClasses.getString(1) + "-" + cClasses.getString(2));
            } while (cClasses.moveToNext());
            cClasses.close();
        }
        // FIX: Use R.layout.spinner_item here
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, classes);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClass.setAdapter(classAdapter);

        // 3. Subjects
        String[] subjects = {"Mathematics", "English", "Science", "History", "Physics", "Chemistry"};
        // FIX: Use R.layout.spinner_item here
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, subjects);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubject.setAdapter(subjectAdapter);
    }

    private void loadStudents() {
        llStudentList.removeAllViews();
        studentRows.clear();

        if (spCategory.getSelectedItem() == null || spClass.getSelectedItem() == null) return;

        String examId = examIds.get(spCategory.getSelectedItemPosition());
        String className = spClass.getSelectedItem().toString();
        String subject = spSubject.getSelectedItem().toString();

        Cursor cursor = db.getStudentsWithMarks(className, examId, subject);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String studentId = cursor.getString(0);
                String name = cursor.getString(1);
                int score = cursor.getInt(2); // 0 if null usually, handled by query logic

                addStudentRow(studentId, name, score);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "No students found in this class", Toast.LENGTH_SHORT).show();
        }
    }

    private void addStudentRow(String studentId, String name, int currentScore) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(16, 16, 16, 16);
        row.setGravity(Gravity.CENTER_VERTICAL);

        // 1. Student Name
        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f));
        tvName.setTextColor(Color.BLACK);
        tvName.setTextSize(16);

        // 2. Score Input (Disabled by default)
        EditText etScore = new EditText(this);
        if (currentScore > 0) etScore.setText(String.valueOf(currentScore));
        etScore.setHint("0");
        etScore.setInputType(InputType.TYPE_CLASS_NUMBER);
        etScore.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        // Lock the field initially
        etScore.setEnabled(false);
        etScore.setTextColor(Color.DKGRAY); // Make it look readable but disabled

        // 3. Edit/Save Button
        Button btnAction = new Button(this);
        btnAction.setText("Edit");
        btnAction.setTextSize(12);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.2f);
        btnParams.setMargins(8, 0, 0, 0);
        btnAction.setLayoutParams(btnParams);

        // 4. Button Logic
        btnAction.setOnClickListener(v -> {
            String status = btnAction.getText().toString();

            if (status.equals("Edit")) {
                // Unlock for editing
                etScore.setEnabled(true);
                etScore.requestFocus();
                etScore.setTextColor(Color.BLACK);
                btnAction.setText("Save");
            } else {
                // Save to Database
                String scoreStr = etScore.getText().toString();
                int score = scoreStr.isEmpty() ? 0 : Integer.parseInt(scoreStr);

                // Get current selections from spinners
                if (spCategory.getSelectedItem() != null && spSubject.getSelectedItem() != null) {
                    String examId = examIds.get(spCategory.getSelectedItemPosition());
                    String subject = spSubject.getSelectedItem().toString();

                    // Save directly
                    db.saveExamMark(examId, studentId, subject, score, 100);

                    Toast.makeText(this, "Grade Updated for " + name, Toast.LENGTH_SHORT).show();
                }

                // Lock the field again
                etScore.setEnabled(false);
                etScore.setTextColor(Color.DKGRAY);
                btnAction.setText("Edit");
            }
        });

        row.addView(tvName);
        row.addView(etScore);
        row.addView(btnAction);

        llStudentList.addView(row);

        // Still add to list in case you want to use the global "Save All" button later
        studentRows.add(new StudentMarkRow(studentId, etScore));
    }

    private void saveMarks() {
        if (spCategory.getSelectedItem() == null) return;

        String examId = examIds.get(spCategory.getSelectedItemPosition());
        String subject = spSubject.getSelectedItem().toString();

        for (StudentMarkRow row : studentRows) {
            String scoreStr = row.etScore.getText().toString();
            int score = scoreStr.isEmpty() ? 0 : Integer.parseInt(scoreStr);

            // Assuming total marks is 100 for simplicity
            db.saveExamMark(examId, row.studentId, subject, score, 100);
        }
        Toast.makeText(this, "Marks Saved Successfully!", Toast.LENGTH_SHORT).show();
    }
}
package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddStudentActivity extends AppCompatActivity {

    private EditText etName, etId, etEmail, etEmergency;
    private Spinner spClass;
    private LinearLayout llSubjectContainer;
    private TextView tvTotalFees, tvNoSubjects;
    private DatabaseHelper db;

    // Helper for Spinner
    private static class ClassItem {
        int id;
        String name;
        String grade;

        ClassItem(int id, String name, String grade) {
            this.id = id;
            this.name = name;
            this.grade = grade;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // Helper for CheckBox tags
    private static class SubjectItem {
        int id;
        String name;
        double cost;

        SubjectItem(int id, String name, double cost) {
            this.id = id;
            this.name = name;
            this.cost = cost;
        }
    }

    private List<ClassItem> classList = new ArrayList<>();
    private List<SubjectItem> selectedSubjects = new ArrayList<>();
    private int selectedClassId = -1;
    private double currentTotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_directory_add_student);

        db = new DatabaseHelper(this);

        // Bind Views
        etName = findViewById(R.id.et_student_name);
        etId = findViewById(R.id.et_student_id);
        spClass = findViewById(R.id.sp_student_class);
        etEmail = findViewById(R.id.et_student_email);
        etEmergency = findViewById(R.id.et_emergency_contact);

        // New Views for Subject Selection
        llSubjectContainer = findViewById(R.id.ll_subject_container);
        tvTotalFees = findViewById(R.id.tv_total_fees);
        tvNoSubjects = findViewById(R.id.tv_no_subjects);

        ImageView btnBack = findViewById(R.id.btn_back);
        if(btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Auto-Generate ID
        TextView btnGenId = findViewById(R.id.btn_generate_student_id);
        btnGenId.setOnClickListener(v -> {
            int randomId = 100000 + new Random().nextInt(900000);
            etId.setText("STU" + randomId);
        });

        // Register Button
        CardView btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> saveStudent());

        loadClasses();
    }

    private void loadClasses() {
        Cursor cursor = db.getAllClasses();
        classList.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // class_id, grade_level, section_name
                int id = cursor.getInt(0);
                String grade = cursor.getString(1);
                String section = cursor.getString(2);
                classList.add(new ClassItem(id, grade + " - " + section, grade));
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<ClassItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClass.setAdapter(adapter);

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClassItem selected = classList.get(position);
                selectedClassId = selected.id;
                // Fetch subjects based on the Grade Level of the selected class
                loadSubjectsForGrade(selected.grade);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedClassId = -1;
                llSubjectContainer.removeAllViews();
            }
        });
    }

    private void loadSubjectsForGrade(String gradeLevel) {
        llSubjectContainer.removeAllViews();
        selectedSubjects.clear();
        currentTotal = 0.0;
        updateTotalDisplay();

        Cursor cursor = db.getSubjectsByGrade(gradeLevel);

        if (cursor != null && cursor.moveToFirst()) {
            tvNoSubjects.setVisibility(View.GONE);
            do {
                int subId = cursor.getInt(cursor.getColumnIndexOrThrow("subject_id"));
                String subName = cursor.getString(cursor.getColumnIndexOrThrow("subject_name"));
                double cost = cursor.getDouble(cursor.getColumnIndexOrThrow("cost"));

                SubjectItem item = new SubjectItem(subId, subName, cost);
                addSubjectCheckBox(item);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            tvNoSubjects.setVisibility(View.VISIBLE);
            tvNoSubjects.setText("No subjects found for " + gradeLevel);
        }
    }

    private void addSubjectCheckBox(SubjectItem item) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(item.name + " ($" + String.format("%.2f", item.cost) + ")");
        checkBox.setTextSize(14);
        checkBox.setTextColor(getResources().getColor(R.color.black));
        checkBox.setPadding(0, 10, 0, 10);

        // Add listener to update total when checked/unchecked
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedSubjects.add(item);
                currentTotal += item.cost;
            } else {
                selectedSubjects.remove(item);
                currentTotal -= item.cost;
            }
            updateTotalDisplay();
        });

        llSubjectContainer.addView(checkBox);
    }

    private void updateTotalDisplay() {
        tvTotalFees.setText("$" + String.format("%.2f", currentTotal));
    }

    private void saveStudent() {
        String name = etName.getText().toString().trim();
        String id = etId.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String emergency = etEmergency.getText().toString().trim();

        if (name.isEmpty() || id.isEmpty() || selectedClassId == -1) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedSubjects.isEmpty()) {
            Toast.makeText(this, "Please select at least one subject", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Create Student
        boolean isInserted = db.enrollStudent(name, id, selectedClassId, "N/A", "N/A", email, emergency);

        if (isInserted) {
            // 2. Enroll in Subjects & Generate Fees
            for (SubjectItem sub : selectedSubjects) {
                // Link student to subject
                db.enrollStudentInSubject(id, sub.id);

                // Add Fee Record
                db.addFee(id, "Tuition: " + sub.name, sub.cost, "Tuition");
            }

            Toast.makeText(this, "Enrollment Complete! Fees Generated.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Error: Student ID may already exist.", Toast.LENGTH_SHORT).show();
        }
    }
}
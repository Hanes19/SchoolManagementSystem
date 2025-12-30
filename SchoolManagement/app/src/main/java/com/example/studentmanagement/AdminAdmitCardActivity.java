package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminAdmitCardActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Spinner spExam, spClass;
    private LinearLayout llList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_admit_cards);

        db = new DatabaseHelper(this);
        spExam = findViewById(R.id.sp_exam_select);
        spClass = findViewById(R.id.sp_class_select);
        llList = findViewById(R.id.ll_student_list); // Ensure ID added to XML

        findViewById(R.id.btn_back_admit).setOnClickListener(v -> finish());

        setupSpinners();

        findViewById(R.id.btn_generate).setOnClickListener(v -> generateList());
    }

    private void setupSpinners() {
        // Mock data. In real app, fetch from TABLE_EXAM_CATEGORIES and TABLE_CLASSES
        String[] exams = {"Midterm 2025", "Finals 2025"};
        String[] classes = {"Grade 10-A", "Grade 10-B", "Grade 11-A"};

        ArrayAdapter<String> examAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, exams);
        spExam.setAdapter(examAdapter);

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
        spClass.setAdapter(classAdapter);
    }

    private void generateList() {
        llList.removeAllViews();
        String selectedClass = spClass.getSelectedItem().toString();

        Cursor cursor = db.getStudentsForAdmitCard(selectedClass);
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String roll = cursor.getString(cursor.getColumnIndexOrThrow("roll_no")); // Ensure roll_no exists in Users table or handle error

                // Reuse a simple student row layout
                View view = inflater.inflate(R.layout.item_student_row, llList, false);

                TextView tvName = view.findViewById(R.id.tv_student_name); // Assuming IDs from item_student_row
                TextView tvRoll = view.findViewById(R.id.tv_student_id);

                tvName.setText(name);
                tvRoll.setText("Roll: " + (roll != null ? roll : "N/A"));

                // Add click listener to "Download" or "Print" icon if present in layout
                // view.findViewById(R.id.btn_download).setOnClickListener(...);

                llList.addView(view);
            } while (cursor.moveToNext());
            Toast.makeText(this, "Admit Cards Generated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No students found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
}
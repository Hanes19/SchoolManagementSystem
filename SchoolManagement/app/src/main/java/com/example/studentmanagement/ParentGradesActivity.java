package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ParentGradesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_grades);

        studentId = getIntent().getStringExtra("STUDENT_ID");
        db = new DatabaseHelper(this);
        llList = findViewById(R.id.ll_grades_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadGrades();
    }

    private void loadGrades() {
        llList.removeAllViews();
        // Fetch all grades (pass "All" as semester/term)
        Cursor cursor = db.getStudentGrades(studentId, "All");
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String grade = cursor.getString(cursor.getColumnIndexOrThrow("grade")); // or score
                // If you stored score as Integer in Exam module, cast it:
                // String score = String.valueOf(cursor.getInt(...));

                // Reusing item_student_row.xml or creating a simple view programmatically
                // Here is a simple dynamic view for demonstration:
                View view = inflater.inflate(R.layout.item_expense_row, llList, false);
                TextView tvSubject = view.findViewById(R.id.tv_expense_title);
                TextView tvGrade = view.findViewById(R.id.tv_expense_amount);
                TextView tvDesc = view.findViewById(R.id.tv_expense_category);

                tvSubject.setText(subject);
                tvGrade.setText(grade); // e.g. "95" or "A"
                tvDesc.setText("Midterm 2025"); // Placeholder or fetch from DB

                llList.addView(view);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
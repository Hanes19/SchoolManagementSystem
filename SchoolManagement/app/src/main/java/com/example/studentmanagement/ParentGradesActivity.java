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
        // Fetch "All" semesters
        Cursor cursor = db.getStudentGrades(studentId, "All");
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String grade = cursor.getString(cursor.getColumnIndexOrThrow("grade"));
                String semester = cursor.getString(cursor.getColumnIndexOrThrow("semester"));

                View row = inflater.inflate(R.layout.item_expense_row, llList, false);

                TextView tvSubject = row.findViewById(R.id.tv_expense_title);
                TextView tvGrade = row.findViewById(R.id.tv_expense_amount);
                TextView tvSem = row.findViewById(R.id.tv_expense_category);

                tvSubject.setText(subject);
                tvGrade.setText(grade);
                tvSem.setText(semester);

                // Logic: Color Coding
                if ("A".equals(grade) || "A+".equals(grade)) {
                    tvGrade.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else if ("F".equals(grade)) {
                    tvGrade.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    tvGrade.setTextColor(getResources().getColor(android.R.color.black));
                }

                llList.addView(row);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudentGradesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_grades);

        db = new DatabaseHelper(this);
        studentId = getIntent().getStringExtra("STUDENT_ID");

        // FIX 1: Match the XML ID for the container (see XML fix below)
        llList = findViewById(R.id.ll_grades_list);

        // FIX 2: Match the XML ID for the back button
        findViewById(R.id.btn_back_grades).setOnClickListener(v -> finish());

        loadGrades();
    }

    private void loadGrades() {
        llList.removeAllViews();
        Cursor cursor = db.getStudentGrades(studentId, "Midterm");
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String exam = cursor.getString(cursor.getColumnIndexOrThrow("exam_name"));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
                int max = cursor.getInt(cursor.getColumnIndexOrThrow("total_marks"));

                // Reuse item_expense_row or similar generic card
                View view = inflater.inflate(R.layout.item_expense_row, llList, false);
                TextView tvSubject = view.findViewById(R.id.tv_expense_title);
                TextView tvExam = view.findViewById(R.id.tv_expense_category);
                TextView tvScore = view.findViewById(R.id.tv_expense_amount);

                tvSubject.setText(subject);
                tvExam.setText(exam);
                tvScore.setText(score + "/" + max);

                llList.addView(view);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
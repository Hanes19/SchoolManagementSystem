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

        // This ID now exists in your XML
        llList = findViewById(R.id.ll_grades_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadGrades();
    }

    private void loadGrades() {
        if (llList == null) return;
        llList.removeAllViews();

        Cursor cursor = db.getStudentGrades(studentId, "All");
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String grade = cursor.getString(cursor.getColumnIndexOrThrow("grade"));

                // Reuse the row layout
                View view = inflater.inflate(R.layout.item_expense_row, llList, false);

                // FIX: Map the variables to the IDs that actually exist in item_expense_row.xml
                TextView tvSubject = view.findViewById(R.id.tv_expense_title);
                TextView tvGrade = view.findViewById(R.id.tv_amount);        // Correct ID for right-side text
                TextView tvDesc = view.findViewById(R.id.tv_requested_by);   // Correct ID for subtitle

                tvSubject.setText(subject);
                tvGrade.setText(grade);
                tvDesc.setText("Midterm 2025");

                llList.addView(view);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
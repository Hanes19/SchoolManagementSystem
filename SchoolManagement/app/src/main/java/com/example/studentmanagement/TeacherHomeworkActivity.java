package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherHomeworkActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llAssignmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_homework_management);

        db = new DatabaseHelper(this);
        llAssignmentList = findViewById(R.id.ll_assignment_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.fab_add_homework).setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherAddHomeworkActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAssignments();
    }

    private void loadAssignments() {
        llAssignmentList.removeAllViews();
        Cursor cursor = db.getAllAssignments();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String className = cursor.getString(cursor.getColumnIndexOrThrow("class_name"));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String dueDate = cursor.getString(cursor.getColumnIndexOrThrow("due_date"));
                int maxScore = cursor.getInt(cursor.getColumnIndexOrThrow("max_score"));

                View itemView = inflater.inflate(R.layout.item_assignment_row, llAssignmentList, false);
                TextView tvTitle = itemView.findViewById(R.id.tv_title);
                TextView tvDetails = itemView.findViewById(R.id.tv_details);
                TextView tvScore = itemView.findViewById(R.id.tv_score);

                tvTitle.setText(title);
                tvDetails.setText("Due: " + dueDate + " • " + className);
                tvScore.setText("Max Points: " + maxScore);

                llAssignmentList.addView(itemView);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
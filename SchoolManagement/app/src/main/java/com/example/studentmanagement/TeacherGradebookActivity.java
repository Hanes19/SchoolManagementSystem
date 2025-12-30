package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherGradebookActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView rvGradebook;
    private GradebookAdapter adapter;
    private List<StudentGradeModel> studentList;

    private int currentAssignmentId = 1; // Defaulting to 1 for demo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_gradebook);

        db = new DatabaseHelper(this);
        rvGradebook = findViewById(R.id.rv_gradebook_list);
        rvGradebook.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        adapter = new GradebookAdapter(studentList);
        rvGradebook.setAdapter(adapter);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Save Button Logic
        findViewById(R.id.btn_save_container).setOnClickListener(v -> saveAllGrades());

        loadStudents();
    }

    private void loadStudents() {
        studentList.clear();
        Cursor cursor = db.getStudentsForGradebook();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String id = cursor.getString(cursor.getColumnIndexOrThrow("user_id")); // or user_id

                // Fetch existing grade
                int score = db.getStudentGrade(currentAssignmentId, id);

                studentList.add(new StudentGradeModel(id, name, score));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void saveAllGrades() {
        // In a real app, you might iterate the adapter's data or use a callback
        // For simplicity, we assume the adapter updates the model list directly

        for (StudentGradeModel student : studentList) {
            if (student.getCurrentScore() != -1) {
                db.saveStudentGrade(currentAssignmentId, student.getId(), student.getCurrentScore());
            }
        }
        Toast.makeText(this, "Grades Saved Successfully!", Toast.LENGTH_SHORT).show();
    }
}
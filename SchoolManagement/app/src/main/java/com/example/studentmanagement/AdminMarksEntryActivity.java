package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AdminMarksEntryActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;
    private List<View> rowViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_marks_entry);

        db = new DatabaseHelper(this);
        llList = findViewById(R.id.ll_student_marks_list); // Ensure ID added

        findViewById(R.id.header).setOnClickListener(v -> finish());

        findViewById(R.id.btn_save_marks).setOnClickListener(v -> saveAllMarks());

        loadStudents();
    }

    private void loadStudents() {
        llList.removeAllViews();
        rowViews.clear();

        // Fetch all students in a class (Hardcoded class ID '1' for Grade 10)
        Cursor cursor = db.getStudentsByClass("1");
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String id = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));

                // Inflate the student row (reuse teacher_item_gradebook or similar)
                View view = inflater.inflate(R.layout.teacher_item_gradebook, llList, false);

                TextView tvName = view.findViewById(R.id.tv_student_name);
                TextView tvId = view.findViewById(R.id.tv_student_id);
                EditText etMark = view.findViewById(R.id.et_grade_input);

                tvName.setText(name);
                tvId.setText(id);
                view.setTag(id); // Store ID in tag for retrieval

                llList.addView(view);
                rowViews.add(view);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void saveAllMarks() {
        for (View view : rowViews) {
            String studentId = (String) view.getTag();
            EditText etMark = view.findViewById(R.id.et_grade_input);
            String scoreStr = etMark.getText().toString();

            if (!scoreStr.isEmpty()) {
                db.saveExamMark(1, studentId, "Mathematics", Integer.parseInt(scoreStr));
            }
        }
        Toast.makeText(this, "Marks Saved!", Toast.LENGTH_SHORT).show();
    }
}
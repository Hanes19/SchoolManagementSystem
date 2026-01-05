package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AdminManageSubjectsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String studentId;
    private ListView lvSubjects;
    private TextView tvBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_subjects);

        db = new DatabaseHelper(this);
        studentId = getIntent().getStringExtra("STUDENT_ID");

        // FIX: Prevent crash if no student ID is passed
        if (studentId == null) {
            Toast.makeText(this, "Error: No Student Selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        lvSubjects = findViewById(R.id.lv_subjects);
        tvBanner = findViewById(R.id.tv_student_name_banner);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.fab_add_subject).setOnClickListener(v -> showAddSubjectDialog());

        loadStudentDetails();
        loadSubjects();
    }

    private void loadStudentDetails() {
        String name = db.getUserName(studentId);
        tvBanner.setText("Managing: " + name);
    }

    private void loadSubjects() {
        Cursor cursor = db.getStudentEnrolledSubjects(studentId);
        if (cursor != null) {
            SubjectListAdapter adapter = new SubjectListAdapter(cursor);
            lvSubjects.setAdapter(adapter);
        }
    }

    private void showAddSubjectDialog() {
        // Get Grade Level to filter subjects
        String gradeLevel = db.getStudentGradeLevel(studentId);
        if (gradeLevel == null) {
            Toast.makeText(this, "Student is not assigned to a class/grade.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get Available Subjects
        Cursor subjectsCursor = db.getSubjectsByGrade(gradeLevel);
        List<String> subjectNames = new ArrayList<>();
        List<Integer> subjectIds = new ArrayList<>();

        if (subjectsCursor != null && subjectsCursor.moveToFirst()) {
            do {
                subjectNames.add(subjectsCursor.getString(subjectsCursor.getColumnIndexOrThrow("subject_name")));
                subjectIds.add(subjectsCursor.getInt(subjectsCursor.getColumnIndexOrThrow("subject_id")));
            } while (subjectsCursor.moveToNext());
            subjectsCursor.close();
        }

        if (subjectNames.isEmpty()) {
            Toast.makeText(this, "No subjects found for " + gradeLevel, Toast.LENGTH_SHORT).show();
            return;
        }

        // Setup Dialog with Spinner
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Subject");

        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subjectNames);
        spinner.setAdapter(adapter);

        // Add padding for better look
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        spinner.setPadding(padding, padding, padding, padding);

        builder.setView(spinner);

        builder.setPositiveButton("Add", (dialog, which) -> {
            int position = spinner.getSelectedItemPosition();
            if (position >= 0) {
                int selectedId = subjectIds.get(position);
                db.enrollStudentInSubject(studentId, selectedId);
                Toast.makeText(this, "Subject Added", Toast.LENGTH_SHORT).show();
                loadSubjects();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Custom Adapter to handle Delete Button in List
    private class SubjectListAdapter extends CursorAdapter {
        public SubjectListAdapter(Cursor cursor) {
            super(AdminManageSubjectsActivity.this, cursor, 0);
        }

        @Override
        public View newView(android.content.Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_subject_row, parent, false);
        }

        @Override
        public void bindView(View view, android.content.Context context, Cursor cursor) {
            TextView tvName = view.findViewById(R.id.tv_role_name);
            TextView tvDesc = view.findViewById(R.id.tv_role_desc);
            ImageView btnDelete = view.findViewById(R.id.btn_delete_role);

            // Capture data for the listener
            String subjectName = cursor.getString(cursor.getColumnIndexOrThrow("subject_name"));
            int subjectId = cursor.getInt(cursor.getColumnIndexOrThrow("subject_id"));

            tvName.setText(subjectName);
            tvDesc.setText("Enrolled");

            // Setup Delete Action
            btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Subject")
                        .setMessage("Remove " + subjectName + " from this student?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            if (db.removeStudentSubject(studentId, subjectId)) {
                                Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                                loadSubjects(); // Refresh List
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
        }
    }
}
package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

public class StudentGradesActivity extends AppCompatActivity {

    private LinearLayout gradesContainer;
    private Spinner semesterSpinner;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_grades);

        // 1. Initialize Database & Session
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        studentId = sessionManager.getUserId();

        // 2. Bind Views using IDs from student_grades.xml
        gradesContainer = findViewById(R.id.ll_grades_list);
        semesterSpinner = findViewById(R.id.spinner_semester_filter);
        ImageView btnBack = findViewById(R.id.btn_back_grades);

        // 3. Setup Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 4. Setup Semester Spinner
        setupSpinner();

        // 5. Load Initial Data
        loadGrades("All");
    }

    private void setupSpinner() {
        // Create a list of options
        List<String> semesters = new ArrayList<>();
        semesters.add("All");
        semesters.add("Midterm");
        semesters.add("Finals");

        // Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, semesters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(adapter);

        // Handle selection
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSemester = semesters.get(position);
                loadGrades(selectedSemester);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadGrades(String semester) {
        if (gradesContainer == null) return;

        // Clear previous list items
        gradesContainer.removeAllViews();

        // Fetch data from Database
        Cursor cursor = dbHelper.getStudentGrades(studentId, semester);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // Extract data from Cursor based on your DatabaseHelper table columns
                // Columns: grade_id, assignment_id, student_id, subject, score, grade, semester, ...
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String grade = cursor.getString(cursor.getColumnIndexOrThrow("grade"));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));

                // Add the card to the UI
                addGradeCard(subject, grade, String.valueOf(score));
            }
        } else {
            // Show "No Records" message
            TextView noData = new TextView(this);
            noData.setText("No grades found for this term.");
            noData.setPadding(20, 20, 20, 20);
            noData.setGravity(Gravity.CENTER);
            gradesContainer.addView(noData);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void addGradeCard(String subject, String grade, String score) {
        // Create CardView container
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24); // Bottom margin
        card.setLayoutParams(params);
        card.setRadius(24);
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(4);

        // Inner Layout (Horizontal)
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(32, 32, 32, 32);
        inner.setGravity(Gravity.CENTER_VERTICAL);

        // Icon (Left side)
        ImageView icon = new ImageView(this);
        icon.setImageResource(android.R.drawable.star_on); // You can use R.drawable.book if available
        icon.setColorFilter(Color.parseColor("#FF9800"));
        icon.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        inner.addView(icon);

        // Text Details (Middle)
        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(32, 0, 0, 0);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvSubject = new TextView(this);
        tvSubject.setText(subject);
        tvSubject.setTextSize(18);
        tvSubject.setTextColor(Color.parseColor("#1B254B"));
        tvSubject.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvScore = new TextView(this);
        tvScore.setText("Score: " + score); // Removed "credits" as it wasn't in the cursor fetch
        tvScore.setTextSize(14);
        tvScore.setTextColor(Color.parseColor("#A3AED0"));

        textLayout.addView(tvSubject);
        textLayout.addView(tvScore);
        inner.addView(textLayout);

        // Grade Badge (Right side)
        TextView tvGrade = new TextView(this);
        tvGrade.setText(grade);
        tvGrade.setTextSize(20);
        tvGrade.setTypeface(null, android.graphics.Typeface.BOLD);

        // Color coding based on grade
        if ("A".equalsIgnoreCase(grade) || "A+".equalsIgnoreCase(grade)) {
            tvGrade.setTextColor(Color.parseColor("#4CAF50")); // Green
        } else if ("B".equalsIgnoreCase(grade)) {
            tvGrade.setTextColor(Color.parseColor("#2196F3")); // Blue
        } else if ("C".equalsIgnoreCase(grade)) {
            tvGrade.setTextColor(Color.parseColor("#FF9800")); // Orange
        } else {
            tvGrade.setTextColor(Color.RED);
        }

        inner.addView(tvGrade);
        card.addView(inner);

        // Add to main container
        gradesContainer.addView(card);
    }
}
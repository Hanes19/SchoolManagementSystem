package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import java.util.List;

public class AdminAttendanceActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout userListContainer;
    private EditText etSearch;
    private Spinner filterSpinner;
    private ArrayList<StudentModel> allStudents;
    private String currentStudentId;

    private static class StudentModel {
        String id, name, rollNo, status, className;
        public StudentModel(String id, String name, String rollNo, String status, String className) {
            this.id = id; this.name = name; this.rollNo = rollNo; this.status = status; this.className = className;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_directory_students);

        if (getIntent().hasExtra("STUDENT_ID")) {
            currentStudentId = getIntent().getStringExtra("STUDENT_ID");
        } else {
            currentStudentId = "stud01"; // Fallback for testing
        }

        db = new DatabaseHelper(this);
        userListContainer = findViewById(R.id.ll_user_list);
        etSearch = findViewById(R.id.etSearch);

        // Customize Header
        LinearLayout header = findViewById(R.id.header);
        if (header != null && header.getChildCount() > 1 && header.getChildAt(1) instanceof TextView) {
            TextView title = (TextView) header.getChildAt(1);
            title.setText("Attendance Directory");
        }

        ImageView btnBack = findViewById(R.id.btn_back);
        if(btnBack != null) btnBack.setOnClickListener(v -> finish());

        View fab = findViewById(R.id.fab_add_user);
        if(fab != null) fab.setVisibility(View.GONE);

        // Setup Filter Spinner
        setupFilterSpinner();

        // Load Data
        loadAllStudents();

        // Search Listener
        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    applyFilters();
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void setupFilterSpinner() {
        LinearLayout tabContainer = findViewById(R.id.tab_container);

        if (tabContainer != null) {
            tabContainer.setVisibility(View.VISIBLE);
            tabContainer.removeAllViews();

            filterSpinner = new Spinner(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            filterSpinner.setLayoutParams(params);

            tabContainer.addView(filterSpinner);
            loadSpinnerData();
        }
    }

    private void loadSpinnerData() {
        List<String> classes = new ArrayList<>();
        classes.add("All Classes"); // Default Option

        Cursor cursor = db.getAllClasses();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String grade = cursor.getString(1);
                String section = cursor.getString(2);
                classes.add(grade + "-" + section);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // FIX: Use 'simple_spinner_item' for the selected view (fixes white text problem)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        // Use 'simple_spinner_dropdown_item' for the popup list
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadAllStudents() {
        allStudents = new ArrayList<>();
        Cursor cursor = db.getAllStudentsWithClassDetails();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String roll = cursor.getString(2);
                String status = cursor.getString(3);
                String grade = cursor.getString(4);
                String section = cursor.getString(5);

                String fullClass = (grade != null && section != null) ? grade + "-" + section : "Unassigned";
                allStudents.add(new StudentModel(id, name, roll, status, fullClass));
            } while (cursor.moveToNext());
            cursor.close();
        }
        applyFilters();
    }

    private void applyFilters() {
        if (userListContainer == null) return;
        userListContainer.removeAllViews();

        String searchText = etSearch.getText().toString().toLowerCase().trim();
        String selectedClass = (filterSpinner != null && filterSpinner.getSelectedItem() != null)
                ? filterSpinner.getSelectedItem().toString()
                : "All Classes";

        boolean hasResults = false;

        for (StudentModel student : allStudents) {
            boolean matchesClass = selectedClass.equals("All Classes") || student.className.equalsIgnoreCase(selectedClass);
            boolean matchesSearch = student.name.toLowerCase().contains(searchText) ||
                    (student.rollNo != null && student.rollNo.toLowerCase().contains(searchText));

            if (matchesClass && matchesSearch) {
                addStudentCard(student);
                hasResults = true;
            }
        }

        if (!hasResults) {
            TextView noData = new TextView(this);
            noData.setText("No students found matching criteria.");
            noData.setPadding(32, 32, 32, 32);
            userListContainer.addView(noData);
        }
    }

    private void addStudentCard(StudentModel student) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);
        card.setRadius(30f);
        card.setCardElevation(0);
        card.setCardBackgroundColor(Color.WHITE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(32, 32, 32, 32);
        layout.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Profile Icon
        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.profile_pic);
        icon.setBackgroundColor(Color.parseColor("#F4F7FE"));
        icon.setPadding(20, 20, 20, 20);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(100, 100);
        icon.setLayoutParams(iconParams);
        layout.addView(icon);

        // Text Info
        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(32, 0, 0, 0);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        textLayout.setLayoutParams(textParams);

        TextView tvName = new TextView(this);
        tvName.setText(student.name);
        tvName.setTextSize(16);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        tvName.setTextColor(Color.parseColor("#1B254B"));
        textLayout.addView(tvName);

        TextView tvDetails = new TextView(this);
        String details = student.className + " | Roll: " + (student.rollNo != null ? student.rollNo : "N/A");
        tvDetails.setText(details);
        tvDetails.setTextSize(12);
        tvDetails.setTextColor(Color.parseColor("#A3AED0"));
        textLayout.addView(tvDetails);

        layout.addView(textLayout);

        // Action Text
        TextView tvAction = new TextView(this);
        tvAction.setText("View >");
        tvAction.setTextColor(Color.parseColor("#4CAF50")); // Green
        tvAction.setTypeface(null, android.graphics.Typeface.BOLD);
        tvAction.setTextSize(12);
        layout.addView(tvAction);

        card.addView(layout);

        // FIX: Pass the specific Student ID to the profile activity
        card.setOnClickListener(v -> {
            Intent intent = new Intent(AdminAttendanceActivity.this, AdminStudentProfileActivity.class);
            intent.putExtra("STUDENT_ID", student.id);
            startActivity(intent);
        });

        userListContainer.addView(card);
    }
}
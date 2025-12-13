package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminStudentListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_directory_students);

        // ... inside onCreate
        setupTabs();

// Link the Sample Data Click
        findViewById(R.id.card_student_1).setOnClickListener(v -> {
            // Open the Profile
            android.content.Intent intent = new android.content.Intent(this, AdminStudentProfileActivity.class);
            startActivity(intent);
        });
    }

    private void setupTabs() {
        // 1. Get the Tab Container
        LinearLayout tabContainer = findViewById(R.id.tab_container);

        // 2. Get the specific Tab buttons by their index (0=Student, 1=Teacher, 2=Staff)
        TextView tabStudent = (TextView) tabContainer.getChildAt(0);
        TextView tabTeacher = (TextView) tabContainer.getChildAt(1);
        TextView tabStaff   = (TextView) tabContainer.getChildAt(2);

        // 3. Setup Back Button (Inside the header)
        LinearLayout header = findViewById(R.id.header);
        header.getChildAt(0).setOnClickListener(v -> finish()); // The ImageView is the first child

        // 4. Tab Click Logic

        // Student Tab (Already here, do nothing)
        tabStudent.setOnClickListener(v -> {});

        // Teacher Tab -> Go to Teacher Activity
        tabTeacher.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminTeacherListActivity.class));
            overridePendingTransition(0, 0); // No animation (looks like a tab switch)
            finish();
        });

        // Staff Tab -> Go to Staff Activity
        tabStaff.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminStaffListActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
    }
}
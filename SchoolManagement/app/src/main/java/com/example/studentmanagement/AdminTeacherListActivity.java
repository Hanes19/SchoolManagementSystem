package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminTeacherListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_directory_teacher); // Note: singular 'teacher' as per your file

        // ... inside onCreate
        setupTabs();

// Link the Sample Data Click
        findViewById(R.id.card_teacher_1).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, AdminTeacherProfileActivity.class);
            startActivity(intent);
        });
    }

    private void setupTabs() {
        LinearLayout tabContainer = findViewById(R.id.tab_container);
        TextView tabStudent = (TextView) tabContainer.getChildAt(0);
        TextView tabTeacher = (TextView) tabContainer.getChildAt(1);
        TextView tabStaff   = (TextView) tabContainer.getChildAt(2);

        LinearLayout header = findViewById(R.id.header);
        header.getChildAt(0).setOnClickListener(v -> finish());

        // Student Tab -> Go to Student Activity
        tabStudent.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminStudentListActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });

        // Teacher Tab (Already here)
        tabTeacher.setOnClickListener(v -> {});

        // Staff Tab -> Go to Staff Activity
        tabStaff.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminStaffListActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
    }
}
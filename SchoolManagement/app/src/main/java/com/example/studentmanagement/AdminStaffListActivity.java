package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminStaffListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_directory_staff);

        // ... inside onCreate
        setupTabs();

// Link the Sample Data Click
        findViewById(R.id.card_staff_1).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, AdminStaffProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_add_staff).setOnClickListener(v -> {
            Intent intent = new Intent(AdminStaffListActivity.this, AddStaffActivity.class);
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

        // Teacher Tab -> Go to Teacher Activity
        tabTeacher.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminTeacherListActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });

        // Staff Tab (Already here)
        tabStaff.setOnClickListener(v -> {});
    }
}
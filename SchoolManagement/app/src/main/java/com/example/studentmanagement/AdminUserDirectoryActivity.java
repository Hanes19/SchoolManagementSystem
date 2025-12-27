package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminUserDirectoryActivity extends AppCompatActivity {

    // Views
    private ImageView btnBack;
    private CardView cardStudents, cardTeachers, cardStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_actions_userdirectory);

        // 1. Bind Views
        btnBack = findViewById(R.id.btnBack);
        cardStudents = findViewById(R.id.cardStudents);
        cardTeachers = findViewById(R.id.cardTeachers);
        cardStaff = findViewById(R.id.cardStaff);

        // 2. Set Click Listeners

        // Back Button
        btnBack.setOnClickListener(v -> finish());

        // Students Section
        cardStudents.setOnClickListener(v -> {
            Intent intent = new Intent(AdminUserDirectoryActivity.this, AdminStudentListActivity.class);
            startActivity(intent);
        });

        // Teachers Section
        cardTeachers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminUserDirectoryActivity.this, AdminTeacherListActivity.class);
            startActivity(intent);
        });

        // Staff Section
        cardStaff.setOnClickListener(v -> {
            Intent intent = new Intent(AdminUserDirectoryActivity.this, AdminStaffListActivity.class);
            startActivity(intent);
        });
    }
}
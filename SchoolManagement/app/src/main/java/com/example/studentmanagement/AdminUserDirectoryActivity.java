package com.example.studentmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminUserDirectoryActivity extends AppCompatActivity {

    private TextView tabStudents, tabTeachers, tabStaff;
    private FrameLayout contentContainer;

    // Colors
    private final int COLOR_ACTIVE = Color.parseColor("#1B254B"); // Dark Blue
    private final int COLOR_INACTIVE = Color.parseColor("#9E9E9E"); // Grey

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use the new master layout we created
        setContentView(R.layout.activity_admin_user_directory);

        // Initialize UI Views
        contentContainer = findViewById(R.id.content_container);

        // Back Button Logic
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Tab Click Listeners
        tabStudents.setOnClickListener(v -> loadView("Student"));
        tabTeachers.setOnClickListener(v -> loadView("Teacher"));
        tabStaff.setOnClickListener(v -> loadView("Staff"));

        // Default Load
        loadView("Student");
    }

    private void loadView(String type) {
        // 1. Update Tab Styles (Highlight active tab)
        updateTabs(type);

        // 2. Clear previous content
        contentContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        // 3. Inflate the appropriate layout file provided by user
        View view;
        switch (type) {
            case "Teacher":
                view = inflater.inflate(R.layout.admin_user_directory_teacher, contentContainer, false);
                setupTeacherLogic(view);
                break;
            case "Staff":
                view = inflater.inflate(R.layout.admin_user_directory_staff, contentContainer, false);
                setupStaffLogic(view);
                break;
            case "Student":
            default:
                view = inflater.inflate(R.layout.admin_user_directory_students, contentContainer, false);
                setupStudentLogic(view);
                break;
        }

        // 4. Hide the duplicate header inside the included layout if it exists
        // (Assuming your individual XMLs still have the id 'header' from your previous code)
        View duplicateHeader = view.findViewById(R.id.header);
        if (duplicateHeader != null) {
            duplicateHeader.setVisibility(View.GONE);
        }

        // 5. Add the view to container
        contentContainer.addView(view);
    }

    private void updateTabs(String activeType) {
        tabStudents.setTextColor(activeType.equals("Student") ? COLOR_ACTIVE : COLOR_INACTIVE);
        tabTeachers.setTextColor(activeType.equals("Teacher") ? COLOR_ACTIVE : COLOR_INACTIVE);
        tabStaff.setTextColor(activeType.equals("Staff") ? COLOR_ACTIVE : COLOR_INACTIVE);
    }

    // --- Logic for individual screens ---

    private void setupStudentLogic(View view) {
        // Find views inside 'view' and set listeners
        // Example: View btnAdd = view.findViewById(R.id.fab_add_user);
        // if (btnAdd != null) btnAdd.setOnClickListener(...)

        View fab = view.findViewById(R.id.fab_add_user);
        if (fab != null) {
            fab.setOnClickListener(v -> startActivity(new Intent(this, AddStudentActivity.class)));
        }
    }

    private void setupTeacherLogic(View view) {
        View fab = view.findViewById(R.id.fab_add_user);
        if (fab != null) {
            fab.setOnClickListener(v -> startActivity(new Intent(this, AddTeacherActivity.class)));
        }
    }

    private void setupStaffLogic(View view) {
        View btnAdd = view.findViewById(R.id.btn_add_staff); // Note: Your staff xml used 'btn_add_staff'
        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> startActivity(new Intent(this, AddStaffActivity.class)));
        }
    }
}
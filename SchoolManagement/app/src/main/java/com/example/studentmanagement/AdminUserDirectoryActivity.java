package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminUserDirectoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Retrieve the type passed from the Main Menu (e.g., "Teacher", "Staff")
        String type = getIntent().getStringExtra("type");

        // 2. Default to "Student" only if no type was passed
        if (type == null || type.isEmpty()) {
            type = "Student";
        }

        // 3. Load the correct directory
        loadDirectory(type);
    }

    private void loadDirectory(String type) {
        // 1. Swap the entire layout based on selection
        if (type.equals("Student")) {
            setContentView(R.layout.admin_user_directory_students);
            setupStudentLogic(); // CALL THE LOGIC HERE
        } else if (type.equals("Teacher")) {
            setContentView(R.layout.admin_user_directory_teacher);
            setupTeacherLogic(); // CALL THE LOGIC HERE (removed view param)
        } else if (type.equals("Staff")) {
            setContentView(R.layout.admin_user_directory_staff);
            setupStaffLogic();   // CALL THE LOGIC HERE (removed view param)
        }

        // 2. Re-find and re-set listeners for navigation tabs
        setupTabs(type);
        setupButtons(type);
    }

    private void setupTabs(String activeType) {
        TextView tabStudents = findViewById(R.id.tabStudents);
        TextView tabTeachers = findViewById(R.id.tabTeachers);
        TextView tabStaff = findViewById(R.id.tabStaff);

        // Click listeners to "open" the other files
        tabStudents.setOnClickListener(v -> loadDirectory("Student"));
        tabTeachers.setOnClickListener(v -> loadDirectory("Teacher"));
        tabStaff.setOnClickListener(v -> loadDirectory("Staff"));
    }

    private void setupButtons(String type) {
        // Back Button (Common to all)
        View btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Add Button (ID is slightly different in your Staff file)
        View fabAdd = (type.equals("Staff")) ? findViewById(R.id.btn_add_staff) : findViewById(R.id.fab_add_user);

        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                Class<?> targetClass = AddStudentActivity.class; // Default
                if (type.equals("Teacher")) targetClass = AddTeacherActivity.class;
                if (type.equals("Staff")) targetClass = AddStaffActivity.class;

                startActivity(new Intent(this, targetClass));
            });
        }
    }

    // --- Logic for individual screens ---

// --- Updated logic for individual screens ---

    private void setupStudentLogic() {
        // Handle Card Clicks (Matching IDs in admin_user_directory_students.xml)
        int[] studentIds = {R.id.card_student_1, R.id.card_student_2, R.id.card_student_3,
                R.id.card_student_4, R.id.card_student_5, R.id.card_student_6, R.id.card_student_7};

        for (int id : studentIds) {
            View card = findViewById(id);
            if (card != null) {
                card.setOnClickListener(v -> startActivity(new Intent(this, AdminStudentProfileActivity.class)));
            }
        }
    }

    private void setupTeacherLogic() {
        // Handle Card Clicks (Matching IDs in admin_user_directory_teacher.xml)
        int[] teacherIds = {R.id.card_teacher_1, R.id.card_teacher_2, R.id.card_teacher_3,
                R.id.card_teacher_4, R.id.card_teacher_5, R.id.card_teacher_6, R.id.card_teacher_7};

        for (int id : teacherIds) {
            View card = findViewById(id);
            if (card != null) {
                card.setOnClickListener(v -> startActivity(new Intent(this, AdminTeacherProfileActivity.class)));
            }
        }
    }

    private void setupStaffLogic() {
        // Handle Card Clicks (Matching IDs in admin_user_directory_staff.xml)
        int[] staffIds = {R.id.card_staff_1, R.id.card_staff_2, R.id.card_staff_3,
                R.id.card_staff_4, R.id.card_staff_5, R.id.card_staff_6};

        for (int id : staffIds) {
            View card = findViewById(id);
            if (card != null) {
                card.setOnClickListener(v -> startActivity(new Intent(this, AdminStaffProfileActivity.class)));
            }
        }
    }
}
package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class AdminMainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_menu);

        // --- Header Actions ---
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // ==========================================
        // 1. PEOPLE & ROLES
        // ==========================================
        setupLink(R.id.btn_module_students, AdminUserDirectoryActivity.class, "Student");
        setupLink(R.id.btn_module_teachers, AdminUserDirectoryActivity.class, "Teacher");
        setupLink(R.id.btn_module_staff, AdminUserDirectoryActivity.class, "Staff");
        setupLink(R.id.btn_module_parents, AdminParentDirectoryActivity.class, null);

        // ==========================================
        // 2. ACADEMICS
        // ==========================================
        setupLink(R.id.btn_module_classes, AdminClassListActivity.class, null);
        setupLink(R.id.btn_module_timetable, AdminMasterTimetableActivity.class, null);
        setupLink(R.id.btn_module_attendance, AdminAttendanceActivity.class, null);
        setupLink(R.id.btn_module_exams, AdminExamDashboardActivity.class, null);

        // ==========================================
        // 3. FINANCE & ADMIN
        // ==========================================
        setupLink(R.id.btn_module_fees, AdminFeesBillingActivity.class, null);
        setupLink(R.id.btn_module_payroll, AdminPayrollActivity.class, null);

        // This button now exists in the XML, so this line is valid
        setupLink(R.id.btn_module_library, LibraryDashboardActivity.class, null);
    }

    private void setupLink(int id, Class<?> targetActivity, String typeExtra) {
        LinearLayout btn = findViewById(id);
        if (btn != null) {
            btn.setOnClickListener(v -> {
                Intent intent = new Intent(this, targetActivity);
                if (typeExtra != null) {
                    intent.putExtra("type", typeExtra);
                }
                startActivity(intent);
            });
        }
    }
}
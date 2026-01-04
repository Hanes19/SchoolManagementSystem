package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
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
        // 1. PEOPLE & ROLES (Redirect to Directory)
        // ==========================================

        // Students -> AdminUserDirectoryActivity (Filter: Student)
        LinearLayout btnStudents = findViewById(R.id.btn_module_students);
        if (btnStudents != null) {
            btnStudents.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminUserDirectoryActivity.class);
                intent.putExtra("type", "Student");
                startActivity(intent);
            });
        }

        // Teachers -> AdminUserDirectoryActivity (Filter: Teacher)
        LinearLayout btnTeachers = findViewById(R.id.btn_module_teachers);
        if (btnTeachers != null) {
            btnTeachers.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminUserDirectoryActivity.class);
                intent.putExtra("type", "Teacher");
                startActivity(intent);
            });
        }

        // Staff -> AdminUserDirectoryActivity (Filter: Staff)
        LinearLayout btnStaff = findViewById(R.id.btn_module_staff);
        if (btnStaff != null) {
            btnStaff.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminUserDirectoryActivity.class);
                intent.putExtra("type", "Staff");
                startActivity(intent);
            });
        }

        // Parents -> AdminParentDirectoryActivity
        LinearLayout btnParents = findViewById(R.id.btn_module_parents);
        if (btnParents != null) {
            btnParents.setOnClickListener(v -> startActivity(new Intent(this, AdminParentDirectoryActivity.class)));
        }

        // ==========================================
        // 2. ACADEMICS
        // ==========================================

        // Classes
        LinearLayout btnClasses = findViewById(R.id.btn_module_classes);
        if (btnClasses != null) {
            btnClasses.setOnClickListener(v -> startActivity(new Intent(this, AdminClassListActivity.class)));
        }

        // Timetable
        LinearLayout btnTimetable = findViewById(R.id.btn_module_timetable);
        if (btnTimetable != null) {
            btnTimetable.setOnClickListener(v -> startActivity(new Intent(this, AdminMasterTimetableActivity.class)));
        }

        // Attendance (FIX: Linked to new Activity)
        LinearLayout btnAttendance = findViewById(R.id.btn_module_attendance);
        if (btnAttendance != null) {
            btnAttendance.setOnClickListener(v -> startActivity(new Intent(this, AdminAttendanceActivity.class)));
        }

        // Exams (Linked to Dashboard)
        LinearLayout btnExams = findViewById(R.id.btn_module_exams);
        if (btnExams != null) {
            btnExams.setOnClickListener(v -> startActivity(new Intent(this, AdminExamDashboardActivity.class)));
        }

        // ==========================================
        // 3. FINANCE & ADMIN
        // ==========================================

        // Fees (FIX: Linked to Billing Activity)
        LinearLayout btnFees = findViewById(R.id.btn_module_fees);
        if (btnFees != null) {
            btnFees.setOnClickListener(v -> startActivity(new Intent(this, AdminFeesBillingActivity.class)));
        }

        // Payroll
        LinearLayout btnPayroll = findViewById(R.id.btn_module_payroll);
        if (btnPayroll != null) {
            btnPayroll.setOnClickListener(v -> startActivity(new Intent(this, AdminPayrollActivity.class)));
        }

        // Library
        LinearLayout btnLibrary = findViewById(R.id.btn_module_library);
        if (btnLibrary != null) {
            btnLibrary.setOnClickListener(v -> startActivity(new Intent(this, LibraryDashboardActivity.class)));
        }

        // ==========================================
        // 4. OTHERS (Coming Soon / Minor)
        // ==========================================

        // Notice Board
        LinearLayout btnNotice = findViewById(R.id.btn_module_notice);
        if (btnNotice != null) {
            btnNotice.setOnClickListener(v -> startActivity(new Intent(this, AdminNoticeBoardActivity.class)));
        }

        // Events / Calendar
        LinearLayout btnEvents = findViewById(R.id.btn_module_events);
        if (btnEvents != null) {
            btnEvents.setOnClickListener(v -> startActivity(new Intent(this, AdminCalendarActivity.class)));
        }

        // System Logs
        LinearLayout btnLogs = findViewById(R.id.btn_module_logs);
        if (btnLogs != null) {
            btnLogs.setOnClickListener(v -> startActivity(new Intent(this, SystemLogActivity.class)));
        }

        // Settings
        LinearLayout btnSettings = findViewById(R.id.btn_module_settings);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> startActivity(new Intent(this, AdminSettingsActivity.class)));
        }

        // Placeholders for features not yet implemented
        setupComingSoon(R.id.btn_module_leaves, "Leave Management");
        setupComingSoon(R.id.btn_module_transport, "Transport");
        setupComingSoon(R.id.btn_module_inventory, "Inventory");
    }

    private void setupComingSoon(int id, String featureName) {
        LinearLayout btn = findViewById(id);
        if (btn != null) {
            btn.setOnClickListener(v -> Toast.makeText(this, featureName + " Module Coming Soon", Toast.LENGTH_SHORT).show());
        }
    }
}
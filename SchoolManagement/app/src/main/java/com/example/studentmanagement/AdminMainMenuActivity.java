package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Import your new activities here if they are in a different package,
// otherwise they will be found automatically.
// import com.example.studentmanagement.AdminParentDirectoryActivity;
// import com.example.studentmanagement.AdminReportsGuideActivity;

public class AdminMainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_menu);

        // --- Header Actions ---
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish()); // Go back to Dashboard

        // --- PEOPLE & ROLES ---
        LinearLayout btnStudents = findViewById(R.id.btn_module_students);
        btnStudents.setOnClickListener(v -> startActivity(new Intent(this, AdminStudentListActivity.class)));

        LinearLayout btnTeachers = findViewById(R.id.btn_module_teachers);
        btnTeachers.setOnClickListener(v -> startActivity(new Intent(this, AdminTeacherListActivity.class)));

        LinearLayout btnStaff = findViewById(R.id.btn_module_staff);
        btnStaff.setOnClickListener(v -> startActivity(new Intent(this, AdminStaffListActivity.class)));

        // UPDATED: Now links to the Parent Directory
        LinearLayout btnParents = findViewById(R.id.btn_module_parents);
        btnParents.setOnClickListener(v -> startActivity(new Intent(this, AdminParentDirectoryActivity.class)));

        // --- ACADEMICS ---
        LinearLayout btnClasses = findViewById(R.id.btn_module_classes);
        btnClasses.setOnClickListener(v -> startActivity(new Intent(this, AdminClassListActivity.class)));

        LinearLayout btnTimetable = findViewById(R.id.btn_module_timetable);
        btnTimetable.setOnClickListener(v -> startActivity(new Intent(this, AdminMasterTimetableActivity.class)));

        LinearLayout btnAttendance = findViewById(R.id.btn_module_attendance);
        btnAttendance.setOnClickListener(v -> Toast.makeText(this, "Attendance Module Coming Soon", Toast.LENGTH_SHORT).show());

        LinearLayout btnExams = findViewById(R.id.btn_module_exams);
        btnExams.setOnClickListener(v -> Toast.makeText(this, "Exams Module Coming Soon", Toast.LENGTH_SHORT).show());

        // --- FINANCE & ADMIN ---
        LinearLayout btnFees = findViewById(R.id.btn_module_fees);
        btnFees.setOnClickListener(v -> startActivity(new Intent(this, AdminFeesBillingActivity.class)));

        LinearLayout btnPayroll = findViewById(R.id.btn_module_payroll);
        btnPayroll.setOnClickListener(v -> startActivity(new Intent(this, AdminPayrollActivity.class)));

        LinearLayout btnExpenses = findViewById(R.id.btn_module_expenses);
        btnExpenses.setOnClickListener(v -> startActivity(new Intent(this, AdminExpensesActivity.class)));

        // --- GENERAL ---
        LinearLayout btnSystemConfig = findViewById(R.id.btn_general_config);
        btnSystemConfig.setOnClickListener(v -> startActivity(new Intent(this, AdminSystemConfigActivity.class)));

        LinearLayout btnSettings = findViewById(R.id.btn_general_settings);
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, AdminSettingsActivity.class)));

        // FAQ button is intentionally omitted as requested.
    }
}
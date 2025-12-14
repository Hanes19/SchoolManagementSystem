package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        LinearLayout btnParents = findViewById(R.id.btn_module_parents);
        btnParents.setOnClickListener(v -> Toast.makeText(this, "Parents Module Coming Soon", Toast.LENGTH_SHORT).show());


        // --- ACADEMICS ---
        LinearLayout btnClasses = findViewById(R.id.btn_module_classes);
        btnClasses.setOnClickListener(v -> startActivity(new Intent(this, AdminClassListActivity.class)));

        LinearLayout btnTimetable = findViewById(R.id.btn_module_timetable);
        btnTimetable.setOnClickListener(v -> startActivity(new Intent(this, AdminMasterTimetableActivity.class)));

        LinearLayout btnAttendance = findViewById(R.id.btn_module_attendance);
        btnAttendance.setOnClickListener(v -> Toast.makeText(this, "Attendance Module Coming Soon", Toast.LENGTH_SHORT).show());

        LinearLayout btnExams = findViewById(R.id.btn_module_exams);
        btnExams.setOnClickListener(v -> Toast.makeText(this, "Exams Module Coming Soon", Toast.LENGTH_SHORT).show());


// --- FINANCE ---
        LinearLayout btnFees = findViewById(R.id.btn_module_fees);
        btnFees.setOnClickListener(v -> startActivity(new Intent(this, AdminFeesBillingActivity.class)));

        LinearLayout btnPayroll = findViewById(R.id.btn_module_payroll);
// FIX: Change btnTimetable to btnPayroll, and ensure the Intent target is correct (e.g., AdminPayrollActivity)
        btnPayroll.setOnClickListener(v -> startActivity(new Intent(this, AdminPayrollActivity.class))); // Check if this class is correct for Payroll

        LinearLayout btnExpenses = findViewById(R.id.btn_module_expenses);
// FIX: Change btnTimetable to btnExpenses
        btnExpenses.setOnClickListener(v -> startActivity(new Intent(this, AdminExpensesActivity.class)));


        // --- GENERAL ---
        LinearLayout btnSystemConfig = findViewById(R.id.btn_general_config);
        btnSystemConfig.setOnClickListener(v -> startActivity(new Intent(this, AdminSystemConfigActivity.class)));

        LinearLayout btnSettings = findViewById(R.id.btn_general_settings);
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, AdminSettingsActivity.class)));

    }
}
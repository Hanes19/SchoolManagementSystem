package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AdminDashboardActivity extends AppCompatActivity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        session = new SessionManager(this);

        // Security Check
        if (!session.isLoggedIn() || !"Admin".equals(session.getRole())) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // --- 1. Users & Staff Module Group ---
        findViewById(R.id.btn_users_staff).setOnClickListener(v -> showUsersBottomSheet());

        // --- 2. Academic Structure Group (Classes & Timetable) ---
        findViewById(R.id.btn_classes_sections).setOnClickListener(v -> showAcademicBottomSheet());

        // --- 3. Fees & Billing Group ---
        findViewById(R.id.btn_fees_billing).setOnClickListener(v -> {
            // Direct link or bottom sheet if you have multiple fee activities
            startActivity(new Intent(AdminDashboardActivity.this, AdminFeesActivity.class));
        });

        // --- 4. System Config Group ---
        findViewById(R.id.btn_system_config).setOnClickListener(v -> showSystemBottomSheet());

        // --- 5. FAB (Quick Actions) ---
        findViewById(R.id.fab_add_new).setOnClickListener(v -> showQuickActionBottomSheet());

        // --- 6. Bottom Navigation ---
        setupBottomNavigation();
    }

    // --- Bottom Sheet Logic for "Users & Staff" ---
    private void showUsersBottomSheet() {
        BottomSheetDialog sheet = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.admin_quick_action, null); // Reusing layout container
        sheet.setContentView(view);

        // We dynamically add buttons here or you can create a specific layout: 'sheet_users_selection.xml'
        // For simplicity, we assume you might want to create a simple layout for this selection
        // OR simply route them to a Directory Landing Page if you have one.

        // Since we don't have a specific "sheet_users.xml", let's route to the Directory Activity
        // where tabs can handle Student/Teacher/Staff.
        startActivity(new Intent(this, AdminUserDirectoryActivity.class));
        sheet.dismiss();
    }

    // --- Bottom Sheet Logic for "Academic Structure" ---
    private void showAcademicBottomSheet() {
        // Simple dialog to choose between Classes or Timetable
        String[] options = {"Manage Classes", "Timetable & Schedule", "Exam Schedule"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Academic Management");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: startActivity(new Intent(this, AdminClassListActivity.class)); break;
                case 1: startActivity(new Intent(this, AdminClassTimetableActivity.class)); break;
                case 2: startActivity(new Intent(this, AdminScheduleExamActivity.class)); break;
            }
        });
        builder.show();
    }

    // --- Bottom Sheet Logic for "System Config" ---
    private void showSystemBottomSheet() {
        String[] options = {"General Settings", "System Logs", "School Profile", "Backup & Restore"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("System Configuration");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: startActivity(new Intent(this, AdminSettingsActivity.class)); break;
                case 1: startActivity(new Intent(this, SystemLogActivity.class)); break;
                case 2: startActivity(new Intent(this, AdminSchoolProfileActivity.class)); break;
                case 3: startActivity(new Intent(this, AdminBackupRestoreActivity.class)); break;
            }
        });
        builder.show();
    }

    private void showQuickActionBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        // Ensure 'admin_quick_action.xml' exists with clickable IDs
        View sheetView = LayoutInflater.from(this).inflate(R.layout.admin_quick_action, null);
        bottomSheetDialog.setContentView(sheetView);

        // Example binding for quick actions inside the sheet
        // sheetView.findViewById(R.id.btn_add_student).setOnClickListener(...)

        bottomSheetDialog.show();
    }

    private void setupBottomNavigation() {
        findViewById(R.id.btn_home).setOnClickListener(v -> { /* Already on Home */ });
        findViewById(R.id.btn_calendar).setOnClickListener(v -> startActivity(new Intent(this, AdminCalendarActivity.class)));
        findViewById(R.id.btn_bell).setOnClickListener(v -> startActivity(new Intent(this, AdminNotificationActivity.class)));
        findViewById(R.id.btnMain_menu).setOnClickListener(v -> startActivity(new Intent(this, AdminMainMenuActivity.class)));
    }
}
package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
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
        findViewById(R.id.btn_users_staff).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminUserDirectoryActivity.class));
        });

        // --- 2. Academic Structure Group ---
        findViewById(R.id.btn_classes_sections).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminClassListActivity.class));
        });

        // --- 3. Fees & Billing Group ---
        findViewById(R.id.btn_fees_billing).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminFeesBillingActivity.class));
        });

        findViewById(R.id.btn_system_config).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminSystemConfigActivity.class));
        });

        // --- Quick Status Links ---

        // 1. Unread Messages -> Admin Notification Activity
        findViewById(R.id.btn_quick_msg).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminNotificationActivity.class));
        });

        // 2. Pending Gradebook -> Admin Marks Entry Activity
        findViewById(R.id.btn_quick_grade).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminMarksEntryActivity.class));
        });

        // 3. Library Overdue -> Library Overdue Items Activity
        findViewById(R.id.btn_quick_library).setOnClickListener(v -> {
            startActivity(new Intent(this, LibraryOverdueItemsActivity.class));
        });

        // --- FAB: Quick Actions Bottom Sheet ---
        findViewById(R.id.fab_add_new).setOnClickListener(v -> showQuickActionSheet());

        setupBottomNavigation();
    }

    private void showQuickActionSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.admin_quick_action, null);
        bottomSheetDialog.setContentView(view);

        // 1. Add Student
        view.findViewById(R.id.btn_quick_add_student).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            startActivity(new Intent(this, AddStudentActivity.class));
        });

        // 2. Add Teacher
        view.findViewById(R.id.btn_quick_add_teacher).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            startActivity(new Intent(this, AddTeacherActivity.class));
        });

        // 3. New Invoice
        view.findViewById(R.id.btn_quick_invoice).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            startActivity(new Intent(this, AdminGenerateInvoiceActivity.class));
        });

        // 4. Post Notice
        view.findViewById(R.id.btn_quick_notice).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            startActivity(new Intent(this, AdminNoticeBoardActivity.class));
        });

        // 5. Take Attendance
        view.findViewById(R.id.btn_quick_attendance).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            startActivity(new Intent(this, AdminAttendanceActivity.class));
        });

        // 6. View Reports
        view.findViewById(R.id.btn_quick_reports).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            startActivity(new Intent(this, AdminReportsGuideActivity.class));
        });

        bottomSheetDialog.show();
    }

    private void setupBottomNavigation() {
        // Home button (Already here)
        findViewById(R.id.btn_home).setOnClickListener(v -> { });

        // Calendar
        findViewById(R.id.btn_calendar).setOnClickListener(v ->
                startActivity(new Intent(this, AdminCalendarActivity.class)));

        // Notifications
        findViewById(R.id.btn_bell).setOnClickListener(v ->
                startActivity(new Intent(this, AdminNotificationActivity.class)));

        // Main Menu
        findViewById(R.id.btnMain_menu).setOnClickListener(v ->
                startActivity(new Intent(this, AdminMainMenuActivity.class)));
    }
}
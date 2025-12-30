package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AdminDashboardActivity extends AppCompatActivity {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        // 1. Session & Security Check (From current code)
        session = new SessionManager(this);
        if (!session.isLoggedIn() || !session.getRole().equals("Admin")) {
            Toast.makeText(this, "Security Alert: Unauthorized Access!", Toast.LENGTH_LONG).show();
            session.logoutUser();
            finish();
            return;
        }

        // 2. Header Buttons (From current code)
        ImageView btnMain_menu = findViewById(R.id.btnMain_menu);
        btnMain_menu.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminMainMenuActivity.class);
            startActivity(intent);
        });

        ImageView btn_bell = findViewById(R.id.btn_bell); // Assuming ID based on context
        btn_bell.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminNotificationActivity.class);
            startActivity(intent);
        });

        // 3. Quick Action FAB (From current logic context)
        View fabQuick = findViewById(R.id.fab_quick_actions); // Ensure this ID matches your XML
        if (fabQuick != null) {
            fabQuick.setOnClickListener(v -> showQuickActions());
        }

        // ============================================================
        // 4. NEW: Dashboard Grid Navigation (Added Features)
        // ============================================================

        // User Management
        setupNav(R.id.card_students, AdminStudentListActivity.class);
        setupNav(R.id.card_teachers, AdminTeacherListActivity.class);
        setupNav(R.id.card_staff, AdminStaffListActivity.class);
        setupNav(R.id.card_parents, AdminParentDirectoryActivity.class);

        // Modules
        setupNav(R.id.card_exams, AdminExamDashboardActivity.class); // Exam Module
        setupNav(R.id.card_fees, AdminFeesActivity.class);           // Fee Module
        setupNav(R.id.card_library, LibraryDashboardActivity.class); // Library Module

        // Misc
        setupNav(R.id.card_settings, AdminSettingsActivity.class);
        // setupNav(R.id.card_notices, AdminNoticeBoardActivity.class); // Optional if card exists
    }

    // Helper method to setup CardView navigation
    private void setupNav(int id, Class<?> cls) {
        CardView card = findViewById(id);
        if (card != null) {
            card.setOnClickListener(v -> startActivity(new Intent(this, cls)));
        }
    }

    // 5. Quick Actions Bottom Sheet (From current code)
    private void showQuickActions() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.admin_quick_action, (LinearLayout) findViewById(R.id.bottomSheetContainer));

        // Setup Button Listeners inside the Sheet

        // Quick Add Student
        bottomSheetView.findViewById(R.id.btn_quick_add_student).setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddStudentActivity.class);
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        // Quick Add Teacher
        bottomSheetView.findViewById(R.id.btn_quick_add_teacher).setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddTeacherActivity.class);
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        // Quick Invoice
        bottomSheetView.findViewById(R.id.btn_quick_invoice).setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminGenerateInvoiceActivity.class);
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        // Note: Add listeners for other buttons (Notice, Attendance, etc.) as they appear in your admin_quick_action.xml

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}
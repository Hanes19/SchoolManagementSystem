package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class AdminDashboardActivity extends AppCompatActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard); // Ensure this matches your XML file name

        session = new SessionManager(this);

        if (!session.isLoggedIn() || !session.getRole().equals("Admin")) {
            Toast.makeText(this, "Security Alert: Unauthorized Access!", Toast.LENGTH_LONG).show();
            session.logoutUser();
            finish();
            return;
        }

        // --- NEW BUTTON INITIALIZATION ---

        // 1. Burger Menu Button
        // This will now work because we added btn_burger_menu to the XML


        // 2. Settings Button
        // This will now work because we renamed btnSettings to btn_settings in the XML
        ImageView btnMain_menu = findViewById(R.id.btnMain_menu);
        btnMain_menu.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminMainMenuActivity.class);
            startActivity(intent);
        });

        // --- END NEW BUTTONS ---

        // 1. Users Module
        CardView btnUsers = findViewById(R.id.btn_users_staff);
        btnUsers.setOnClickListener(v -> {
            Toast.makeText(this, "Opening User Management...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AdminStudentListActivity.class);
            startActivity(intent);
        });

        // 2. Classes & Timetable Module
        CardView btnClasses = findViewById(R.id.btn_classes_sections);
        btnClasses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminClassListActivity.class);
            startActivity(intent);
        });

        CardView btnFees = findViewById(R.id.btn_fees_billing);
        btnFees.setOnClickListener(v -> {
            // UPDATED: Now opens the Fees Activity
            Intent intent = new Intent(AdminDashboardActivity.this, AdminFeesBillingActivity.class);
            startActivity(intent);
        });

        // 4. System Config
        CardView btnConfig = findViewById(R.id.btn_system_config);
        btnConfig.setOnClickListener(v -> {
            Toast.makeText(this, "System logs coming soon", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminDashboardActivity.this, AdminSystemConfigActivity.class);
            startActivity(intent);
        });

        CardView fabAdd = findViewById(R.id.fab_add_new);
        fabAdd.setOnClickListener(v -> {
            showQuickActionsBottomSheet();
        });
    }
    private void showQuickActionsBottomSheet() {
        // 1. Initialize the BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // 2. Inflate the layout resource (admin_quick_action.xml)
        View bottomSheetView = LayoutInflater.from(this)
                .inflate(R.layout.admin_quick_action, null);

        // 3. Connect the inner views/buttons to listeners
        // Example: Quick Add Student
        bottomSheetView.findViewById(R.id.btn_quick_add_student).setOnClickListener(v -> {
            // Handle the click (e.g., Navigate to AddStudentActivity)
            Intent intent = new Intent(AdminDashboardActivity.this, AddStudentActivity.class); // Ensure this Activity exists
            startActivity(intent);
            bottomSheetDialog.dismiss(); // Close the sheet after clicking
        });

        // Example: Quick Add Teacher
        bottomSheetView.findViewById(R.id.btn_quick_add_teacher).setOnClickListener(v -> {
            // Handle click
            Intent intent = new Intent(AdminDashboardActivity.this, AddTeacherActivity.class); // Ensure this Activity exists
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        // Example: New Invoice
        bottomSheetView.findViewById(R.id.btn_quick_invoice).setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminGenerateInvoiceActivity.class); // Check your actual class name
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        // (Repeat for other buttons: btn_quick_notice, btn_quick_attendance, btn_quick_reports)

        // 4. Set the view to the dialog and show it
        bottomSheetDialog.setContentView(bottomSheetView);

        // OPTIONAL: This ensures the transparent background so your rounded corners (bottom_sheet_bg.xml) show correctly
        if (bottomSheetDialog.getWindow() != null) {
            bottomSheetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        bottomSheetDialog.show();
    }
}
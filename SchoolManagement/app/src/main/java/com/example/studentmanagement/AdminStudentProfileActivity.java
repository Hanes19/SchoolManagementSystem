package com.example.studentmanagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminStudentProfileActivity extends AppCompatActivity {

    private String currentStudentId;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_student_profile);

        db = new DatabaseHelper(this); // Initialize DB

        // --- Get ID from Intent ---
        if (getIntent().hasExtra("STUDENT_ID")) {
            currentStudentId = getIntent().getStringExtra("STUDENT_ID");
        } else {
            currentStudentId = "stud01"; // Fallback
            Toast.makeText(this, "No Student ID provided, defaulting to stud01", Toast.LENGTH_SHORT).show();
        }

        ImageView btnBack = findViewById(R.id.btn_back_profile);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Find Cards
        CardView cardAttendance = findViewById(R.id.card_attendance);
        CardView cardGpa = findViewById(R.id.card_gpa);
        CardView cardFees = findViewById(R.id.card_fees);
        CardView cardSchedule = findViewById(R.id.card_schedule);
        CardView btnCheckEligibility = findViewById(R.id.btn_check_eligibility); // Updated ID to match XML

        // --- NAVIGATION ---
        cardAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentAttendanceActivity.class);
            intent.putExtra("STUDENT_ID", currentStudentId);
            startActivity(intent);
        });

        cardGpa.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentGradesActivity.class);
            intent.putExtra("STUDENT_ID", currentStudentId);
            startActivity(intent);
        });

        cardFees.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentFeesActivity.class);
            intent.putExtra("STUDENT_ID", currentStudentId);
            startActivity(intent);
        });

        cardSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentScheduleActivity.class);
            intent.putExtra("STUDENT_ID", currentStudentId);
            startActivity(intent);
        });

        // --- NEW: Eligibility Check ---
        if (btnCheckEligibility != null) {
            btnCheckEligibility.setOnClickListener(v -> showEligibilityDialog());
        }
    }

    private void showEligibilityDialog() {
        DatabaseHelper.EligibilityResult result = db.checkEnrollmentEligibility(currentStudentId);

        StringBuilder message = new StringBuilder();

        // 1. Attendance Status
        message.append("Attendance: ").append(String.format("%.1f", result.attendancePercent)).append("%");
        if (result.attendancePercent < 80.0) {
            message.append(" ❌ (Requires 80%)");
        } else {
            message.append(" ✅");
        }
        message.append("\n\n");

        // 2. Financial Status (CHANGED TO PESO)
        message.append("Outstanding Balance: ₱").append(String.format("%.2f", result.outstandingBalance));
        if (result.outstandingBalance > 0) {
            message.append(" ❌ (Must be ₱0.00)");
        } else {
            message.append(" ✅");
        }
        message.append("\n\n");

        // 3. Academic Status
        message.append("Academic Standing: ");
        if (result.hasFailedSubjects) {
            message.append("Has Failed Subjects ❌");
        } else {
            message.append("All Passed ✅");
        }
        message.append("\n\n");

        // Final Verdict
        String title = result.isEligible ? "Eligible for Enrollment" : "Not Eligible";
        int icon = result.isEligible ? android.R.drawable.ic_input_add : android.R.drawable.ic_delete;

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message.toString())
                .setIcon(icon)
                .setPositiveButton("OK", null)
                .setNeutralButton("Proceed Anyway", (dialog, which) -> {
                    Toast.makeText(this, "Override authorized.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}
package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminTeacherProfileActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String teacherId;

    // UI Views
    private TextView tvName, tvId, tvEmail, tvPhone, tvQualification;
    private CardView btnDeactivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_teacher_profile);

        db = new DatabaseHelper(this);

        // 1. Get Teacher ID from Intent
        if (getIntent().hasExtra("TEACHER_ID")) {
            teacherId = getIntent().getStringExtra("TEACHER_ID");
        } else {
            Toast.makeText(this, "Error: No Teacher ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Initialize Views
        ImageView btnBack = findViewById(R.id.btnBack); // Ensure this ID exists in XML (it was added in my previous fix or needs adding)
        if (btnBack == null) btnBack = findViewById(R.id.header).findViewById(android.R.id.icon); // Fallback attempt or just safety check
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        tvName = findViewById(R.id.tv_profile_name);
        tvId = findViewById(R.id.tv_profile_id);
        tvEmail = findViewById(R.id.tv_profile_email);

        // IMPORTANT: You must add IDs to these views in your XML (see step 3 below)
        tvPhone = findViewById(R.id.tv_profile_phone);
        tvQualification = findViewById(R.id.tv_profile_qualification);

        btnDeactivate = findViewById(R.id.btn_delete_user);

        // 3. Load Data
        loadTeacherDetails();

        // 4. Deactivate Action
        btnDeactivate.setOnClickListener(v -> {
            boolean success = db.updateUserStatus(teacherId, false); // Set status to Inactive
            if (success) {
                Toast.makeText(this, "Teacher account deactivated", Toast.LENGTH_SHORT).show();
                finish(); // Go back to list
            } else {
                Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTeacherDetails() {
        Cursor cursor = db.getUserDetails(teacherId);
        if (cursor != null && cursor.moveToFirst()) {
            // Column indices based on DatabaseHelper.onCreate table structure:
            // 0=id, 1=user_id, 2=full_name, 3=password, 4=role, 5=class_id,
            // 6=status, 7=email, 8=phone, ...

            String name = cursor.getString(2);
            String email = cursor.getString(7);
            String phone = cursor.getString(8);
            // Note: Qualification is not in your current Users table schema, so we placeholder it
            // Or use a generic column if you added one.

            tvName.setText(name);
            tvId.setText("ID: " + teacherId);
            tvEmail.setText(email != null ? email : "No Email");

            if (tvPhone != null) {
                tvPhone.setText(phone != null ? phone : "No Phone");
            }

            if (tvQualification != null) {
                tvQualification.setText("Senior Faculty"); // Placeholder as DB doesn't have this col yet
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Teacher not found in database", Toast.LENGTH_SHORT).show();
        }
    }
}
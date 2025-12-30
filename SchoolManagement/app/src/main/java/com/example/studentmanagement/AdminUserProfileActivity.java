package com.example.studentmanagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminUserProfileActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvName, tvId, tvEmail;
    private String userId;
    private String userRole; // "Student", "Teacher", etc.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Decide layout based on role passed via Intent, or use a generic one
        userRole = getIntent().getStringExtra("USER_ROLE");
        userId = getIntent().getStringExtra("USER_ID");

        if ("Student".equals(userRole)) {
            setContentView(R.layout.admin_user_student_profile);
        } else if ("Teacher".equals(userRole)) {
            setContentView(R.layout.admin_user_teacher_profile);
        } else {
            setContentView(R.layout.admin_user_staff_profile);
        }

        db = new DatabaseHelper(this);

        tvName = findViewById(R.id.tv_profile_name);
        tvId = findViewById(R.id.tv_profile_id);
        tvEmail = findViewById(R.id.tv_profile_email);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish()); // Check ID

        findViewById(R.id.btn_edit_profile).setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminEditProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        findViewById(R.id.btn_delete_user).setOnClickListener(v -> showDeleteConfirm());

        loadDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDetails();
    }

    private void loadDetails() {
        Cursor cursor = db.getUserDetails(userId);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

            tvName.setText(name);
            tvId.setText("ID: " + userId);
            tvEmail.setText(email);
        }
        cursor.close();
    }

    private void showDeleteConfirm() {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (db.deleteUser(userId)) {
                        Toast.makeText(this, "User Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
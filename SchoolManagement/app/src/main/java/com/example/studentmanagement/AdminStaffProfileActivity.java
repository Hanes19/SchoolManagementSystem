package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminStaffProfileActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String staffId;

    private TextView tvName, tvId, tvEmail, tvPhone, tvShift;
    private CardView btnDeactivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_staff_profile);

        db = new DatabaseHelper(this);

        if (getIntent().hasExtra("STAFF_ID")) {
            staffId = getIntent().getStringExtra("STAFF_ID");
        } else {
            Toast.makeText(this, "Error: No Staff ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        tvName = findViewById(R.id.tv_profile_name);
        tvId = findViewById(R.id.tv_profile_id);
        tvEmail = findViewById(R.id.tv_profile_email);

        // IMPORTANT: Add IDs to XML (see step 3)
        tvPhone = findViewById(R.id.tv_profile_phone);
        tvShift = findViewById(R.id.tv_profile_shift);

        btnDeactivate = findViewById(R.id.btn_delete_user);

        loadStaffDetails();

        btnDeactivate.setOnClickListener(v -> {
            boolean success = db.updateUserStatus(staffId, false);
            if (success) {
                Toast.makeText(this, "Staff account deactivated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStaffDetails() {
        Cursor cursor = db.getUserDetails(staffId);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(2);
            String email = cursor.getString(7);
            String phone = cursor.getString(8);

            tvName.setText(name);
            tvId.setText("ID: " + staffId);
            tvEmail.setText(email != null ? email : "No Email");

            if (tvPhone != null) {
                tvPhone.setText(phone != null ? phone : "No Phone");
            }

            // Shift is not in DB table 'users', using placeholder or could be 'status'
            if (tvShift != null) {
                tvShift.setText("Day Shift (08:00 - 17:00)");
            }

            cursor.close();
        }
    }
}
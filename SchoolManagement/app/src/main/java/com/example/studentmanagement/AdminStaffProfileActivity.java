package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminStaffProfileActivity extends AppCompatActivity {

    private String staffId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_staff_profile);

        // 1. Safe ID Retrieval with Fallback
        if (getIntent().hasExtra("STAFF_ID")) {
            staffId = getIntent().getStringExtra("STAFF_ID");
        } else {
            staffId = "DEFAULT"; // Prevents "No ID Provided" error
        }

        // 2. Initialize Views
        TextView tvName = findViewById(R.id.tv_profile_name);
        TextView tvId = findViewById(R.id.tv_profile_id);
        TextView tvEmail = findViewById(R.id.tv_profile_email);
        TextView tvPhone = findViewById(R.id.tv_profile_phone);
        TextView tvShift = findViewById(R.id.tv_profile_shift);
        ImageView btnBack = findViewById(R.id.btnBack);

        // 3. Load Sample Data based on ID
        if ("STF001".equals(staffId)) {
            tvName.setText("Argus Filch");
            tvId.setText("ID: STF-001");
            tvEmail.setText("argus.filch@school.edu");
            if(tvPhone != null) tvPhone.setText("+44 7700 900461");
            if(tvShift != null) tvShift.setText("Night Shift (06:00 PM - 02:00 AM)");

        } else if ("STF002".equals(staffId)) {
            tvName.setText("Madam Pomfrey");
            tvId.setText("ID: STF-002");
            tvEmail.setText("poppy.pomfrey@school.edu");
            if(tvPhone != null) tvPhone.setText("+44 7700 900999");
            if(tvShift != null) tvShift.setText("Day Shift (08:00 AM - 04:00 PM)");

        } else if ("STF003".equals(staffId)) {
            tvName.setText("Rubeus Hagrid");
            tvId.setText("ID: STF-003");
            tvEmail.setText("hagrid@school.edu");
            if(tvPhone != null) tvPhone.setText("+44 7700 123456");
            if(tvShift != null) tvShift.setText("Full Day");

        } else {
            // FALLBACK PROFILE
            tvName.setText("Staff Member");
            tvId.setText("ID: " + (staffId.equals("DEFAULT") ? "STF-XXXX" : staffId));
            tvEmail.setText("staff@school.edu");
            if(tvPhone != null) tvPhone.setText("+1 (555) 000-0000");
            if(tvShift != null) tvShift.setText("Standard Shift");
        }

        // 4. Back Button Logic
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        } else {
            // Emergency fallback if finding ID fails
            android.view.View header = findViewById(R.id.header);
            if(header instanceof android.view.ViewGroup) {
                ((android.view.ViewGroup)header).getChildAt(0).setOnClickListener(v -> finish());
            }
        }

        // 5. Deactivate Button
        CardView btnDeactivate = findViewById(R.id.btn_delete_user);
        if(btnDeactivate != null) {
            btnDeactivate.setOnClickListener(v -> {
                Toast.makeText(this, "Staff account deactivated (Demo)", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}
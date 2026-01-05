package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminTeacherProfileActivity extends AppCompatActivity {

    private String teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_teacher_profile);

        // 1. Safe ID Retrieval
        if (getIntent().hasExtra("TEACHER_ID")) {
            teacherId = getIntent().getStringExtra("TEACHER_ID");
        } else {
            // FALLBACK: Don't close the app, just use a default ID
            teacherId = "DEFAULT";
        }

        // 2. Initialize Views
        TextView tvName = findViewById(R.id.tv_profile_name);
        TextView tvId = findViewById(R.id.tv_profile_id);
        TextView tvEmail = findViewById(R.id.tv_profile_email);
        TextView tvPhone = findViewById(R.id.tv_profile_phone);
        TextView tvQualification = findViewById(R.id.tv_profile_qualification);
        ImageView btnBack = findViewById(R.id.btnBack);

        // 3. Load Data
        if ("TCH001".equals(teacherId)) {
            tvName.setText("Mr. Walter White");
            tvId.setText("ID: TCH-2025-088");
            tvEmail.setText("walter.white@school.edu");
            if(tvPhone != null) tvPhone.setText("+1 (505) 555-0100");
            if(tvQualification != null) tvQualification.setText("M.Sc. Chemistry");

        } else if ("TCH002".equals(teacherId)) {
            tvName.setText("Ms. Valerie Frizzle");
            tvId.setText("ID: TCH-2025-099");
            tvEmail.setText("v.frizzle@school.edu");
            if(tvPhone != null) tvPhone.setText("+1 (555) 123-4567");
            if(tvQualification != null) tvQualification.setText("B.Ed, Science");

        } else {
            // DEFAULT / FALLBACK PROFILE
            tvName.setText("Teacher Name");
            tvId.setText("ID: " + (teacherId.equals("DEFAULT") ? "TCH-XXXX" : teacherId));
            tvEmail.setText("teacher@school.edu");
            if(tvPhone != null) tvPhone.setText("+1 (555) 000-0000");
            if(tvQualification != null) tvQualification.setText("PhD in Education");
        }

        // 4. Back Button Logic
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
        // Fallback if ID is different in XML
        if (btnBack == null) {
            android.view.View headerBack = findViewById(R.id.header);
            if(headerBack instanceof android.view.ViewGroup) {
                ((android.view.ViewGroup)headerBack).getChildAt(0).setOnClickListener(v -> finish());
            }
        }

        // 5. Deactivate Button
        CardView btnDeactivate = findViewById(R.id.btn_delete_user);
        if (btnDeactivate != null) {
            btnDeactivate.setOnClickListener(v -> {
                Toast.makeText(this, "Teacher account deactivated (Demo)", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}
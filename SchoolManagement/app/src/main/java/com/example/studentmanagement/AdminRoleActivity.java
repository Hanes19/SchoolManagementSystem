package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminRoleActivity extends AppCompatActivity {

    CardView cardStudents, cardTeachers, cardStaff;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_actions_userdirectory); // Error should be gone now

        // Initialize Views
        cardStudents = findViewById(R.id.cardStudents);
        cardTeachers = findViewById(R.id.cardTeachers);
        cardStaff = findViewById(R.id.cardStaff);
        btnBack = findViewById(R.id.btnBack);

        // --- CLICK LISTENERS ---

        btnBack.setOnClickListener(v -> finish()); // Go back to Dashboard

        cardStudents.setOnClickListener(v -> {
            // Future Step: Create AdminStudentListActivity and link it here
            Toast.makeText(this, "Opening Student Directory...", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, AdminStudentListActivity.class);
            // startActivity(intent);
        });

        cardTeachers.setOnClickListener(v -> {
            // Future Step: Create AdminTeacherListActivity
            Toast.makeText(this, "Opening Teacher Directory...", Toast.LENGTH_SHORT).show();
        });

        cardStaff.setOnClickListener(v -> {
            // Future Step: Create AdminStaffListActivity
            Toast.makeText(this, "Opening Staff Directory...", Toast.LENGTH_SHORT).show();
        });
    }
}
package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AddStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_directory_add_student); // Loads your uploaded XML

        // 1. Back Button Logic (Accessing the first icon in the header)
        LinearLayout header = findViewById(R.id.header);
        if (header != null && header.getChildCount() > 0) {
            header.getChildAt(0).setOnClickListener(v -> finish());
        }

        // 2. Register Button Logic
        CardView btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> {
            // Future: Add logic to save data to DatabaseHelper
            Toast.makeText(this, "Student Registration Logic goes here", Toast.LENGTH_SHORT).show();
            finish(); // Close form after registering
        });
    }
}
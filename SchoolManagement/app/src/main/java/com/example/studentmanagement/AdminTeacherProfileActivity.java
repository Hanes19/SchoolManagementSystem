package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminTeacherProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_teacher_profile);

        ImageView back = findViewById(R.id.btnBack); // Add this ID to your XML
        if(back != null) {
            back.setOnClickListener(v -> finish());
        }
    }
}
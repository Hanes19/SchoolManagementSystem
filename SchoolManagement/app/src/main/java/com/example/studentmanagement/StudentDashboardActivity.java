package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        // 1. Sample User Info
        TextView tvName = findViewById(R.id.tv_student_name);
        TextView tvClass = findViewById(R.id.tv_class_name);

        if (tvName != null) tvName.setText("Jason Statham");
        if (tvClass != null) tvClass.setText("Grade 12 - Diamond");

        // 2. Navigation
        setupCard(R.id.card_grades, StudentGradesActivity.class);
        setupCard(R.id.card_schedule, StudentScheduleActivity.class);
        setupCard(R.id.card_attendance, StudentAttendanceActivity.class);
        setupCard(R.id.card_fees, StudentFeesActivity.class);

        // 3. Back/Logout
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
    }

    private void setupCard(int id, Class<?> cls) {
        CardView card = findViewById(id);
        if (card != null) {
            card.setOnClickListener(v -> startActivity(new Intent(this, cls)));
        }
    }
}
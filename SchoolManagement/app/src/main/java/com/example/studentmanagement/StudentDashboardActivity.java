package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentDashboardActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvName, tvId;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        db = new DatabaseHelper(this);
        // In real app, get from Session/Intent. Mocking 'STU-001'.
        studentId = "STU-001";

        tvName = findViewById(R.id.tv_student_name);
        tvId = findViewById(R.id.tv_student_id);

        loadStudentInfo();
        setupNavigation();
    }

    private void loadStudentInfo() {
        // Fetch name from DB
        String name = db.getStudentName(studentId);
        tvName.setText(name != null ? name : "Welcome Student");
        tvId.setText("ID: " + studentId);
    }

    private void setupNavigation() {
        setNav(R.id.card_attendance, StudentAttendanceActivity.class);
        setNav(R.id.card_grades, StudentGradesActivity.class);
        setNav(R.id.card_fees, StudentFeesActivity.class);
        setNav(R.id.card_schedule, StudentScheduleActivity.class);

        // Profile or Settings if available
        // setNav(R.id.card_profile, StudentProfileActivity.class);
    }

    private void setNav(int id, Class<?> cls) {
        CardView card = findViewById(id);
        if (card != null) {
            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, cls);
                intent.putExtra("STUDENT_ID", studentId);
                startActivity(intent);
            });
        }
    }
}
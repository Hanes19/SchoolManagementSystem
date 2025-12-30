package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ParentDashboardActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvChildName;
    private String linkedChildId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_dashboard);

        db = new DatabaseHelper(this);
        // Assuming "parent01" is logged in. Get this from SessionManager in real app.
        linkedChildId = db.getLinkedChildId("parent01");

        tvChildName = findViewById(R.id.tv_child_name);
        if(tvChildName != null) {
            tvChildName.setText(db.getStudentName(linkedChildId));
        }

        // Navigation
        setupNav(R.id.card_attendance, ParentAttendanceActivity.class);
        setupNav(R.id.card_grades, ParentGradesActivity.class);
        setupNav(R.id.card_fees, ParentFeesActivity.class);
        // Reuse Student Schedule if layout is similar, or create ParentScheduleActivity
        // setupNav(R.id.card_schedule, StudentScheduleActivity.class);
    }

    private void setupNav(int id, Class<?> cls) {
        CardView card = findViewById(id);
        if (card != null) {
            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, cls);
                intent.putExtra("STUDENT_ID", linkedChildId);
                startActivity(intent);
            });
        }
    }
}
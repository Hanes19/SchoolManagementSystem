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
        setContentView(R.layout.parent_dashboard); // Links to your XML layout

        db = new DatabaseHelper(this);

        // 1. Identify the Child: In a real app, you'd get the parent ID from a session.
        // Here we simulate it or fetch a hardcoded link.
        linkedChildId = db.getLinkedChildId("parent01");

        // 2. Display Child's Name
        tvChildName = findViewById(R.id.tv_child_name);
        if(tvChildName != null) {
            String name = db.getStudentName(linkedChildId);
            tvChildName.setText(name != null ? name : "Unknown Student");
        }

        // 3. Setup Navigation Functions
        setupNavigation();
    }

    private void setupNavigation() {
        // Helper function to make code cleaner
        setNavListener(R.id.btn_attendance, ParentAttendanceActivity.class);
        setNavListener(R.id.card_grades, ParentGradesActivity.class);
        setNavListener(R.id.btn_fees_history, ParentFeesActivity.class);
        setNavListener(R.id.btn_contact_teacher, ParentMessageActivity.class);

        // Logout Function
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setNavListener(int cardId, Class<?> targetActivity) {
        CardView card = findViewById(cardId);
        if (card != null) {
            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, targetActivity);
                intent.putExtra("STUDENT_ID", linkedChildId); // Pass the child's ID to the next screen
                startActivity(intent);
            });
        }
    }
}
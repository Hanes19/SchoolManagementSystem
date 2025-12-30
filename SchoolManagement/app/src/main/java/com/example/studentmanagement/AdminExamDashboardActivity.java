package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminExamDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_exam); // Ensure this XML file exists

        setupNav(R.id.btn_schedule, AdminScheduleExamActivity.class); // You need to add IDs to admin_exam.xml cards
        setupNav(R.id.btn_admit_card, AdminAdmitCardActivity.class);
        setupNav(R.id.btn_question_bank, AdminQuestionBankActivity.class);
        setupNav(R.id.btn_marks_entry, AdminMarksEntryActivity.class);
        setupNav(R.id.btn_categories, AdminExamCategoriesActivity.class);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void setupNav(int id, Class<?> cls) {
        CardView card = findViewById(id);
        if (card != null) {
            card.setOnClickListener(v -> startActivity(new Intent(this, cls)));
        }
    }
}
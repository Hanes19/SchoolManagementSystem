package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminExamDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_exam_dashboard);

        // Header Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // --- Exam Management Section ---
        setupNavigation(R.id.card_exam_categories, AdminExamCategoriesActivity.class);
        setupNavigation(R.id.card_schedule_exam, AdminScheduleExamActivity.class);
        setupNavigation(R.id.card_question_bank, AdminQuestionBankActivity.class);
        setupNavigation(R.id.card_marks_entry, AdminMarksEntryActivity.class);
        setupNavigation(R.id.card_admit_cards, AdminAdmitCardActivity.class);

        // --- Reports & Analytics Section ---
        // Assuming AdminReportsGuideActivity is the placeholder for Reports, adjust if you have a specific GenerateReportActivity
        setupNavigation(R.id.card_generate_reports, AdminReportsGuideActivity.class);
        setupNavigation(R.id.card_performance_analytics, AdminAnalyticsActivity.class);
    }

    private void setupNavigation(int cardId, Class<?> destinationClass) {
        CardView card = findViewById(cardId);
        if (card != null) {
            card.setOnClickListener(v -> startActivity(new Intent(this, destinationClass)));
        }
    }
}
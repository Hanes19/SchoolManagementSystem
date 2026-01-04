package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminExamDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This links to your provided XML layout
        setContentView(R.layout.admin_exam_dashboard);

        // 1. Setup Header Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Initialize Cards
        // Ensure these IDs match what is in your admin_exam_dashboard.xml
        CardView cardCategories = findViewById(R.id.card_exam_categories);
        CardView cardSchedule = findViewById(R.id.card_schedule_exam);
        CardView cardMarks = findViewById(R.id.card_marks_entry);
        CardView cardResults = findViewById(R.id.card_performance_analytics); // Checked against your snippet

        // 3. Set Click Listeners (with null safety checks)
        if (cardCategories != null) {
            cardCategories.setOnClickListener(v ->
                    startActivity(new Intent(this, AdminExamCategoriesActivity.class)));
        } else {
            // Debugging aid: remove this in production
            // Toast.makeText(this, "Error: Categories Card ID not found in XML", Toast.LENGTH_SHORT).show();
        }

        if (cardSchedule != null) {
            cardSchedule.setOnClickListener(v ->
                    startActivity(new Intent(this, AdminScheduleExamActivity.class)));
        }

        if (cardMarks != null) {
            cardMarks.setOnClickListener(v ->
                    startActivity(new Intent(this, AdminMarksEntryActivity.class)));
        }

        if (cardResults != null) {
            cardResults.setOnClickListener(v ->
                    startActivity(new Intent(this, AdminAnalyticsActivity.class))); // Linking to Analytics
        }
    }
}
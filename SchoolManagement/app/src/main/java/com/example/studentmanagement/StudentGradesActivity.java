package com.example.studentmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentGradesActivity extends AppCompatActivity {

    private LinearLayout gradesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_grades);

        setupHeader();

        // Find the specific container defined in your XML
        gradesContainer = findViewById(R.id.ll_grades_list);

        loadSampleGrades();
    }

    private void setupHeader() {
        View backBtn = findViewById(R.id.btn_back_grades);
        if (backBtn != null) backBtn.setOnClickListener(v -> finish());
    }

    private void loadSampleGrades() {
        if (gradesContainer == null) return;
        gradesContainer.removeAllViews();

        // --- SAMPLE DATA ---
        addGradeCard("Mathematics", "A", "95", "4.0");
        addGradeCard("Physics", "B+", "88", "3.5");
        addGradeCard("Chemistry", "A-", "91", "3.7");
        addGradeCard("History", "B", "85", "3.0");
        addGradeCard("English Lit", "A", "98", "4.0");
        addGradeCard("Computer Science", "A+", "100", "4.0");
    }

    private void addGradeCard(String subject, String grade, String score, String credits) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);
        card.setRadius(24);
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(4);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(32, 32, 32, 32);
        inner.setGravity(Gravity.CENTER_VERTICAL);

        // Icon
        ImageView icon = new ImageView(this);
        icon.setImageResource(android.R.drawable.star_on);
        icon.setColorFilter(Color.parseColor("#FF9800"));
        icon.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        inner.addView(icon);

        // Text Details
        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(32, 0, 0, 0);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvSubject = new TextView(this);
        tvSubject.setText(subject);
        tvSubject.setTextSize(18);
        tvSubject.setTextColor(Color.parseColor("#1B254B"));
        tvSubject.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvScore = new TextView(this);
        tvScore.setText("Score: " + score + " | Credits: " + credits);
        tvScore.setTextSize(14);
        tvScore.setTextColor(Color.parseColor("#A3AED0"));

        textLayout.addView(tvSubject);
        textLayout.addView(tvScore);
        inner.addView(textLayout);

        // Grade Badge
        TextView tvGrade = new TextView(this);
        tvGrade.setText(grade);
        tvGrade.setTextSize(20);
        tvGrade.setTypeface(null, android.graphics.Typeface.BOLD);

        // Color code the grade
        if(grade.startsWith("A")) tvGrade.setTextColor(Color.parseColor("#4CAF50")); // Green
        else if(grade.startsWith("B")) tvGrade.setTextColor(Color.parseColor("#2196F3")); // Blue
        else if(grade.startsWith("F")) tvGrade.setTextColor(Color.parseColor("#F44336")); // Red
        else tvGrade.setTextColor(Color.parseColor("#FF9800")); // Orange

        inner.addView(tvGrade);
        card.addView(inner);

        gradesContainer.addView(card);
    }
}
package com.example.studentmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentGradesActivity extends AppCompatActivity {

    private LinearLayout gradesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_grades);

        // 1. Setup Header (Back Button & Title)
        setupHeader();

        // 2. Find the container for the list items
        setupContainer();

        // 3. Populate with Sample Data
        loadSampleGrades();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findHeader(root);

        if (header != null) {
            // Usually the first child is the Back Button
            if (header.getChildCount() > 0) {
                header.getChildAt(0).setOnClickListener(v -> finish());
            }
            // Usually the second child (or middle one) is the Title Text
            // We can iterate to find a TextView to set the title
            for(int i=0; i<header.getChildCount(); i++) {
                if(header.getChildAt(i) instanceof TextView) {
                    ((TextView) header.getChildAt(i)).setText("My Grades");
                    break;
                }
            }
        }
    }

    private void setupContainer() {
        // Find the LinearLayout inside the ScrollView to add items to
        ViewGroup root = findViewById(android.R.id.content);
        ScrollView scrollView = findScrollView(root);

        if (scrollView != null && scrollView.getChildCount() > 0) {
            View child = scrollView.getChildAt(0);
            if (child instanceof LinearLayout) {
                gradesContainer = (LinearLayout) child;
            }
        }
    }

    private void loadSampleGrades() {
        if (gradesContainer == null) return;

        // Clear existing items (except the "Current Term" title if it exists)
        // For safety in this demo, we'll just append.

        addGradeCard("Mathematics", "A", "95", "4.0");
        addGradeCard("Physics", "B+", "88", "3.5");
        addGradeCard("Chemistry", "A-", "91", "3.7");
        addGradeCard("History", "B", "85", "3.0");
        addGradeCard("English Lit", "A", "98", "4.0");

        // Add GPA Summary at the bottom
        addGpaSummary("3.64");
    }

    private void addGradeCard(String subject, String grade, String score, String credits) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24); // Bottom margin
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
        icon.setImageResource(android.R.drawable.star_on); // Generic star icon
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
        tvScore.setText("Score: " + score + "% | Credits: " + credits);
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
        tvGrade.setTextColor(Color.parseColor("#4CAF50")); // Green

        inner.addView(tvGrade);
        card.addView(inner);

        gradesContainer.addView(card);
    }

    private void addGpaSummary(String gpa) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 40);
        card.setLayoutParams(params);
        card.setRadius(24);
        card.setCardBackgroundColor(Color.parseColor("#4361EE")); // Blue background

        TextView tv = new TextView(this);
        tv.setText("Overall GPA: " + gpa);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(32, 32, 32, 32);

        card.addView(tv);
        gradesContainer.addView(card);
    }

    // --- Helpers to find views ---

    private LinearLayout findHeader(View view) {
        if (view instanceof LinearLayout) {
            // Simple heuristic: headers usually have horizontal orientation and top gravity
            return (LinearLayout) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                // Look for the first LinearLayout that isn't the main container
                if (child instanceof LinearLayout) return (LinearLayout) child;

                LinearLayout result = findHeader(child);
                if (result != null) return result;
            }
        }
        return null;
    }

    private ScrollView findScrollView(View view) {
        if (view instanceof ScrollView) {
            return (ScrollView) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                ScrollView result = findScrollView(group.getChildAt(i));
                if (result != null) return result;
            }
        }
        return null;
    }
}
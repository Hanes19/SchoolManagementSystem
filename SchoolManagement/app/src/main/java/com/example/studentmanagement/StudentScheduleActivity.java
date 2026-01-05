package com.example.studentmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentScheduleActivity extends AppCompatActivity {

    private LinearLayout scheduleContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_schedule);

        setupHeader();
        findContainer();
        loadSampleSchedule();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findHeaderLayout(root);
        if (header != null) {
            if (header.getChildCount() > 0) {
                header.getChildAt(0).setOnClickListener(v -> finish());
            }
            for (int i = 0; i < header.getChildCount(); i++) {
                if (header.getChildAt(i) instanceof TextView) {
                    ((TextView) header.getChildAt(i)).setText("Class Schedule");
                    break;
                }
            }
        }
    }

    private void findContainer() {
        ViewGroup root = findViewById(android.R.id.content);
        ScrollView scrollView = findScrollView(root);
        if (scrollView != null && scrollView.getChildCount() > 0) {
            if (scrollView.getChildAt(0) instanceof LinearLayout) {
                scheduleContainer = (LinearLayout) scrollView.getChildAt(0);
            }
        }
    }

    private void loadSampleSchedule() {
        if (scheduleContainer == null) return;

        addScheduleItem("08:00 - 09:30", "Mathematics", "Mr. Smith", "Room 101", "#4CAF50");
        addScheduleItem("09:45 - 11:15", "Physics", "Ms. Frizzle", "Lab 2", "#2196F3");
        addScheduleItem("11:15 - 12:00", "Lunch Break", "", "Cafeteria", "#9E9E9E");
        addScheduleItem("13:00 - 14:30", "History", "Mr. Binns", "Room 204", "#FF9800");
        addScheduleItem("14:45 - 16:15", "English Lit", "Mr. Keating", "Room 105", "#673AB7");
    }

    private void addScheduleItem(String time, String subject, String teacher, String room, String colorHex) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);
        card.setRadius(20);
        card.setCardElevation(2);
        card.setCardBackgroundColor(Color.WHITE);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(0, 0, 0, 0); // Padding handled by internal views

        // Color Strip
        View strip = new View(this);
        strip.setLayoutParams(new LinearLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT));
        strip.setBackgroundColor(Color.parseColor(colorHex));
        inner.addView(strip);

        // Content
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(32, 24, 32, 24);

        TextView tvTime = new TextView(this);
        tvTime.setText(time);
        tvTime.setTextColor(Color.parseColor(colorHex));
        tvTime.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvSubject = new TextView(this);
        tvSubject.setText(subject);
        tvSubject.setTextSize(18);
        tvSubject.setTextColor(Color.parseColor("#1B254B"));
        tvSubject.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvDetails = new TextView(this);
        tvDetails.setText(teacher + (teacher.isEmpty() ? "" : " • ") + room);
        tvDetails.setTextColor(Color.GRAY);
        tvDetails.setTextSize(14);

        content.addView(tvTime);
        content.addView(tvSubject);
        content.addView(tvDetails);

        inner.addView(content);
        card.addView(inner);
        scheduleContainer.addView(card);
    }

    // --- Helpers ---
    private LinearLayout findHeaderLayout(View view) {
        if (view instanceof LinearLayout) return (LinearLayout) view;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                LinearLayout res = findHeaderLayout(group.getChildAt(i));
                if (res != null) return res;
            }
        }
        return null;
    }

    private ScrollView findScrollView(View view) {
        if (view instanceof ScrollView) return (ScrollView) view;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                ScrollView res = findScrollView(group.getChildAt(i));
                if (res != null) return res;
            }
        }
        return null;
    }
}
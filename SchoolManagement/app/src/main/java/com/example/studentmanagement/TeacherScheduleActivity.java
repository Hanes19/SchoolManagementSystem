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

public class TeacherScheduleActivity extends AppCompatActivity {

    private LinearLayout scheduleContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_schedule);

        // 1. Setup Header (Back Button & Title)
        setupHeader();

        // 2. Find the container to hold the list of classes
        findContainer();

        // 3. Load Sample Data
        loadSampleSchedule();
    }

    private void setupHeader() {
        // Find the top header Layout
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findHeaderLayout(root);

        if (header != null) {
            // Setup Back Button (usually first child)
            if (header.getChildCount() > 0) {
                header.getChildAt(0).setOnClickListener(v -> finish());
            }
            // Setup Title (usually second child)
            if (header.getChildCount() > 1 && header.getChildAt(1) instanceof TextView) {
                ((TextView) header.getChildAt(1)).setText("My Schedule");
            } else if (header.getChildCount() > 2 && header.getChildAt(2) instanceof TextView) {
                // Sometimes there's a spacer view in between
                ((TextView) header.getChildAt(2)).setText("My Schedule");
            }
        }
    }

    private void findContainer() {
        // Look for the main ScrollView and its child LinearLayout
        ViewGroup root = findViewById(android.R.id.content);
        ScrollView scrollView = findScrollView(root);

        if (scrollView != null && scrollView.getChildCount() > 0) {
            View child = scrollView.getChildAt(0);
            if (child instanceof LinearLayout) {
                scheduleContainer = (LinearLayout) child;
            }
        }
    }

    private void loadSampleSchedule() {
        if (scheduleContainer == null) return;

        // Clear only if you want to remove hardcoded XML placeholder items
        // scheduleContainer.removeAllViews();

        addScheduleCard("08:00 AM - 09:30 AM", "Mathematics", "Grade 10 - Emerald", "Room 302", "Ongoing");
        addScheduleCard("10:00 AM - 11:30 AM", "Physics", "Grade 11 - Ruby", "Lab 1", "Upcoming");
        addScheduleCard("01:00 PM - 02:30 PM", "Chemistry", "Grade 12 - Diamond", "Lab 2", "Upcoming");
        addScheduleCard("03:00 PM - 04:30 PM", "Consultation", "Faculty Room", "", "Pending");
    }

    private void addScheduleCard(String time, String subject, String className, String room, String status) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 30);
        card.setLayoutParams(params);
        card.setRadius(24);
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(4);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(40, 40, 40, 40);

        // Row 1: Time & Status
        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);

        TextView tvTime = new TextView(this);
        tvTime.setText(time);
        tvTime.setTextColor(Color.parseColor("#4361EE")); // Blue
        tvTime.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTime.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));

        TextView tvStatus = new TextView(this);
        tvStatus.setText(status);
        tvStatus.setTextSize(12);
        tvStatus.setTextColor(status.equals("Ongoing") ? Color.parseColor("#4CAF50") : Color.GRAY);
        tvStatus.setBackgroundColor(status.equals("Ongoing") ? Color.parseColor("#E8F5E9") : Color.parseColor("#F5F5F5"));
        tvStatus.setPadding(16, 8, 16, 8);

        row1.addView(tvTime);
        row1.addView(tvStatus);
        inner.addView(row1);

        // Subject Title
        TextView tvSubject = new TextView(this);
        tvSubject.setText(subject);
        tvSubject.setTextSize(20);
        tvSubject.setTextColor(Color.parseColor("#1B254B"));
        tvSubject.setTypeface(null, android.graphics.Typeface.BOLD);
        tvSubject.setPadding(0, 16, 0, 4);
        inner.addView(tvSubject);

        // Class & Room
        TextView tvDetails = new TextView(this);
        tvDetails.setText(className + (room.isEmpty() ? "" : " • " + room));
        tvDetails.setTextColor(Color.parseColor("#A3AED0"));
        tvDetails.setTextSize(14);
        inner.addView(tvDetails);

        card.addView(inner);
        scheduleContainer.addView(card);
    }

    // --- Helpers to find layout elements dynamically ---

    private LinearLayout findHeaderLayout(View view) {
        if (view instanceof LinearLayout) {
            // Assume the first horizontal LL with 3+ children or typical header height is the header
            return (LinearLayout) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof LinearLayout) return (LinearLayout) child; // Return first found

                LinearLayout result = findHeaderLayout(child);
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
package com.example.studentmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentAttendanceActivity extends AppCompatActivity {

    private LinearLayout listContainer;
    private TextView tvPercentage, tvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_attendance);

        setupHeader();
        findContainer();
        loadSampleAttendance();
    }

    private void setupHeader() {
        View back = findViewById(R.id.btn_back_attendance);
        if(back != null) back.setOnClickListener(v -> finish());

        tvPercentage = findViewById(R.id.tv_attendance_percentage);
        tvSummary = findViewById(R.id.tv_attendance_summary);
    }

    private void findContainer() {
        ViewGroup root = findViewById(android.R.id.content);
        findScrollViewRecursive(root);
    }

    private void findScrollViewRecursive(View view) {
        if (view instanceof android.widget.ScrollView) {
            android.widget.ScrollView sv = (android.widget.ScrollView) view;
            if (sv.getChildCount() > 0 && sv.getChildAt(0) instanceof LinearLayout) {
                listContainer = (LinearLayout) sv.getChildAt(0);
            }
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                findScrollViewRecursive(group.getChildAt(i));
                if (listContainer != null) return;
            }
        }
    }

    private void loadSampleAttendance() {
        if (listContainer == null) return;

        // --- SAMPLE HEADER ---
        TextView header = new TextView(this);
        header.setText("October 2025");
        header.setTextSize(18);
        header.setPadding(0, 40, 0, 20);
        header.setTextColor(Color.parseColor("#1B254B"));
        header.setTypeface(null, android.graphics.Typeface.BOLD);
        listContainer.addView(header);

        // --- SAMPLE DATA ---
        addAttendanceRow("Oct 25, 2025", "Present", "#4CAF50");
        addAttendanceRow("Oct 24, 2025", "Present", "#4CAF50");
        addAttendanceRow("Oct 23, 2025", "Absent", "#F44336");
        addAttendanceRow("Oct 22, 2025", "Present", "#4CAF50");
        addAttendanceRow("Oct 21, 2025", "Late", "#FF9800");

        TextView header2 = new TextView(this);
        header2.setText("September 2025");
        header2.setTextSize(18);
        header2.setPadding(0, 40, 0, 20);
        header2.setTextColor(Color.parseColor("#1B254B"));
        header2.setTypeface(null, android.graphics.Typeface.BOLD);
        listContainer.addView(header2);

        addAttendanceRow("Sep 30, 2025", "Present", "#4CAF50");
        addAttendanceRow("Sep 29, 2025", "Present", "#4CAF50");

        // Hardcoded Stats for the sample
        if(tvPercentage != null) tvPercentage.setText("92%");
        if(tvSummary != null) tvSummary.setText("22 Present out of 24 days");
    }

    private void addAttendanceRow(String date, String status, String colorHex) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 8, 0, 8);
        card.setLayoutParams(params);
        card.setCardElevation(2);
        card.setRadius(16);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(32, 24, 32, 24);
        inner.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvDate = new TextView(this);
        tvDate.setText(date);
        tvDate.setTextSize(14);
        tvDate.setTextColor(Color.parseColor("#1B254B"));
        tvDate.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1f));

        TextView tvStatus = new TextView(this);
        tvStatus.setText(status);
        tvStatus.setTextColor(Color.parseColor(colorHex));
        tvStatus.setTypeface(null, android.graphics.Typeface.BOLD);

        inner.addView(tvDate);
        inner.addView(tvStatus);
        card.addView(inner);
        listContainer.addView(card);
    }
}
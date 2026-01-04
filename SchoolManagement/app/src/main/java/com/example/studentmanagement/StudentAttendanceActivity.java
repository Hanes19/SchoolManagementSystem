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

public class StudentAttendanceActivity extends AppCompatActivity {

    private LinearLayout listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_attendance);

        setupHeader();
        findContainer();
        loadSampleData();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findHeaderLayout(root);
        if (header != null && header.getChildCount() > 0) {
            header.getChildAt(0).setOnClickListener(v -> finish());
            // Try to set title
            for(int i=0; i<header.getChildCount(); i++){
                if(header.getChildAt(i) instanceof TextView){
                    ((TextView)header.getChildAt(i)).setText("Attendance History");
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
                listContainer = (LinearLayout) scrollView.getChildAt(0);
            }
        }
    }

    private void loadSampleData() {
        if (listContainer == null) return;

        // Add Month Header
        addMonthHeader("October 2023");
        addAttendanceRow("Oct 25, 2023", "Present", "#4CAF50");
        addAttendanceRow("Oct 24, 2023", "Present", "#4CAF50");
        addAttendanceRow("Oct 23, 2023", "Absent", "#F44336");
        addAttendanceRow("Oct 22, 2023", "Present", "#4CAF50");
        addAttendanceRow("Oct 21, 2023", "Weekend", "#9E9E9E");

        addMonthHeader("September 2023");
        addAttendanceRow("Sep 30, 2023", "Late", "#FF9800");
        addAttendanceRow("Sep 29, 2023", "Present", "#4CAF50");
    }

    private void addMonthHeader(String month) {
        TextView tv = new TextView(this);
        tv.setText(month);
        tv.setTextSize(16);
        tv.setPadding(16, 32, 16, 16);
        tv.setTextColor(Color.parseColor("#1B254B"));
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        listContainer.addView(tv);
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
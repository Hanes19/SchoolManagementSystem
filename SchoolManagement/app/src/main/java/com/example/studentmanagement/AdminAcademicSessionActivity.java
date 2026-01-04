package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminAcademicSessionActivity extends AppCompatActivity {

    private LinearLayout container;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_academic_session);

        dbHelper = new DatabaseHelper(this);
        container = findViewById(R.id.session_container);

        // 1. Back Button
        LinearLayout header = findViewById(R.id.header);
        header.setOnClickListener(v -> finish());

        // 2. Add Session FAB
        CardView btnAddSession = findViewById(R.id.btn_add_session);
        btnAddSession.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminAddAcademicSessionActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSessions(); // Refresh list when returning from Add Activity
    }

    private void loadSessions() {
        container.removeAllViews();
        Cursor cursor = dbHelper.getAllSessions();

        if (cursor != null && cursor.moveToFirst()) {
            boolean hasActive = false;
            boolean hasHistory = false;

            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("session_name"));
                String start = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
                String end = cursor.getString(cursor.getColumnIndexOrThrow("end_date"));
                int isActive = cursor.getInt(cursor.getColumnIndexOrThrow("is_active"));

                if (isActive == 1) {
                    // Add Header if it's the first active one
                    if (!hasActive) {
                        addHeader("CURRENT SESSION");
                        hasActive = true;
                    }
                    addActiveSessionCard(name, start, end);
                } else {
                    // Add Header if it's the first history one
                    if (!hasHistory) {
                        addHeader("HISTORY");
                        hasHistory = true;
                    }
                    addHistorySessionCard(name);
                }

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            // No sessions found
            TextView emptyView = new TextView(this);
            emptyView.setText("No academic sessions found.");
            emptyView.setPadding(20, 20, 20, 20);
            container.addView(emptyView);
        }
    }

    private void addHeader(String text) {
        TextView header = new TextView(this);
        header.setText(text);
        header.setTextColor(Color.parseColor("#111C44"));
        header.setTextSize(12);
        header.setTypeface(null, android.graphics.Typeface.BOLD);
        header.setLetterSpacing(0.1f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 30, 0, 20);
        header.setLayoutParams(params);
        container.addView(header);
    }

    private void addActiveSessionCard(String name, String start, String end) {
        // Create CardView
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);
        card.setCardBackgroundColor(Color.parseColor("#4318FF"));
        card.setRadius(40);
        card.setCardElevation(10);

        // Content Layout
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(60, 60, 60, 60);
        content.setBackgroundColor(Color.parseColor("#4318FF")); // Match card bg

        // Top Row (Year + Active Badge)
        LinearLayout topRow = new LinearLayout(this);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        topRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextColor(Color.WHITE);
        tvName.setTextSize(20);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        tvName.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView tvBadge = new TextView(this);
        tvBadge.setText("ACTIVE");
        tvBadge.setTextColor(Color.parseColor("#4318FF"));
        tvBadge.setBackgroundResource(R.drawable.rounded_corner_white_bg);
        tvBadge.setPadding(30, 10, 30, 10);
        tvBadge.setTextSize(12);
        tvBadge.setTypeface(null, android.graphics.Typeface.BOLD);

        topRow.addView(tvName);
        topRow.addView(tvBadge);

        // Date Text
        TextView tvDates = new TextView(this);
        tvDates.setText("Starts: " + start + " • Ends: " + end);
        tvDates.setTextColor(Color.parseColor("#E0E0E0"));
        tvDates.setTextSize(14);
        tvDates.setPadding(0, 20, 0, 0);

        content.addView(topRow);
        content.addView(tvDates);
        card.addView(content);
        container.addView(card);
    }

    private void addHistorySessionCard(String name) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);
        card.setCardBackgroundColor(Color.WHITE);
        card.setRadius(40);
        card.setCardElevation(0);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.HORIZONTAL);
        content.setGravity(Gravity.CENTER_VERTICAL);
        content.setPadding(40, 40, 40, 40);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextColor(Color.parseColor("#1B254B"));
        tvName.setTextSize(16);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvStatus = new TextView(this);
        tvStatus.setText("Closed");
        tvStatus.setTextColor(Color.parseColor("#A3AED0"));
        tvStatus.setTextSize(12);

        textLayout.addView(tvName);
        textLayout.addView(tvStatus);
        content.addView(textLayout);
        card.addView(content);
        container.addView(card);
    }
}
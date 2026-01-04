package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
                // [CHANGED] Get ID column
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("session_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("session_name"));
                String start = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
                String end = cursor.getString(cursor.getColumnIndexOrThrow("end_date"));
                int isActive = cursor.getInt(cursor.getColumnIndexOrThrow("is_active"));

                if (isActive == 1) {
                    if (!hasActive) {
                        addHeader("CURRENT SESSION");
                        hasActive = true;
                    }
                    // [CHANGED] Pass ID
                    addActiveSessionCard(id, name, start, end);
                } else {
                    if (!hasHistory) {
                        addHeader("HISTORY");
                        hasHistory = true;
                    }
                    // [CHANGED] Pass ID, start, end
                    addHistorySessionCard(id, name, start, end);
                }

            } while (cursor.moveToNext());
            cursor.close();
        } else {
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

    // [CHANGED] Added ID parameter and Edit logic
    private void addActiveSessionCard(int id, String name, String start, String end) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);
        card.setCardBackgroundColor(Color.parseColor("#4318FF"));
        card.setRadius(40);
        card.setCardElevation(10);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(60, 60, 60, 60);
        content.setBackgroundColor(Color.parseColor("#4318FF"));

        // Top Row
        LinearLayout topRow = new LinearLayout(this);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        topRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextColor(Color.WHITE);
        tvName.setTextSize(20);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        tvName.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        // Active Badge
        TextView tvBadge = new TextView(this);
        tvBadge.setText("ACTIVE");
        tvBadge.setTextColor(Color.parseColor("#4318FF"));
        tvBadge.setBackgroundResource(R.drawable.rounded_corner_white_bg);
        tvBadge.setPadding(30, 10, 30, 10);
        tvBadge.setTextSize(12);
        tvBadge.setTypeface(null, android.graphics.Typeface.BOLD);

        // [NEW] Edit Icon for Active Card (White to match theme)
        ImageView btnEdit = new ImageView(this);
        btnEdit.setImageResource(android.R.drawable.ic_menu_edit); // Use system edit icon
        btnEdit.setColorFilter(Color.WHITE); // Make it white
        btnEdit.setPadding(20, 0, 0, 0);
        btnEdit.setOnClickListener(v -> openEditActivity(id, name, start, end));

        topRow.addView(tvName);
        topRow.addView(tvBadge);
        topRow.addView(btnEdit); // Add edit button

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

    // [CHANGED] Added ID, start, end params and Edit logic
    private void addHistorySessionCard(int id, String name, String start, String end) {
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
        tvStatus.setText("Closed • " + start + " to " + end);
        tvStatus.setTextColor(Color.parseColor("#A3AED0"));
        tvStatus.setTextSize(12);

        textLayout.addView(tvName);
        textLayout.addView(tvStatus);

        // [NEW] Edit Icon for History Card (Dark blue)
        ImageView btnEdit = new ImageView(this);
        btnEdit.setImageResource(android.R.drawable.ic_menu_edit);
        btnEdit.setColorFilter(Color.parseColor("#1B254B"));
        btnEdit.setPadding(20, 20, 20, 20);
        btnEdit.setOnClickListener(v -> openEditActivity(id, name, start, end));

        content.addView(textLayout);
        content.addView(btnEdit); // Add edit button
        card.addView(content);
        container.addView(card);
    }

    // [NEW] Helper to open Edit Screen
    private void openEditActivity(int id, String name, String start, String end) {
        Intent intent = new Intent(this, AdminAddAcademicSessionActivity.class);
        intent.putExtra("SESSION_ID", String.valueOf(id));
        intent.putExtra("SESSION_NAME", name);
        intent.putExtra("START_DATE", start);
        intent.putExtra("END_DATE", end);
        startActivity(intent);
    }
}
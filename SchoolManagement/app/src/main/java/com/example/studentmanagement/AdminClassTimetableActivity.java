package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminClassTimetableActivity extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout container;
    String classId;
    String selectedDay = "Monday"; // Default day

    // UI References for Tabs
    CardView[] dayButtons;
    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_class_timetable);

        db = new DatabaseHelper(this);
        container = findViewById(R.id.timetable_container);

        // Get Class Info
        classId = getIntent().getStringExtra("CLASS_ID");
        String className = getIntent().getStringExtra("CLASS_NAME");

        TextView title = findViewById(R.id.tv_class_title);
        if(className != null) title.setText(className);

        findViewById(R.id.header).setOnClickListener(v -> finish());

        setupDayTabs();
        loadTimetable();
    }

    private void setupDayTabs() {
        // Map IDs to Array
        dayButtons = new CardView[]{
                findViewById(R.id.btn_day_mon),
                findViewById(R.id.btn_day_tue),
                findViewById(R.id.btn_day_wed),
                findViewById(R.id.btn_day_thu),
                findViewById(R.id.btn_day_fri)
        };

        // Add Listeners
        for (int i = 0; i < dayButtons.length; i++) {
            final int index = i;
            dayButtons[i].setOnClickListener(v -> {
                selectedDay = days[index];
                updateTabColors(index);
                loadTimetable();
            });
        }
    }

    private void updateTabColors(int activeIndex) {
        for (int i = 0; i < dayButtons.length; i++) {
            LinearLayout innerLayout = (LinearLayout) dayButtons[i].getChildAt(0);
            TextView text = (TextView) innerLayout.getChildAt(0);

            if (i == activeIndex) {
                // Active: Dark Blue Background, White Text
                dayButtons[i].setCardBackgroundColor(Color.parseColor("#111C44"));
                dayButtons[i].setCardElevation(4);
                text.setTextColor(Color.WHITE);
            } else {
                // Inactive: White Background, Gray Text
                dayButtons[i].setCardBackgroundColor(Color.WHITE);
                dayButtons[i].setCardElevation(0);
                text.setTextColor(Color.parseColor("#A3AED0"));
            }
        }
    }

    private void loadTimetable() {
        container.removeAllViews(); // Clear old list

        if (classId == null) {
            Toast.makeText(this, "Error: No Class ID found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use the helper method provided in the previous step
        Cursor cursor = db.getClassSchedule(classId, selectedDay);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // 1. Get Data from Cursor
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String teacher = cursor.getString(cursor.getColumnIndexOrThrow("teacher_name"));
                String room = cursor.getString(cursor.getColumnIndexOrThrow("room"));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));

                // 2. Inflate Row
                View view = getLayoutInflater().inflate(R.layout.item_schedule_class_row, container, false);

                // 3. Bind Data
                TextView tvSubject = view.findViewById(R.id.tv_schedule_subject);
                TextView tvDesc = view.findViewById(R.id.tv_schedule_location);
                TextView tvTime = view.findViewById(R.id.tv_schedule_time);
                TextView tvMeridiem = view.findViewById(R.id.tv_schedule_meridiem);
                View indicator = view.findViewById(R.id.view_status_indicator);

                tvSubject.setText(subject);
                tvDesc.setText(room + " • " + teacher);

                // Format Time (e.g., 08:30 -> 08:30 AM)
                try {
                    String[] parts = startTime.split(":");
                    int h = Integer.parseInt(parts[0]);
                    String m = parts[1];
                    String ampm = (h >= 12) ? "PM" : "AM";
                    if (h > 12) h -= 12;
                    tvTime.setText(String.format("%02d:%s", h, m));
                    tvMeridiem.setText(ampm);
                } catch (Exception e) {
                    tvTime.setText(startTime);
                }

                // Random Color Strip for aesthetics
                int[] colors = {Color.parseColor("#4318FF"), Color.parseColor("#FFB547"), Color.parseColor("#05CD99")};
                indicator.setBackgroundColor(colors[Math.abs(subject.hashCode()) % 3]);

                container.addView(view);

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            // Show "No Classes" message if empty
            TextView emptyMsg = new TextView(this);
            emptyMsg.setText("No classes scheduled for " + selectedDay);
            emptyMsg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            emptyMsg.setPadding(0, 50, 0, 0);
            emptyMsg.setTextColor(Color.GRAY);
            container.addView(emptyMsg);
        }
    }
}
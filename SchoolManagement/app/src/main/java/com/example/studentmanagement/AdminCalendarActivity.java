package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_calendar);

        // --- Header Actions ---
        ImageView btnBack = findViewById(R.id.btn_back); // Add this ID to your XML
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // --- Calendar Logic ---
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Note: Month is 0-indexed (0 = Jan, 11 = Dec)
            String selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
            Toast.makeText(AdminCalendarActivity.this, "Selected: " + selectedDate, Toast.LENGTH_SHORT).show();
            // TODO: Filter the event list below based on this date
        });

        // --- Add Event Button ---
        CardView btnAddEvent = findViewById(R.id.btn_add_event);
        btnAddEvent.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCalendarActivity.this, AdminAddEventActivity.class);
            startActivity(intent);
        });
    }
}
package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AdminAttendanceActivity extends AppCompatActivity {

    private TextView tvDate;
    private Calendar currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_attendance_sheet);

        // Initialize Calendar to today
        currentCalendar = Calendar.getInstance();

        // 1. Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Date Setup & Navigation
        tvDate = findViewById(R.id.tv_date);
        updateDateDisplay(); // Show initial date

        View btnPrevDate = findViewById(R.id.btn_prev_date);
        if (btnPrevDate != null) {
            btnPrevDate.setOnClickListener(v -> {
                // Move back 1 day
                currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
                updateDateDisplay();
            });
        }

        View btnNextDate = findViewById(R.id.btn_next_date);
        if (btnNextDate != null) {
            btnNextDate.setOnClickListener(v -> {
                // Move forward 1 day
                currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
                updateDateDisplay();
            });
        }

        // 3. Save Button
        View btnSave = findViewById(R.id.btn_save);
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                Toast.makeText(this, "Attendance Saved for " + tvDate.getText(), Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }

    private void updateDateDisplay() {
        if (tvDate != null) {
            // Format: "Wed, Oct 24 2026"
            String dateText = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault()).format(currentCalendar.getTime());
            tvDate.setText(dateText);
        }
    }
}
package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.Calendar;

public class AdminAddAcademicSessionActivity extends AppCompatActivity {

    private EditText etSessionName;
    private TextView tvStartDate, tvEndDate;
    private Switch switchActive;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_academic_session);

        dbHelper = new DatabaseHelper(this);

        // Bind Views
        etSessionName = findViewById(R.id.et_session_name);
        tvStartDate = findViewById(R.id.tv_start_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        switchActive = findViewById(R.id.switch_active);

        LinearLayout btnPickStart = findViewById(R.id.btn_pick_start);
        LinearLayout btnPickEnd = findViewById(R.id.btn_pick_end);
        CardView btnCreate = findViewById(R.id.btn_create_container);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Date Pickers
        btnPickStart.setOnClickListener(v -> showDatePicker(tvStartDate));
        btnPickEnd.setOnClickListener(v -> showDatePicker(tvEndDate));

        // Create Button Logic
        btnCreate.setOnClickListener(v -> saveSession());
    }

    private void showDatePicker(TextView targetView) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    // Format: YYYY-MM-DD
                    String date = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    targetView.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveSession() {
        String name = etSessionName.getText().toString().trim();
        String start = tvStartDate.getText().toString();
        String end = tvEndDate.getText().toString();
        boolean isActive = switchActive.isChecked();

        if (name.isEmpty() || start.equals("Select Date") || end.equals("Select Date")) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.addAcademicSession(name, start, end, isActive)) {
            Toast.makeText(this, "Session Created Successfully", Toast.LENGTH_SHORT).show();
            finish(); // Go back to the list
        } else {
            Toast.makeText(this, "Error creating session", Toast.LENGTH_SHORT).show();
        }
    }
}
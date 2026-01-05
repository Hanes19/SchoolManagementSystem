package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Locale;

public class StaffLeaveActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private EditText etReason;
    private TextView tvStartDate, tvEndDate;
    private Spinner spLeaveType;
    private ListView lvLeaveHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_apply_for_leave);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Initialize Views
        spLeaveType = findViewById(R.id.spinner_leave_type);
        tvStartDate = findViewById(R.id.tv_start_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        etReason = findViewById(R.id.et_leave_reason);
        lvLeaveHistory = findViewById(R.id.lv_leave_history);
        Button btnSubmit = findViewById(R.id.btn_submit_leave);

        // Setup Spinner
        String[] types = {"Sick Leave", "Casual Leave", "Emergency", "Vacation"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLeaveType.setAdapter(adapter);

        // Setup Date Pickers
        tvStartDate.setOnClickListener(v -> showDatePicker(tvStartDate));
        tvEndDate.setOnClickListener(v -> showDatePicker(tvEndDate));

        // Submit Action
        btnSubmit.setOnClickListener(v -> submitLeave());

        // Back Button
        if (findViewById(R.id.btn_back) != null) {
            findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        }

        loadLeaveHistory();
    }

    private void showDatePicker(TextView targetView) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, day);
            targetView.setText(date);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void submitLeave() {
        String type = spLeaveType.getSelectedItem().toString();
        String start = tvStartDate.getText().toString();
        String end = tvEndDate.getText().toString();
        String reason = etReason.getText().toString();

        if (start.equals("Select Date") || end.equals("Select Date") || reason.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.applyForLeave(session.getUserId(), type, start, end, reason)) {
            Toast.makeText(this, "Leave Applied Successfully", Toast.LENGTH_SHORT).show();
            loadLeaveHistory(); // Refresh List
            etReason.setText(""); // Clear Input
        } else {
            Toast.makeText(this, "Failed to apply", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLeaveHistory() {
        Cursor cursor = db.getMyLeaveHistory(session.getUserId());
        if (cursor != null) {
            LeaveAdapter adapter = new LeaveAdapter(cursor);
            lvLeaveHistory.setAdapter(adapter);
        }
    }

    // Inner Class for List Adapter
    private class LeaveAdapter extends CursorAdapter {
        public LeaveAdapter(Cursor cursor) {
            super(StaffLeaveActivity.this, cursor, 0);
        }

        @Override
        public View newView(android.content.Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_staff_leave, parent, false);
        }

        @Override
        public void bindView(View view, android.content.Context context, Cursor cursor) {
            TextView type = view.findViewById(R.id.tv_leave_type);
            TextView dates = view.findViewById(R.id.tv_leave_dates);
            TextView status = view.findViewById(R.id.tv_leave_status);

            String leaveType = cursor.getString(cursor.getColumnIndexOrThrow("leave_type"));
            String start = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
            String end = cursor.getString(cursor.getColumnIndexOrThrow("end_date"));
            String stat = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            type.setText(leaveType);
            dates.setText(start + " to " + end);
            status.setText(stat);

            // Color Code Status
            if ("Approved".equalsIgnoreCase(stat)) {
                status.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else if ("Rejected".equalsIgnoreCase(stat)) {
                status.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                status.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            }
        }
    }
}
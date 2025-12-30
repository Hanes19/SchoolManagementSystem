package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class StaffLeaveActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvStartDate, tvEndDate;
    private EditText etReason, etLeaveType;
    private LinearLayout llHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_apply_for_leave);

        db = new DatabaseHelper(this);

        tvStartDate = findViewById(R.id.tv_start_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        etReason = findViewById(R.id.et_reason);
        etLeaveType = findViewById(R.id.et_leave_type); // Or Spinner
        llHistoryList = findViewById(R.id.ll_leave_history);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        tvStartDate.setOnClickListener(v -> showDatePicker(tvStartDate));
        tvEndDate.setOnClickListener(v -> showDatePicker(tvEndDate));

        findViewById(R.id.btn_submit_leave).setOnClickListener(v -> applyLeave());

        loadLeaveHistory();
    }

    private void showDatePicker(TextView target) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, y, m, d) ->
                target.setText(y + "-" + (m + 1) + "-" + d),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void applyLeave() {
        String start = tvStartDate.getText().toString();
        String end = tvEndDate.getText().toString();
        String reason = etReason.getText().toString().trim();
        String type = etLeaveType != null ? etLeaveType.getText().toString() : "Casual";

        if (TextUtils.isEmpty(reason) || start.contains("Select") || end.contains("Select")) {
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hardcoded user ID 'stf001' for demo
        if (db.applyForLeave("stf001", type, start, end, reason)) {
            Toast.makeText(this, "Leave Application Submitted", Toast.LENGTH_SHORT).show();
            loadLeaveHistory();
        } else {
            Toast.makeText(this, "Submission Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLeaveHistory() {
        llHistoryList.removeAllViews();
        Cursor cursor = db.getMyLeaveHistory("stf001");
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndexOrThrow("leave_type"));
                String start = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
                String end = cursor.getString(cursor.getColumnIndexOrThrow("end_date"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                View itemView = inflater.inflate(R.layout.item_staff_leave, llHistoryList, false);
                TextView tvType = itemView.findViewById(R.id.tv_leave_type);
                TextView tvDate = itemView.findViewById(R.id.tv_dates);
                TextView tvStatus = itemView.findViewById(R.id.tv_status);

                tvType.setText(type);
                tvDate.setText(start + " to " + end);
                tvStatus.setText(status.toUpperCase());

                // Color Logic
                if ("Approved".equalsIgnoreCase(status)) {
                    tvStatus.setTextColor(Color.parseColor("#05CD99"));
                    tvStatus.setBackgroundColor(Color.parseColor("#E6FFF5"));
                } else if ("Rejected".equalsIgnoreCase(status)) {
                    tvStatus.setTextColor(Color.parseColor("#F44336"));
                    tvStatus.setBackgroundColor(Color.parseColor("#FFEBEE"));
                } else {
                    tvStatus.setTextColor(Color.parseColor("#FF9800"));
                    tvStatus.setBackgroundColor(Color.parseColor("#FFF3E0"));
                }

                llHistoryList.addView(itemView);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
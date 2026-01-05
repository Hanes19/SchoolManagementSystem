package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentFeesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager sessionManager;
    private LinearLayout llHistory;
    private TextView tvOutstanding;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_fees);

        // 1. Initialize DB and SessionManager
        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // 2. Get User ID from Session (Fixes the null pointer/bind error)
        studentId = sessionManager.getUserId();

        // Optional: Check if user is logged in
        if (!sessionManager.isLoggedIn() || studentId == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        llHistory = findViewById(R.id.ll_payment_history);
        tvOutstanding = findViewById(R.id.tv_outstanding_amount);

        // 3. Setup Back Button
        View btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        loadFeeData();
    }

    private void loadFeeData() {
        if (llHistory == null) return;

        llHistory.removeAllViews();

        // This query works now because studentId is valid
        Cursor cursor = db.getStudentFees(studentId);
        LayoutInflater inflater = LayoutInflater.from(this);

        double totalPaid = 0;

        // You can fetch this from DB using db.getOutstandingBalance(studentId) if you want real data,
        // but we will keep your hardcoded value for now to match your logic.
        double annualFee = 25000.00;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Ensure columns exist to prevent crashes if schema changes
                int amountIndex = cursor.getColumnIndex("amount");
                int dateIndex = cursor.getColumnIndex("date");
                int methodIndex = cursor.getColumnIndex("payment_method");

                if (amountIndex != -1 && dateIndex != -1) {
                    double amount = cursor.getDouble(amountIndex);
                    String date = cursor.getString(dateIndex);
                    String method = (methodIndex != -1) ? cursor.getString(methodIndex) : "Unknown";

                    totalPaid += amount;

                    View view = inflater.inflate(R.layout.item_expense_row, llHistory, false);
                    TextView tvTitle = view.findViewById(R.id.tv_expense_title);
                    TextView tvDesc = view.findViewById(R.id.tv_expense_category);
                    TextView tvAmount = view.findViewById(R.id.tv_expense_amount);

                    tvTitle.setText("Fee Paid");
                    tvDesc.setText(date + " via " + method);
                    tvAmount.setText("-₱" + String.format("%.2f", amount));
                    tvAmount.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                    llHistory.addView(view);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        double due = Math.max(0, annualFee - totalPaid);
        tvOutstanding.setText("₱" + String.format("%.2f", due));
    }
}
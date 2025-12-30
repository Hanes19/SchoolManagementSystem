package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ParentFeesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llHistory;
    private TextView tvTotalDue;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_fees_view);

        studentId = getIntent().getStringExtra("STUDENT_ID");
        db = new DatabaseHelper(this);

        llHistory = findViewById(R.id.ll_transaction_history);
        tvTotalDue = findViewById(R.id.tv_total_outstanding);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_pay_now).setOnClickListener(v ->
                Toast.makeText(this, "Payment Gateway Integration Required", Toast.LENGTH_SHORT).show()
        );

        loadFeeDetails();
    }

    private void loadFeeDetails() {
        if (llHistory == null) return;
        llHistory.removeAllViews();
        Cursor cursor = db.getStudentFees(studentId);
        LayoutInflater inflater = LayoutInflater.from(this);

        double totalDue = 1500.00; // Mock initial due, or calculate from DB

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                View view = inflater.inflate(R.layout.item_expense_row, llHistory, false);

                // Correct IDs matching item_expense_row.xml
                TextView tvTitle = view.findViewById(R.id.tv_expense_title);
                TextView tvAmount = view.findViewById(R.id.tv_amount);       // Matches item_expense_row
                TextView tvDate = view.findViewById(R.id.tv_date);           // Matches item_expense_row

                tvTitle.setText(desc);
                tvAmount.setText("-$" + amount);
                tvDate.setText(date);

                totalDue -= amount;
                llHistory.addView(view); // Fixed variable name from llList to llHistory
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (tvTotalDue != null) {
            tvTotalDue.setText(String.format("$%.2f", Math.max(0, totalDue)));
        }
    }
}
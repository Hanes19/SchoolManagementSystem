package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminFeesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvCollected, tvPending;
    private LinearLayout llTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_fees); // Ensure XML name matches

        db = new DatabaseHelper(this);

        tvCollected = findViewById(R.id.tv_total_collected);
        tvPending = findViewById(R.id.tv_pending_fees);
        llTransactions = findViewById(R.id.ll_transaction_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadStats();
        loadTransactions();
    }

    private void loadStats() {
        double collected = db.getTotalFeesCollected();
        tvCollected.setText(String.format("$%.2f", collected));

        // Mock pending calculation
        double estimatedTotal = 50000.00;
        double pending = Math.max(0, estimatedTotal - collected);
        tvPending.setText(String.format("$%.2f", pending));
    }

    private void loadTransactions() {
        llTransactions.removeAllViews();
        Cursor cursor = db.getRecentFeeTransactions();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String studentId = cursor.getString(cursor.getColumnIndexOrThrow("student_id"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String method = cursor.getString(cursor.getColumnIndexOrThrow("payment_method"));

                View view = inflater.inflate(R.layout.item_expense_row, llTransactions, false);
                TextView tvTitle = view.findViewById(R.id.tv_expense_title);
                TextView tvDesc = view.findViewById(R.id.tv_expense_category);
                TextView tvAmount = view.findViewById(R.id.tv_expense_amount);

                tvTitle.setText("Received from " + studentId);
                tvDesc.setText(date + " • " + method);
                tvAmount.setText("+$" + amount);
                tvAmount.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                llTransactions.addView(view);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
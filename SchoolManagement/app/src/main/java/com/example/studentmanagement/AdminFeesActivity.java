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
        // FIX 1: Point to the correct XML file (admin_fees_billings.xml)
        setContentView(R.layout.admin_fees_billings);

        db = new DatabaseHelper(this);

        tvCollected = findViewById(R.id.tv_total_collected);
        tvPending = findViewById(R.id.tv_pending_fees);
        llTransactions = findViewById(R.id.ll_transaction_list);

        // FIX 2: Ensure this ID exists in admin_fees_billings.xml or remove this line
        View backBtn = findViewById(R.id.btn_back);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }

        loadStats();
        loadTransactions();
    }

    private void loadStats() {
        double collected = db.getTotalFeesCollected();
        if (tvCollected != null) {
            tvCollected.setText(String.format("$%.2f", collected));
        }

        double estimatedTotal = 50000.00;
        double pending = Math.max(0, estimatedTotal - collected);
        if (tvPending != null) {
            tvPending.setText(String.format("$%.2f", pending));
        }
    }

    private void loadTransactions() {
        if (llTransactions == null) return;

        llTransactions.removeAllViews();
        Cursor cursor = db.getRecentFeeTransactions();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String studentId = cursor.getString(cursor.getColumnIndexOrThrow("student_id"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String method = cursor.getString(cursor.getColumnIndexOrThrow("payment_method"));

                View view = inflater.inflate(R.layout.item_expense_row, llTransactions, false);

                // FIX 3: Update IDs to match the corrected item_expense_row.xml
                TextView tvTitle = view.findViewById(R.id.tv_expense_title);
                TextView tvDesc = view.findViewById(R.id.tv_requested_by); // Was tv_expense_category
                TextView tvAmount = view.findViewById(R.id.tv_amount);       // Was tv_expense_amount

                tvTitle.setText("Received from " + studentId);
                tvDesc.setText(date + " • " + method);
                tvAmount.setText("+$" + amount);
                tvAmount.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                llTransactions.addView(view);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
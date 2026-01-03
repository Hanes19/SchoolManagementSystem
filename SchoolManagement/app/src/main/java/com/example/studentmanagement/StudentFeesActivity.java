package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudentFeesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llHistory;
    private TextView tvOutstanding;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_fees);

        db = new DatabaseHelper(this);
        studentId = getIntent().getStringExtra("STUDENT_ID");

        llHistory = findViewById(R.id.ll_payment_history);
        tvOutstanding = findViewById(R.id.tv_outstanding_amount);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadFeeData();
    }

    private void loadFeeData() {
        llHistory.removeAllViews();
        Cursor cursor = db.getStudentFees(studentId);
        LayoutInflater inflater = LayoutInflater.from(this);

        double totalPaid = 0;
        double annualFee = 25000.00;

        if (cursor.moveToFirst()) {
            do {
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String method = cursor.getString(cursor.getColumnIndexOrThrow("payment_method"));

                totalPaid += amount;

                View view = inflater.inflate(R.layout.item_expense_row, llHistory, false);
                TextView tvTitle = view.findViewById(R.id.tv_expense_title);
                TextView tvDesc = view.findViewById(R.id.tv_expense_category);
                TextView tvAmount = view.findViewById(R.id.tv_expense_amount);

                tvTitle.setText("Fee Paid");
                tvDesc.setText(date + " via " + method);
                tvAmount.setText("-₱" + amount);
                tvAmount.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                llHistory.addView(view);
            } while (cursor.moveToNext());
        }
        cursor.close();

        double due = Math.max(0, annualFee - totalPaid);
        tvOutstanding.setText("₱" + String.format("%.2f", due));
    }
}
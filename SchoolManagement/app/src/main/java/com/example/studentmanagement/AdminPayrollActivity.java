package com.example.studentmanagement;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminPayrollActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llPayrollList;
    private String currentMonth = "October 2025"; // In real app, select from calendar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_payroll);

        db = new DatabaseHelper(this);
        llPayrollList = findViewById(R.id.ll_payroll_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Listen for results from the process sheet to refresh list
        getSupportFragmentManager().setFragmentResultListener("refresh_payroll", this, (requestKey, result) -> {
            loadPayrollData();
        });

        loadPayrollData();
    }

    private void loadPayrollData() {
        if (llPayrollList == null) return;
        llPayrollList.removeAllViews();
        SQLiteDatabase database = db.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE role IN ('Teacher', 'Staff')", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));

                Cursor payCursor = database.rawQuery("SELECT * FROM payroll WHERE user_id=? AND month=?", new String[]{userId, currentMonth});
                boolean isPaid = payCursor.moveToFirst();
                payCursor.close();

                View itemView = LayoutInflater.from(this).inflate(R.layout.item_admin_payroll_row, llPayrollList, false);

                TextView tvName = itemView.findViewById(R.id.tv_staff_name);
                TextView tvRole = itemView.findViewById(R.id.tv_staff_role);
                TextView tvStatus = itemView.findViewById(R.id.tv_payment_status);
                CardView card = itemView.findViewById(R.id.card_payslip_item);

                tvName.setText(name);
                tvRole.setText(role);

                if (isPaid) {
                    tvStatus.setText("PAID");
                    tvStatus.setBackgroundTintList(getColorStateList(android.R.color.holo_green_dark));
                    card.setOnClickListener(v -> showPayrollDetailsBottomSheet(userId));
                } else {
                    tvStatus.setText("PENDING");
                    tvStatus.setBackgroundTintList(getColorStateList(android.R.color.holo_red_dark));
                    card.setOnClickListener(v -> showProcessPaymentBottomSheet(userId, name));
                }

                llPayrollList.addView(itemView);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void showProcessPaymentBottomSheet(String userId, String name) {
        AdminPayrollProcessSheet sheet = new AdminPayrollProcessSheet();
        Bundle args = new Bundle();
        args.putString("user_id", userId);
        args.putString("name", name);
        args.putString("month", currentMonth);
        sheet.setArguments(args);
        sheet.show(getSupportFragmentManager(), "ProcessPayroll");
    }

    private void showPayrollDetailsBottomSheet(String userId) {
        AdminPayrollDetailsSheet sheet = new AdminPayrollDetailsSheet();
        Bundle args = new Bundle();
        args.putString("user_id", userId);
        args.putString("month", currentMonth);
        sheet.setArguments(args);
        sheet.show(getSupportFragmentManager(), "PayrollDetails");
    }
}
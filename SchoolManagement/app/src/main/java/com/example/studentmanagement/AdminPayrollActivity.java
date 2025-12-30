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
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AdminPayrollActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llPayrollList;
    private TextView tvTotalPending, tvTotalPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_payroll);

        db = new DatabaseHelper(this);
        // This ID must exist in admin_payroll.xml (as added in the previous step)
        llPayrollList = findViewById(R.id.ll_payroll_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadPayrollData();
    }

    private void loadPayrollData() {
        if (llPayrollList == null) return;
        llPayrollList.removeAllViews();
        SQLiteDatabase database = db.getReadableDatabase();

        // Join Users and Payroll to see who is paid and who is not
        // This query fetches all Teachers/Staff
        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE role IN ('Teacher', 'Staff')", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));

                // Check if paid for current month (Mocking 'October 2025')
                Cursor payCursor = database.rawQuery("SELECT * FROM payroll WHERE user_id=? AND month=?", new String[]{userId, "October 2025"});
                boolean isPaid = payCursor.moveToFirst();
                payCursor.close();

                // FIX: Use the new layout 'item_admin_payroll_row' which has the correct IDs
                View itemView = LayoutInflater.from(this).inflate(R.layout.item_admin_payroll_row, llPayrollList, false);

                // These IDs (tv_staff_name, etc.) must exist in item_admin_payroll_row.xml
                TextView tvName = itemView.findViewById(R.id.tv_staff_name);
                TextView tvRole = itemView.findViewById(R.id.tv_staff_role);
                TextView tvStatus = itemView.findViewById(R.id.tv_payment_status);
                CardView card = itemView.findViewById(R.id.card_payslip_item);

                tvName.setText(name);
                tvRole.setText(role);

                if (isPaid) {
                    tvStatus.setText("PAID");
                    tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    card.setOnClickListener(v -> showPayrollDetailsBottomSheet(userId));
                } else {
                    tvStatus.setText("PENDING");
                    tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    card.setOnClickListener(v -> showProcessPaymentBottomSheet(userId, name));
                }

                llPayrollList.addView(itemView);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void showProcessPaymentBottomSheet(String userId, String name) {
        // ... (Use your existing BottomSheet logic, but pre-fill the name and save to DB on confirm)
        Toast.makeText(this, "Process Payment for " + name, Toast.LENGTH_SHORT).show();
        // On Save: db.execSQL("INSERT INTO payroll ... VALUES ...")
    }

    private void showPayrollDetailsBottomSheet(String userId) {
        // ... (Fetch details from payroll table and show)
    }
}
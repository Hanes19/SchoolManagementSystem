package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StaffPayslipActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_my_payslips);

        db = new DatabaseHelper(this);
        llList = findViewById(R.id.ll_payslip_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadPayslips();
    }

    private void loadPayslips() {
        llList.removeAllViews();
        Cursor cursor = db.getMyPayslips("stf001");
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String month = cursor.getString(cursor.getColumnIndexOrThrow("month"));
                double net = cursor.getDouble(cursor.getColumnIndexOrThrow("net_salary"));

                View itemView = inflater.inflate(R.layout.item_staff_payslip, llList, false);
                TextView tvMonth = itemView.findViewById(R.id.tv_month);
                TextView tvAmount = itemView.findViewById(R.id.tv_net_salary);

                tvMonth.setText(month);
                tvAmount.setText(String.format("$%.2f", net));

                llList.addView(itemView);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
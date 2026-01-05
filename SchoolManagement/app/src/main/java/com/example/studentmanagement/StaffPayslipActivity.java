package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StaffPayslipActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private ListView lvPayslips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_my_payslips);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);
        lvPayslips = findViewById(R.id.lv_payslips);

        loadPayslips();

        if (findViewById(R.id.btn_back) != null) {
            findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        }
    }

    private void loadPayslips() {
        Cursor cursor = db.getMyPayslips(session.getUserId());
        if (cursor != null) {
            PayslipAdapter adapter = new PayslipAdapter(cursor);
            lvPayslips.setAdapter(adapter);
        }
    }

    private class PayslipAdapter extends CursorAdapter {
        public PayslipAdapter(Cursor cursor) {
            super(StaffPayslipActivity.this, cursor, 0);
        }

        @Override
        public View newView(android.content.Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_staff_payslip, parent, false);
        }

        @Override
        public void bindView(View view, android.content.Context context, Cursor cursor) {
            TextView month = view.findViewById(R.id.tv_payslip_month);
            TextView amount = view.findViewById(R.id.tv_payslip_amount);
            TextView status = view.findViewById(R.id.tv_payslip_status);

            String mon = cursor.getString(cursor.getColumnIndexOrThrow("month"));
            double net = cursor.getDouble(cursor.getColumnIndexOrThrow("net_salary"));
            String stat = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            month.setText(mon);
            amount.setText(String.format("$%.2f", net));
            status.setText(stat);
        }
    }
}
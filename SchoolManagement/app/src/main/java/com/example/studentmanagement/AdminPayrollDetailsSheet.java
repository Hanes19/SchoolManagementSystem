package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AdminPayrollDetailsSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_payroll_description, container, false);

        if (getArguments() != null) {
            String userId = getArguments().getString("user_id");
            String month = getArguments().getString("month");
            loadDetails(view, userId, month);
        }

        view.findViewById(R.id.btn_close_details).setOnClickListener(v -> dismiss());
        return view;
    }

    private void loadDetails(View view, String userId, String month) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor cursor = db.getPayrollDetails(userId, month);

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve data
            double basic = cursor.getDouble(cursor.getColumnIndexOrThrow("basic_salary"));
            double allow = cursor.getDouble(cursor.getColumnIndexOrThrow("allowances"));
            double deduct = cursor.getDouble(cursor.getColumnIndexOrThrow("deductions"));
            double net = cursor.getDouble(cursor.getColumnIndexOrThrow("net_salary"));

            // Note: You must add these IDs to admin_payroll_description.xml
            TextView tvBasic = view.findViewById(R.id.tv_details_basic);
            TextView tvAllow = view.findViewById(R.id.tv_details_allowance);
            TextView tvDeduct = view.findViewById(R.id.tv_details_deduction);
            TextView tvNet = view.findViewById(R.id.tv_details_net_pay);
            TextView tvMonth = view.findViewById(R.id.tv_details_month);

            // Populate
            tvBasic.setText(String.format("₱%.2f", basic));
            tvAllow.setText(String.format("+ ₱%.2f", allow));
            tvDeduct.setText(String.format("- ₱%.2f", deduct));
            tvNet.setText(String.format("₱%.2f", net));
            tvMonth.setText(month);

            cursor.close();
        }
    }
}
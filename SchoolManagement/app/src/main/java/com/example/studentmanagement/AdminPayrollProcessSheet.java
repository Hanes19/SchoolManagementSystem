package com.example.studentmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AdminPayrollProcessSheet extends BottomSheetDialogFragment {

    private String userId, userName, month;
    private double basic = 3000.00; // Default
    private double allowance = 500.00;
    private double deduction = 200.00;
    private TextView tvName, tvId, tvMonth, tvBasic, tvAllow, tvDeduct, tvNet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_payroll_process_payment, container, false);

        if (getArguments() != null) {
            userId = getArguments().getString("user_id");
            userName = getArguments().getString("name");
            month = getArguments().getString("month");
        }

        // Init Views
        tvName = view.findViewById(R.id.tv_proc_name);
        tvId = view.findViewById(R.id.tv_proc_id);
        tvMonth = view.findViewById(R.id.tv_proc_month);
        tvBasic = view.findViewById(R.id.tv_proc_basic);
        tvAllow = view.findViewById(R.id.tv_proc_allowance);
        tvDeduct = view.findViewById(R.id.tv_proc_deduction);
        tvNet = view.findViewById(R.id.tv_proc_net);
        Spinner spinner = view.findViewById(R.id.spinner_payment_method);

        // Setup Data
        tvName.setText(userName);
        tvId.setText("ID: " + userId);
        tvMonth.setText(month);
        updateCalculations();

        String[] methods = {"Bank Transfer", "Cash", "Cheque"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, methods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Listen for edits
        getParentFragmentManager().setFragmentResultListener("edit_payroll_result", this, (requestKey, result) -> {
            basic = result.getDouble("basic");
            allowance = result.getDouble("allowance");
            deduction = result.getDouble("deduction");
            updateCalculations();
        });

        view.findViewById(R.id.btn_edit_details).setOnClickListener(v -> {
            AdminPayrollEditSheet editSheet = new AdminPayrollEditSheet();
            Bundle args = new Bundle();
            args.putDouble("basic", basic);
            args.putDouble("allowance", allowance);
            args.putDouble("deduction", deduction);
            args.putString("name", userName);
            editSheet.setArguments(args);
            editSheet.show(getParentFragmentManager(), "EditPayroll");
        });

        view.findViewById(R.id.btn_confirm_payment).setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(getContext());
            boolean success = db.addPayroll(userId, month, basic, allowance, deduction, (basic + allowance - deduction), "Paid");
            if (success) {
                Toast.makeText(getContext(), "Payment Processed!", Toast.LENGTH_SHORT).show();
                Bundle result = new Bundle();
                result.putBoolean("refresh", true);
                getParentFragmentManager().setFragmentResult("refresh_payroll", result);
                dismiss();
            } else {
                Toast.makeText(getContext(), "Error saving payroll", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btn_close_sheet).setOnClickListener(v -> dismiss());

        return view;
    }

    private void updateCalculations() {
        tvBasic.setText(String.format("₱%.2f", basic));
        tvAllow.setText(String.format("+ ₱%.2f", allowance));
        tvDeduct.setText(String.format("- ₱%.2f", deduction));
        tvNet.setText(String.format("₱%.2f", (basic + allowance - deduction)));
    }
}
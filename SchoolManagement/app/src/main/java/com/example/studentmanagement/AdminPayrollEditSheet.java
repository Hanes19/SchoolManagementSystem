package com.example.studentmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AdminPayrollEditSheet extends BottomSheetDialogFragment {

    private EditText etBasic, etAllow, etDeduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_payroll_edit_details, container, false);

        etBasic = view.findViewById(R.id.et_basic_salary);
        etAllow = view.findViewById(R.id.et_allowances);
        etDeduct = view.findViewById(R.id.et_deductions);

        // Use hardcoded ID for now or the one added in XML update
        // TextView tvName = view.findViewById(R.id.tv_edit_staff_name_placeholder);

        if (getArguments() != null) {
            etBasic.setText(String.valueOf(getArguments().getDouble("basic")));
            etAllow.setText(String.valueOf(getArguments().getDouble("allowance")));
            etDeduct.setText(String.valueOf(getArguments().getDouble("deduction")));
        }

        view.findViewById(R.id.btn_save_changes).setOnClickListener(v -> {
            Bundle result = new Bundle();
            try {
                result.putDouble("basic", Double.parseDouble(etBasic.getText().toString()));
                result.putDouble("allowance", Double.parseDouble(etAllow.getText().toString()));
                result.putDouble("deduction", Double.parseDouble(etDeduct.getText().toString()));
                getParentFragmentManager().setFragmentResult("edit_payroll_result", result);
                dismiss();
            } catch (NumberFormatException e) {
                // Handle invalid input
            }
        });

        view.findViewById(R.id.btn_close_edit).setOnClickListener(v -> dismiss());

        return view;
    }
}
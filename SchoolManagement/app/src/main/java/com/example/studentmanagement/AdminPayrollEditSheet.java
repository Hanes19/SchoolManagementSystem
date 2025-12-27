package com.example.studentmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AdminPayrollEditSheet extends BottomSheetDialogFragment {

    private EditText etBasic, etAllowances, etDeductions, etRemarks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_payroll_edit_details, container, false);

        // 1. Initialize Views
        ImageView btnClose = view.findViewById(R.id.btn_close_edit);
        CardView btnSaveChanges = view.findViewById(R.id.btn_save_changes);

        etBasic = view.findViewById(R.id.et_basic_salary);
        etAllowances = view.findViewById(R.id.et_allowances);
        etDeductions = view.findViewById(R.id.et_deductions);
        etRemarks = view.findViewById(R.id.et_remarks);

        // 2. Close Button
        btnClose.setOnClickListener(v -> dismiss());

        // 3. Save Changes Button
        btnSaveChanges.setOnClickListener(v -> {
            if (validateInputs()) {
                saveData();
            }
        });

        return view;
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etBasic.getText())) {
            etBasic.setError("Required");
            return false;
        }
        // Allowances/Deductions can technically be empty (0), but let's check basic validity
        return true;
    }

    private void saveData() {
        // 1. Get values
        double basic = Double.parseDouble(etBasic.getText().toString());
        double allowances = etAllowances.getText().toString().isEmpty() ? 0 : Double.parseDouble(etAllowances.getText().toString());
        double deductions = etDeductions.getText().toString().isEmpty() ? 0 : Double.parseDouble(etDeductions.getText().toString());

        // 2. Perform Calculation (Logic for Net Pay)
        double netPay = basic + allowances - deductions;

        // 3. Save to Database (Placeholder)
        // updatePayrollDatabase(netPay, ...);

        Toast.makeText(getContext(), "Payroll updated. New Net Pay: " + netPay, Toast.LENGTH_SHORT).show();

        // 4. Return to Process Screen
        dismiss();
        // Re-open the process sheet to show updated values
        AdminPayrollProcessSheet processSheet = new AdminPayrollProcessSheet();
        processSheet.show(getParentFragmentManager(), "ProcessPayrollSheet");
    }
}
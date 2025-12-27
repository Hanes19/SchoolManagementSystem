package com.example.studentmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AdminPayrollProcessSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_payroll_process_payment, container, false);

        // 1. Initialize Views
        ImageView btnClose = view.findViewById(R.id.btn_close_sheet);
        CardView btnEditDetails = view.findViewById(R.id.btn_edit_details);
        CardView btnConfirmPayment = view.findViewById(R.id.btn_confirm_payment);
        Spinner spinnerPayment = view.findViewById(R.id.spinner_payment_method);

        // 2. Setup Spinner (Payment Methods)
        String[] paymentMethods = {"Bank Transfer", "Cash", "Cheque", "E-Wallet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayment.setAdapter(adapter);

        // 3. Close Button
        btnClose.setOnClickListener(v -> dismiss());

        // 4. Edit Details Button
        // Closes this sheet and opens the Edit Sheet
        btnEditDetails.setOnClickListener(v -> {
            dismiss(); // Close current sheet
            AdminPayrollEditSheet editSheet = new AdminPayrollEditSheet();
            editSheet.show(getParentFragmentManager(), "EditPayrollSheet");
        });

        // 5. Confirm Payment Button
        btnConfirmPayment.setOnClickListener(v -> {
            String selectedMethod = spinnerPayment.getSelectedItem().toString();
            // In a real app, you would save this transaction to the database here
            Toast.makeText(getContext(), "Payment confirmed via " + selectedMethod, Toast.LENGTH_LONG).show();
            dismiss();
        });

        return view;
    }
}
package com.example.studentmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AdminPayrollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_payroll); // Ensure this matches your main XML name

        // --- 1. Header Navigation ---
        ImageView btnBack = findViewById(R.id.btn_back); // Make sure ID is btn_back or btn_back_payroll in XML
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // --- 2. Summary Card Action ---
        // Clicking the summary card opens the Process Payment sheet
        CardView summaryCard = findViewById(R.id.summary_card); // Add this ID to your XML
        if (summaryCard != null) {
            summaryCard.setOnClickListener(v -> showProcessPaymentBottomSheet());
        }

        // --- 3. Employee Card: Argus Filch (Pending Payment) ---
        // Clicking this opens the "Process Payment" sheet
        CardView cardArgus = findViewById(R.id.card_payslip_argus); // Add this ID to Argus's card in XML
        if (cardArgus != null) {
            cardArgus.setOnClickListener(v -> showProcessPaymentBottomSheet());
        }

        // --- 4. Employee Card: Walter White (Paid/History) ---
        // Clicking this opens the "Payroll Details" (Read-Only) sheet
        CardView cardWalter = findViewById(R.id.card_payslip_walter); // Add this ID to Walter's card in XML
        if (cardWalter != null) {
            cardWalter.setOnClickListener(v -> showPayrollDetailsBottomSheet());
        }
    }

    // =================================================================================
    // BOTTOM SHEET 1: PROCESS PAYMENT (Argus Filch)
    // Layout: admin_payroll_process_payment.xml
    // =================================================================================
    private void showProcessPaymentBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        // Inflate the layout we created earlier
        View sheetView = LayoutInflater.from(this).inflate(R.layout.admin_payroll_process_payment, null);
        bottomSheetDialog.setContentView(sheetView);

        // 1. Close Button
        ImageView btnClose = sheetView.findViewById(R.id.btn_close_sheet);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // 2. Setup Spinner (Payment Methods)
        Spinner spinnerPayment = sheetView.findViewById(R.id.spinner_payment_method);
        String[] paymentMethods = {"Bank Transfer", "Cash", "Cheque", "E-Wallet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayment.setAdapter(adapter);

        // 3. Edit Details Action -> Switches to Edit Sheet
        CardView btnEdit = sheetView.findViewById(R.id.btn_edit_details);
        btnEdit.setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); // Close current sheet
            showEditDetailsBottomSheet(); // Open edit sheet
        });

        // 4. Confirm Payment Action
        CardView btnConfirm = sheetView.findViewById(R.id.btn_confirm_payment);
        btnConfirm.setOnClickListener(v -> {
            String selectedMethod = spinnerPayment.getSelectedItem().toString();
            Toast.makeText(this, "Payment Confirmed via " + selectedMethod, Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    // =================================================================================
    // BOTTOM SHEET 2: EDIT DETAILS (Adjustment)
    // Layout: admin_payroll_edit_details.xml
    // =================================================================================
    private void showEditDetailsBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        // Inflate the layout we created earlier
        View sheetView = LayoutInflater.from(this).inflate(R.layout.admin_payroll_edit_details, null);
        bottomSheetDialog.setContentView(sheetView);

        // 1. Close Button
        ImageView btnClose = sheetView.findViewById(R.id.btn_close_edit);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // 2. Input Fields (For validation or getting values)
        EditText etBasic = sheetView.findViewById(R.id.et_basic_salary);
        EditText etAllowances = sheetView.findViewById(R.id.et_allowances);
        EditText etDeductions = sheetView.findViewById(R.id.et_deductions);
        EditText etRemarks = sheetView.findViewById(R.id.et_remarks);

        // 3. Save Changes Action
        CardView btnSave = sheetView.findViewById(R.id.btn_save_changes);
        btnSave.setOnClickListener(v -> {
            // Logic to save data would go here
            Toast.makeText(this, "Payroll Details Updated", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();

            // Re-open the Process sheet to show the new values
            showProcessPaymentBottomSheet();
        });

        bottomSheetDialog.show();
    }

    // =================================================================================
    // BOTTOM SHEET 3: PAYROLL DETAILS (Read-Only)
    // Layout: admin_payroll_description.xml (Assumed)
    // =================================================================================
    private void showPayrollDetailsBottomSheet() {
        // If you don't have 'admin_payroll_description.xml' yet,
        // you can reuse 'admin_payroll_process_payment.xml' and hide the buttons.
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // Using process layout as a placeholder, but hiding the action buttons
        View sheetView = LayoutInflater.from(this).inflate(R.layout.admin_payroll_process_payment, null);
        bottomSheetDialog.setContentView(sheetView);

        // Hide the editable buttons for read-only view
        CardView btnEdit = sheetView.findViewById(R.id.btn_edit_details);
        CardView btnConfirm = sheetView.findViewById(R.id.btn_confirm_payment);
        if(btnEdit != null) btnEdit.setVisibility(View.GONE);
        if(btnConfirm != null) btnConfirm.setVisibility(View.GONE);

        // Close Button
        ImageView btnClose = sheetView.findViewById(R.id.btn_close_sheet);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }
}
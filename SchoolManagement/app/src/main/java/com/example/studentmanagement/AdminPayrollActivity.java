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
        setContentView(R.layout.admin_payroll); // Main Layout

        // --- 1. Header Navigation ---
        ImageView btnBack = findViewById(R.id.btn_back_payroll);
        btnBack.setOnClickListener(v -> finish());

        ImageView btnFilter = findViewById(R.id.btn_filter_payroll);
        btnFilter.setOnClickListener(v -> {
            Toast.makeText(this, "Filter feature coming soon", Toast.LENGTH_SHORT).show();
        });

        // --- 2. Summary Card Action ---
        // Clicking the top summary card opens the Process Payment sheet (e.g., for bulk processing)
        CardView summaryCard = findViewById(R.id.summary_card);
        summaryCard.setOnClickListener(v -> showProcessPaymentBottomSheet());

        // --- 3. List Item: Walter White (PAID) ---
        // Clicking this opens the "Payroll Details" / Description sheet
        // ensure you added android:id="@+id/card_payslip_walter" to his CardView in XML
        CardView cardWalter = findViewById(R.id.card_payslip_walter);
        if (cardWalter != null) {
            cardWalter.setOnClickListener(v -> showPayrollDetailsBottomSheet());
        } else {
            // Fallback strategy if ID is missing (finds first card in list)
            // Ideally, please add the ID in XML.
        }

        // --- 4. List Item: Argus Filch (PENDING) ---
        // Clicking this opens the "Process Payment" sheet
        // ensure you added android:id="@+id/card_payslip_argus" to his CardView in XML
        CardView cardArgus = findViewById(R.id.card_payslip_argus);
        if (cardArgus != null) {
            cardArgus.setOnClickListener(v -> showProcessPaymentBottomSheet());
        }
    }

    /**
     * FUNCTION 1: Shows the "Payroll Details" Bottom Sheet (Read-Only)
     * Layout: admin_payroll_description.xml
     */
    private void showPayrollDetailsBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.admin_payroll_description, null);
        bottomSheetDialog.setContentView(sheetView);

        // Close Button
        ImageView btnClose = sheetView.findViewById(R.id.btn_close_details);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Download Button
        CardView btnDownload = sheetView.findViewById(R.id.btn_download_payslip);
        btnDownload.setOnClickListener(v -> {
            Toast.makeText(this, "Downloading Payslip...", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    /**
     * FUNCTION 2: Shows the "Process Payment" Bottom Sheet
     * Layout: admin_payroll_process_payment.xml
     */
    private void showProcessPaymentBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.admin_payroll_process_payment, null);
        bottomSheetDialog.setContentView(sheetView);

        // Close Button
        ImageView btnClose = sheetView.findViewById(R.id.btn_close_sheet);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Spinner Setup (Payment Method)
        Spinner spinnerPayment = sheetView.findViewById(R.id.spinner_payment_method);
        String[] paymentMethods = {"Bank Transfer", "Cash", "Cheque", "Mobile Money"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paymentMethods);
        spinnerPayment.setAdapter(adapter);

        // Edit Details Action (Opens the Edit Sheet)
        // Note: Ensure your 'Edit Details' card in admin_payroll_process_payment.xml has an ID, e.g., @id/btn_edit_details
        View btnEdit = sheetView.findViewById(R.id.btn_edit_details); // Assuming you added this ID
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                bottomSheetDialog.dismiss(); // Close current
                showEditDetailsBottomSheet(); // Open edit
            });
        }

        // Confirm Payment Action
        CardView btnConfirm = sheetView.findViewById(R.id.btn_confirm_payment);
        btnConfirm.setOnClickListener(v -> {
            Toast.makeText(this, "Payment Confirmed!", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    /**
     * FUNCTION 3: Shows the "Edit Details" Bottom Sheet
     * Layout: admin_payroll_edit_details.xml
     */
    private void showEditDetailsBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.admin_payroll_edit_details, null);
        bottomSheetDialog.setContentView(sheetView);

        // Close Button
        ImageView btnClose = sheetView.findViewById(R.id.btn_close_edit);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Inputs
        EditText etBasic = sheetView.findViewById(R.id.et_basic_salary);
        EditText etAllowances = sheetView.findViewById(R.id.et_allowances);
        EditText etDeductions = sheetView.findViewById(R.id.et_deductions);
        EditText etRemarks = sheetView.findViewById(R.id.et_remarks);

        // Save Button
        CardView btnSave = sheetView.findViewById(R.id.btn_save_changes);
        btnSave.setOnClickListener(v -> {
            // Save logic here
            Toast.makeText(this, "Details Updated", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();

            // Optional: Re-open process sheet to show updated values
            showProcessPaymentBottomSheet();
        });

        bottomSheetDialog.show();
    }
}
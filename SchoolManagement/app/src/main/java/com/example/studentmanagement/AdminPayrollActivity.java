package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AdminPayrollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_payroll); // The main XML file

        // 1. Setup Header Navigation
        ImageView btnBack = findViewById(R.id.btn_back_payroll);
        btnBack.setOnClickListener(v -> finish());

        // 2. Setup Filter Button
        ImageView btnFilter = findViewById(R.id.btn_filter_payroll);
        btnFilter.setOnClickListener(v -> {
            // Optional: Call a showFilterBottomSheet() function here if needed
            Toast.makeText(this, "Filter feature coming soon", Toast.LENGTH_SHORT).show();
        });

        // 3. Setup "Process Payments" Trigger
        // Note: In your XML, the "Process Payments >" text doesn't have an ID.
        // We will attach the listener to the whole Summary Card for now.
        CardView summaryCard = findViewById(R.id.summary_card);
        summaryCard.setOnClickListener(v -> {
            showProcessPaymentBottomSheet();
        });

        // 4. Setup Static Payslip Click (Example for "Argus Filch" / Pending)
        // Since the card doesn't have an ID in the ScrollView, this is a placeholder.
        // In a real app, you would use a RecyclerView adapter.
        // For this demo, we assume the user clicks the summary to process payment.
    }

    /**
     * Function 1: Opens the Process Payment Bottom Sheet
     * Layout: admin_payroll_process_payment.xml
     */
    private void showProcessPaymentBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.admin_payroll_process_payment, null);
        bottomSheetDialog.setContentView(sheetView);

        // --- Setup Views inside the Sheet ---

        // Close Button
        ImageView btnClose = sheetView.findViewById(R.id.btn_close_sheet);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Spinner Setup (Payment Method)
        Spinner spinnerPayment = sheetView.findViewById(R.id.spinner_payment_method);
        String[] paymentMethods = {"Bank Transfer", "Cash", "Cheque", "Mobile Money"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paymentMethods);
        spinnerPayment.setAdapter(adapter);

        // "Edit Details" Button
        // Note: The CardView for "Edit Details" in your XML did not have an ID.
        // You should add android:id="@+id/btn_open_edit" to that CardView.
        // I will attempt to find it via the parent container for this example, or you can add the ID.
        // Assuming you added the ID:
        CardView btnEditDetails = sheetView.findViewById(R.id.btn_confirm_payment); // Placeholder: Using confirm button ID logic to find sibling if needed
        // Ideally, go back to XML and add ID to the "Edit Details" card.
        // For now, let's look for the Confirm button and find the Edit button if possible,
        // or just rely on you adding an ID. Let's assume you add ID `btn_edit_details_action`.

        // TEMPORARY FIX: Finding the view by traversing or assuming ID existence
        // Please add android:id="@+id/btn_edit_details" to the grey "Edit Details" CardView in your XML.
        int editButtonId = getResources().getIdentifier("btn_edit_details", "id", getPackageName());
        if (editButtonId != 0) {
            View btnEdit = sheetView.findViewById(editButtonId);
            btnEdit.setOnClickListener(v -> {
                bottomSheetDialog.dismiss(); // Close current sheet
                showEditDetailsBottomSheet(); // Open edit sheet
            });
        }

        // Confirm Button
        CardView btnConfirm = sheetView.findViewById(R.id.btn_confirm_payment);
        btnConfirm.setOnClickListener(v -> {
            Toast.makeText(this, "Payment Processed Successfully!", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    /**
     * Function 2: Opens the Edit Details Bottom Sheet
     * Layout: admin_payroll_edit_details.xml
     */
    private void showEditDetailsBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.admin_payroll_edit_details, null);
        bottomSheetDialog.setContentView(sheetView);

        // --- Setup Views inside the Sheet ---

        // Inputs
        EditText etBasic = sheetView.findViewById(R.id.et_basic_salary);
        EditText etAllowances = sheetView.findViewById(R.id.et_allowances);
        EditText etDeductions = sheetView.findViewById(R.id.et_deductions);
        EditText etRemarks = sheetView.findViewById(R.id.et_remarks);

        // Close Button
        ImageView btnClose = sheetView.findViewById(R.id.btn_close_edit);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Save Button
        CardView btnSave = sheetView.findViewById(R.id.btn_save_changes);
        btnSave.setOnClickListener(v -> {
            String salary = etBasic.getText().toString();
            String allowance = etAllowances.getText().toString();

            // Logic to save data would go here
            Toast.makeText(this, "Payroll Details Updated", Toast.LENGTH_SHORT).show();

            bottomSheetDialog.dismiss();
            // Optional: Re-open process sheet
            showProcessPaymentBottomSheet();
        });

        bottomSheetDialog.show();
    }
}
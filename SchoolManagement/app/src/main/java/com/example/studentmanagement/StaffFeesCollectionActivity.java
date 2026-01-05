package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StaffFeesCollectionActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;

    private EditText etStudentId, etAmount;
    private TextView tvStudentName, tvCurrentDue;
    private Spinner spPaymentMethod;
    private LinearLayout layoutPaymentSection;
    private Button btnVerify, btnCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_fees_collection); // Ensure XML name matches

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Init Views
        etStudentId = findViewById(R.id.et_student_id);
        btnVerify = findViewById(R.id.btn_verify_student);

        layoutPaymentSection = findViewById(R.id.layout_payment_section);
        tvStudentName = findViewById(R.id.tv_student_name);
        tvCurrentDue = findViewById(R.id.tv_current_due);

        etAmount = findViewById(R.id.et_amount);
        spPaymentMethod = findViewById(R.id.spinner_payment_method);
        btnCollect = findViewById(R.id.btn_collect_fee);

        // Setup Spinner
        String[] methods = {"Cash", "Bank Transfer", "Check", "Card"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, methods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPaymentMethod.setAdapter(adapter);

        // Hide payment section initially
        layoutPaymentSection.setVisibility(View.GONE);

        // Verify Student Listener
        btnVerify.setOnClickListener(v -> verifyStudent());

        // Collect Fee Listener
        btnCollect.setOnClickListener(v -> processPayment());

        // Back Button
        if (findViewById(R.id.btn_back) != null) {
            findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        }
    }

    private void verifyStudent() {
        String studentId = etStudentId.getText().toString().trim();
        if (studentId.isEmpty()) {
            Toast.makeText(this, "Enter Student ID", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = db.getStudentName(studentId); // Helper method in DB
        if (name != null) {
            tvStudentName.setText("Student: " + name);

            // Calculate Due (Total Fees - Total Paid)
            double due = db.getOutstandingBalance(studentId); // Helper method in DB
            tvCurrentDue.setText(String.format("Outstanding Balance: $%.2f", due));

            layoutPaymentSection.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
            layoutPaymentSection.setVisibility(View.GONE);
        }
    }

    private void processPayment() {
        String studentId = etStudentId.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String method = spPaymentMethod.getSelectedItem().toString();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        String staffId = session.getUserId();

        if (db.collectFee(studentId, staffId, amount, method)) {
            Toast.makeText(this, "Payment Recorded Successfully!", Toast.LENGTH_LONG).show();

            // Reset for next payment
            etAmount.setText("");
            layoutPaymentSection.setVisibility(View.GONE);
            etStudentId.setText("");
        } else {
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
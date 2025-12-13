package com.example.studentmanagement;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminGenerateInvoiceActivity extends AppCompatActivity {

    // Fee Constants
    private static final double FEE_TUITION = 1500.00;
    private static final double FEE_TRANSPORT = 250.00;
    private static final double FEE_BOOKS = 300.00;
    private static final double TAX_RATE = 0.05; // 5%

    // UI Components
    private CheckBox cbTuition, cbTransport, cbBooks;
    private TextView tvSubtotal, tvTax, tvTotal;
    private CardView btnIssueInvoice;
    private ImageView btnBack;

    private DatabaseHelper dbHelper;
    // Hardcoded for the prototype based on your XML text (Jason Statham)
    private final String TARGET_STUDENT_ID = "stud01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_generate_invoice);

        dbHelper = new DatabaseHelper(this);

        // Initialize UI
        btnBack = findViewById(R.id.btn_back); // Ensure you added this ID to the back arrow ImageView
        cbTuition = findViewById(R.id.cb_tuition);
        cbTransport = findViewById(R.id.cb_transport);
        cbBooks = findViewById(R.id.cb_books);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTax = findViewById(R.id.tv_tax);
        tvTotal = findViewById(R.id.tv_total);
        btnIssueInvoice = findViewById(R.id.btn_issue_invoice);

        // Set Listeners for Calculation
        cbTuition.setOnCheckedChangeListener((buttonView, isChecked) -> calculateTotals());
        cbTransport.setOnCheckedChangeListener((buttonView, isChecked) -> calculateTotals());
        cbBooks.setOnCheckedChangeListener((buttonView, isChecked) -> calculateTotals());

        // Back Button Logic
        btnBack.setOnClickListener(v -> finish());

        // Issue Invoice Logic
        btnIssueInvoice.setOnClickListener(v -> issueInvoice());

        // Initial Calculation
        calculateTotals();
    }

    private void calculateTotals() {
        double subtotal = 0.0;

        if (cbTuition.isChecked()) subtotal += FEE_TUITION;
        if (cbTransport.isChecked()) subtotal += FEE_TRANSPORT;
        if (cbBooks.isChecked()) subtotal += FEE_BOOKS;

        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        // Update UI
        tvSubtotal.setText(String.format(Locale.getDefault(), "₱%,.2f", subtotal));
        tvTax.setText(String.format(Locale.getDefault(), "₱%,.2f", tax));
        tvTotal.setText(String.format(Locale.getDefault(), "₱%,.2f", total));
    }

    private void issueInvoice() {
        if (!cbTuition.isChecked() && !cbTransport.isChecked() && !cbBooks.isChecked()) {
            Toast.makeText(this, "Please select at least one fee item.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        try {
            db.beginTransaction();

            if (cbTuition.isChecked()) saveFeeItem(db, "Tuition Fee (Term 1)", FEE_TUITION, currentDate);
            if (cbTransport.isChecked()) saveFeeItem(db, "Transport (Bus Route A)", FEE_TRANSPORT, currentDate);
            if (cbBooks.isChecked()) saveFeeItem(db, "Textbooks & Materials", FEE_BOOKS, currentDate);

            // Optional: You could save the Tax as a separate line item if needed
            // saveFeeItem(db, "Tax (5%)", (calculateSubtotal() * TAX_RATE), currentDate);

            db.setTransactionSuccessful();
            Toast.makeText(this, "Invoice Issued Successfully!", Toast.LENGTH_LONG).show();
            finish(); // Close activity
        } catch (Exception e) {
            Toast.makeText(this, "Error generating invoice", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void saveFeeItem(SQLiteDatabase db, String description, double amount, String date) {
        ContentValues values = new ContentValues();
        values.put("student_id", TARGET_STUDENT_ID);
        values.put("description", description);
        values.put("amount", amount);
        values.put("type", "Bill"); // Type 'Bill' increases the balance
        values.put("date", date);
        db.insert("fees", null, values);
    }
}
package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminInvoiceDetailsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String studentId;

    // UI Components
    private TextView tvInvoiceNumber, tvStatus, tvBilledName, tvBilledGrade, tvDate;
    private TextView tvSubtotal, tvTotalPaid, tvBalanceDue;
    private LinearLayout llInvoiceItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_invoice_details);

        db = new DatabaseHelper(this);
        studentId = getIntent().getStringExtra("STUDENT_ID");

        initViews();
        loadInvoiceData();

        // --- Back Button ---
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // --- Download Button ---
        CardView btnDownload = findViewById(R.id.btn_download);
        if (btnDownload != null) {
            btnDownload.setOnClickListener(v -> {
                Toast.makeText(this, "Downloading Invoice PDF...", Toast.LENGTH_SHORT).show();
            });
        }

        // --- Share Button ---
        CardView btnShare = findViewById(R.id.btn_share);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> {
                Toast.makeText(this, "Opening Share Dialog...", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void initViews() {
        tvInvoiceNumber = findViewById(R.id.tv_invoice_number);
        tvStatus = findViewById(R.id.tv_status);
        tvBilledName = findViewById(R.id.tv_billed_name);
        tvBilledGrade = findViewById(R.id.tv_billed_grade);
        tvDate = findViewById(R.id.tv_due_date);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTotalPaid = findViewById(R.id.tv_total_paid);
        tvBalanceDue = findViewById(R.id.tv_balance_due);
        llInvoiceItems = findViewById(R.id.ll_invoice_items);
    }

    private void loadInvoiceData() {
        if (studentId == null) {
            Toast.makeText(this, "Error: No student selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 1. Load Student Details
        String studentName = db.getUserName(studentId);
        String studentClass = db.getStudentClass(studentId);

        tvBilledName.setText(studentName);
        tvBilledGrade.setText(studentClass);

        String today = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        tvDate.setText(today);
        tvInvoiceNumber.setText("#INV-" + studentId); // generating a mock invoice number based on ID

        // 2. Load Invoice Items (Fees)
        llInvoiceItems.removeAllViews();
        Cursor cursor = db.getStudentInvoices(studentId);
        double subtotal = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                subtotal += amount;

                addInvoiceItemRow(desc, amount);

            } while (cursor.moveToNext());
            cursor.close();
        }

        // 3. Load Payments & Calculate Status
        double totalPaid = db.getStudentTotalPaid(studentId);
        double balance = subtotal - totalPaid;

        tvSubtotal.setText(String.format("₱%.2f", subtotal));
        tvTotalPaid.setText(String.format("₱%.2f", totalPaid));
        tvBalanceDue.setText(String.format("₱%.2f", balance));

        // Update Status Badge
        if (balance <= 0 && subtotal > 0) {
            tvStatus.setText("PAID");
            tvStatus.setTextColor(Color.parseColor("#05CD99"));
            tvStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#E0FBF3")));
        } else if (totalPaid > 0) {
            tvStatus.setText("PARTIAL");
            tvStatus.setTextColor(Color.parseColor("#FFB547"));
            tvStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FFF5E0")));
        } else {
            tvStatus.setText("UNPAID");
            tvStatus.setTextColor(Color.RED);
            tvStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FFEEEE")));
        }
    }

    private void addInvoiceItemRow(String description, double amount) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16); // Bottom margin
        row.setLayoutParams(params);

        // Description TextView
        TextView tvDesc = new TextView(this);
        tvDesc.setText(description);
        tvDesc.setTextColor(Color.parseColor("#1B254B"));
        tvDesc.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        tvDesc.setLayoutParams(descParams);

        // Amount TextView
        TextView tvAmount = new TextView(this);
        tvAmount.setText(String.format("₱%.2f", amount));
        tvAmount.setTextColor(Color.parseColor("#1B254B"));
        tvAmount.setTypeface(null, Typeface.BOLD);

        row.addView(tvDesc);
        row.addView(tvAmount);

        llInvoiceItems.addView(row);
    }
}
package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminExpenseDetailsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int expenseId;

    // UI Components
    private TextView tvStatus, tvAmount, tvTitle, tvCategory, tvRequester, tvDate, tvDesc;
    // FIX: Changed from Button to TextView to match the XML layout
    private TextView btnApprove, btnReject;
    private LinearLayout footerActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_expense_details);

        dbHelper = new DatabaseHelper(this);

        // Get Expense ID from Intent
        expenseId = getIntent().getIntExtra("EXPENSE_ID", -1);

        initViews();
        loadExpenseDetails();

        // Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Action Buttons
        btnApprove.setOnClickListener(v -> updateStatus("Approved"));
        btnReject.setOnClickListener(v -> updateStatus("Rejected"));
    }

    private void initViews() {
        tvStatus = findViewById(R.id.tv_detail_status);
        tvAmount = findViewById(R.id.tv_detail_amount);
        tvTitle = findViewById(R.id.tv_detail_title);
        tvCategory = findViewById(R.id.tv_detail_category);
        tvRequester = findViewById(R.id.tv_detail_requester);
        tvDate = findViewById(R.id.tv_detail_date);
        tvDesc = findViewById(R.id.tv_detail_desc);

        // FIX: These IDs now map correctly to TextViews
        btnApprove = findViewById(R.id.btn_approve);
        btnReject = findViewById(R.id.btn_reject);
        footerActions = findViewById(R.id.footer_actions);
    }

    private void loadExpenseDetails() {
        if (expenseId == -1) {
            Toast.makeText(this, "Invalid Expense ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor cursor = dbHelper.getExpenseById(expenseId);
        if (cursor != null && cursor.moveToFirst()) {
            // Fetch Data
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            // Safe handling for requester ID
            int requesterIndex = cursor.getColumnIndex("requested_by");
            String requesterId = (requesterIndex != -1) ? cursor.getString(requesterIndex) : null;

            // Get Requester Name safely
            String requesterName = "Unknown";
            if (requesterId != null) {
                requesterName = dbHelper.getUserName(requesterId);
            }

            // Populate UI
            tvTitle.setText(title);
            tvCategory.setText(category);
            tvAmount.setText(String.format("$%,.2f", amount));
            tvDate.setText(date);
            tvDesc.setText(desc);
            tvRequester.setText(requesterName + (requesterId != null ? " (" + requesterId + ")" : ""));
            tvStatus.setText(status != null ? status.toUpperCase() : "PENDING");

            // Style Status & Buttons based on state
            updateUIForStatus(status != null ? status : "Pending");

            cursor.close();
        } else {
            Toast.makeText(this, "Expense not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateUIForStatus(String status) {
        switch (status) {
            case "Approved":
                tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
                // Use ContextCompat or simple logic if getColorStateList issues arise
                tvStatus.setBackgroundResource(android.R.color.transparent);
                footerActions.setVisibility(View.GONE); // Hide buttons if already processed
                break;
            case "Rejected":
                tvStatus.setTextColor(Color.parseColor("#E53935")); // Red
                tvStatus.setBackgroundResource(android.R.color.transparent);
                footerActions.setVisibility(View.GONE); // Hide buttons
                break;
            default: // Pending
                tvStatus.setTextColor(Color.parseColor("#FF9800")); // Orange
                footerActions.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void updateStatus(String newStatus) {
        boolean success = dbHelper.updateExpenseStatus(expenseId, newStatus);
        if (success) {
            Toast.makeText(this, "Expense " + newStatus, Toast.LENGTH_SHORT).show();
            loadExpenseDetails(); // Refresh UI to hide buttons
        } else {
            Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.Calendar;
import java.util.Locale;

public class AdminExpensesActivity extends AppCompatActivity {

    DatabaseHelper db;
    SessionManager session;
    LinearLayout llExpensesList;
    TextView tvTotalClaimed, tvPendingClaims;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_expenses);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Initialize Views
        llExpensesList = findViewById(R.id.ll_expenses_list);
        tvTotalClaimed = findViewById(R.id.tv_total_claimed);
        tvPendingClaims = findViewById(R.id.tv_pending_claims);

        // Back Button
        findViewById(R.id.btn_back_expenses).setOnClickListener(v -> finish());

        // Add Button
        findViewById(R.id.fab_add_expense).setOnClickListener(v -> showAddExpenseBottomSheet());

        loadExpenses();
    }

    private void loadExpenses() {
        llExpensesList.removeAllViews();
        Cursor cursor = db.getAllExpenses();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get Data Safely
                // Handle ID column being named either "id" (alias) or "expense_id" (table column)
                int idIndex = cursor.getColumnIndex("id");
                if (idIndex == -1) idIndex = cursor.getColumnIndex("expense_id");

                int id = (idIndex != -1) ? cursor.getInt(idIndex) : -1;

                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                // Safe handling of requested_by
                int reqByIndex = cursor.getColumnIndex("requested_by");
                String requestedBy = (reqByIndex != -1) ? cursor.getString(reqByIndex) : null;

                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                // FIX: Get Name Safely (Check if JOINed full_name exists, else fetch via helper)
                String requesterName = "Unknown";
                int nameIndex = cursor.getColumnIndex("full_name");

                if (nameIndex != -1 && cursor.getString(nameIndex) != null) {
                    // Use the name directly from the query (Fastest)
                    requesterName = cursor.getString(nameIndex);
                } else if (requestedBy != null) {
                    // Fallback: Fetch name using helper (slower but works if JOIN missing)
                    requesterName = db.getUserName(requestedBy);
                }

                // Inflate Row (using item_expense_row.xml)
                View itemView = inflater.inflate(R.layout.item_expense_row, llExpensesList, false);

                // Find Views
                TextView tvTitle = itemView.findViewById(R.id.tv_expense_title);
                TextView tvCategory = itemView.findViewById(R.id.tv_expense_category); // Reuse for details
                TextView tvAmount = itemView.findViewById(R.id.tv_expense_amount);

                // Populate Data
                tvTitle.setText(title);
                // Combine Date and Requester into the subtitle
                tvCategory.setText(date + " • " + requesterName);
                tvAmount.setText(String.format("$%.2f", amount));

                // Style Amount Color based on Status
                if ("Approved".equalsIgnoreCase(status)) {
                    tvAmount.setTextColor(android.graphics.Color.parseColor("#4CAF50")); // Green
                } else if ("Rejected".equalsIgnoreCase(status)) {
                    tvAmount.setTextColor(android.graphics.Color.parseColor("#F44336")); // Red
                } else {
                    tvAmount.setTextColor(android.graphics.Color.parseColor("#FF9800")); // Orange (Pending)
                }

                // Click Listener to Open Details
                final int finalId = id;
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(AdminExpensesActivity.this, AdminExpenseDetailsActivity.class);
                    intent.putExtra("EXPENSE_ID", finalId);
                    startActivity(intent);
                });

                llExpensesList.addView(itemView);

            } while (cursor.moveToNext());
            cursor.close();
        }

        updateSummaryCards();
    }

    private void updateSummaryCards() {
        double total = db.getTotalClaimedAmount();
        double pending = db.getPendingAmount();

        tvTotalClaimed.setText("$" + String.format("%.2f", total));
        tvPendingClaims.setText("$" + String.format("%.2f", pending));
    }

    private void showAddExpenseBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        // Inflate using admin_add_expenses.xml
        View sheetView = LayoutInflater.from(this).inflate(R.layout.admin_add_expenses, null);

        // Fixed IDs matched to admin_add_expenses.xml
        EditText etTitle = sheetView.findViewById(R.id.et_expense_title);
        EditText etAmount = sheetView.findViewById(R.id.et_expense_amount);
        TextView tvDate = sheetView.findViewById(R.id.tv_selected_date);
        Spinner spCategory = sheetView.findViewById(R.id.spinner_category);

        CardView btnPickDate = sheetView.findViewById(R.id.btn_pick_date);
        CardView btnSubmit = sheetView.findViewById(R.id.btn_submit_expense);
        ImageView btnClose = sheetView.findViewById(R.id.btn_close_sheet);

        // Setup Date Picker
        btnPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                // Ensure correct date format YYYY-MM-DD
                String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
                tvDate.setText(selectedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Setup Spinner
        String[] categories = {"Supplies", "Maintenance", "Events", "Travel", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        // Close Button
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Submit Logic
        btnSubmit.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String amountStr = etAmount.getText().toString();
            String date = tvDate.getText().toString();
            String category = spCategory.getSelectedItem().toString();

            if (title.isEmpty() || amountStr.isEmpty() || date.equals("Select Date")) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            String currentUser = session.getUserId();

            boolean success = db.addExpense(title, currentUser, category, amount, "Pending", date);
            if (success) {
                Toast.makeText(this, "Expense Claim Submitted!", Toast.LENGTH_SHORT).show();
                loadExpenses(); // Refresh List
                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(this, "Error submitting claim", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.setContentView(sheetView);

        // Transparent background for rounded corners
        if (bottomSheetDialog.getWindow() != null) {
            bottomSheetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        bottomSheetDialog.show();
    }
}
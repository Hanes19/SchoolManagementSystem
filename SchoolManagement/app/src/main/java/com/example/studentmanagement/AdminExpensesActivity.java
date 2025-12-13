package com.example.studentmanagement;

import android.app.DatePickerDialog;
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

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String requestedBy = cursor.getString(cursor.getColumnIndexOrThrow("requested_by"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                View itemView = LayoutInflater.from(this).inflate(R.layout.item_expense_row, llExpensesList, false);

                TextView tvTitle = itemView.findViewById(R.id.tv_expense_title);
                TextView tvAmount = itemView.findViewById(R.id.tv_amount);
                TextView tvRequester = itemView.findViewById(R.id.tv_requested_by);
                TextView tvStatus = itemView.findViewById(R.id.tv_status);
                TextView tvDate = itemView.findViewById(R.id.tv_date);

                tvTitle.setText(title);
                tvAmount.setText("$" + String.format("%.2f", Double.parseDouble(amount)));
                tvRequester.setText("Requested by: " + db.getUserName(requestedBy));
                tvStatus.setText(status);
                tvDate.setText(date);

                // Style based on status
                if ("Approved".equalsIgnoreCase(status)) {
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
                    tvStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#E8F5E9")));
                } else {
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#FF9800"));
                    tvStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FFF3E0")));
                }

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
        View sheetView = LayoutInflater.from(this).inflate(R.layout.admin_add_expenses, null);

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
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
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
            String currentUser = session.getUserId(); // Get ID from session

            boolean success = db.addExpense(title, amount, date, category, currentUser);
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
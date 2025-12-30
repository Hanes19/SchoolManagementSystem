package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AdminAddExpensesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etName, etAmount;
    // Description field removed because it doesn't exist in your XML
    private TextView tvDate;
    private Spinner spCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_expenses);

        db = new DatabaseHelper(this);

        // Fixed IDs to match admin_add_expenses.xml
        etName = findViewById(R.id.et_expense_title);      // Was et_expense_name
        etAmount = findViewById(R.id.et_expense_amount);   // Was et_amount
        tvDate = findViewById(R.id.tv_selected_date);      // Was tv_date
        spCategory = findViewById(R.id.spinner_category);  // Was sp_category

        setupCategorySpinner();

        // Allow clicking either the text or the card container to pick a date
        findViewById(R.id.btn_pick_date).setOnClickListener(v -> showDatePicker());
        tvDate.setOnClickListener(v -> showDatePicker());

        // Fixed ID for save button
        findViewById(R.id.btn_submit_expense).setOnClickListener(v -> saveExpense()); // Was btn_save_expense

        // Close button logic
        findViewById(R.id.btn_close_sheet).setOnClickListener(v -> finish());
    }

    private void setupCategorySpinner() {
        String[] categories = {"Salary", "Maintenance", "Utilities", "Stationery", "Events", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spCategory.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, y, m, d) ->
                tvDate.setText(y + "-" + (m + 1) + "-" + d),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveExpense() {
        String name = etName.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String date = tvDate.getText().toString();
        String category = "";

        if (spCategory.getSelectedItem() != null) {
            category = spCategory.getSelectedItem().toString();
        }

        // Description is empty because the field is missing in XML
        String desc = "";

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(amountStr) || date.contains("Select")) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.addExpense(name, category, amount, date, desc)) {
            Toast.makeText(this, "Expense Added Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error adding expense", Toast.LENGTH_SHORT).show();
        }
    }
}
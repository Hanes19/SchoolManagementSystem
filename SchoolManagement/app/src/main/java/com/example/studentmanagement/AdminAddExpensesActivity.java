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
    private EditText etName, etAmount, etDesc;
    private TextView tvDate;
    private Spinner spCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_expenses);

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.et_expense_name);
        etAmount = findViewById(R.id.et_amount);
        etDesc = findViewById(R.id.et_description);
        tvDate = findViewById(R.id.tv_date);
        spCategory = findViewById(R.id.sp_category);

        // Header back button (if present)
        // findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        setupCategorySpinner();

        tvDate.setOnClickListener(v -> showDatePicker());

        findViewById(R.id.btn_save_expense).setOnClickListener(v -> saveExpense());
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
        String desc = etDesc.getText().toString();
        String category = spCategory.getSelectedItem().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(amountStr) || date.contains("Select")) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        if (db.addExpense(name, category, amount, date, desc)) {
            Toast.makeText(this, "Expense Added Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error adding expense", Toast.LENGTH_SHORT).show();
        }
    }
}

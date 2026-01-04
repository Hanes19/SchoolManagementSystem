package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import android.widget.Button;
import androidx.cardview.widget.CardView; // Only if you use CardView

public class AdminAddAcademicSessionActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etSessionName;
    private TextView tvStartDate, tvEndDate;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_academic_session);

        db = new DatabaseHelper(this);

        etSessionName = findViewById(R.id.et_session_name);
        tvStartDate = findViewById(R.id.tv_start_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        btnSave = findViewById(R.id.btn_save_session);
        ImageView btnBack = findViewById(R.id.btn_back);

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        tvStartDate.setOnClickListener(v -> showDatePicker(tvStartDate));
        tvEndDate.setOnClickListener(v -> showDatePicker(tvEndDate));

        btnSave.setOnClickListener(v -> saveSession());
    }

    private void showDatePicker(TextView targetView) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = year + "-" + (month + 1) + "-" + dayOfMonth;
            targetView.setText(date);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveSession() {
        String name = etSessionName.getText().toString().trim();
        String start = tvStartDate.getText().toString();
        String end = tvEndDate.getText().toString();

        if (name.isEmpty() || start.contains("Select") || end.contains("Select")) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make sure this matches your DatabaseHelper method signature!
        // If your helper expects 4 args, use: db.addAcademicSession(name, start, end, false);
        long result = db.addAcademicSession(name, start, end);

        if (result != -1) {
            Toast.makeText(this, "Session Added!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error adding session", Toast.LENGTH_SHORT).show();
        }
    }
}
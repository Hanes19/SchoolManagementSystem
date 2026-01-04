package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import androidx.cardview.widget.CardView;

public class AdminAddAcademicSessionActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etSessionName;
    private TextView tvStartDate, tvEndDate, tvHeaderTitle;
    private View btnSave; // Changed to View to support CardView click
    private String sessionId = null;
    private boolean isEditMode = false;

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

        // Find header title to change text in Edit Mode
        // Assumes structure: LinearLayout(header) -> TextView at index 1
        LinearLayout header = findViewById(R.id.header);
        if (header != null && header.getChildCount() > 1 && header.getChildAt(1) instanceof TextView) {
            tvHeaderTitle = (TextView) header.getChildAt(1);
        }

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        tvStartDate.setOnClickListener(v -> showDatePicker(tvStartDate));
        tvEndDate.setOnClickListener(v -> showDatePicker(tvEndDate));

        // [NEW] Check for Edit Mode
        if (getIntent().hasExtra("SESSION_ID")) {
            isEditMode = true;
            sessionId = getIntent().getStringExtra("SESSION_ID");
            etSessionName.setText(getIntent().getStringExtra("SESSION_NAME"));
            tvStartDate.setText(getIntent().getStringExtra("START_DATE"));
            tvEndDate.setText(getIntent().getStringExtra("END_DATE"));

            if (tvHeaderTitle != null) {
                tvHeaderTitle.setText("Edit Academic Session");
            }
        }

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

        if (isEditMode) {
            // [NEW] Update Logic
            boolean success = db.updateAcademicSession(sessionId, name, start, end);
            if (success) {
                Toast.makeText(this, "Session Updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error updating session", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Create Logic
            long result = db.addAcademicSession(name, start, end);
            if (result != -1) {
                Toast.makeText(this, "Session Added!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error adding session", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
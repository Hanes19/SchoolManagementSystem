package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.Calendar;

public class TeacherAddHomeworkActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etTitle, etPoints, etInstructions;
    private TextView tvDueDate, tvClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_add_homework);

        db = new DatabaseHelper(this);

        etTitle = findViewById(R.id.et_title);
        etPoints = findViewById(R.id.et_points);
        etInstructions = findViewById(R.id.et_instructions);
        tvDueDate = findViewById(R.id.tv_due_date);
        tvClass = findViewById(R.id.tv_class_selector);

        findViewById(R.id.btn_close).setOnClickListener(v -> finish());

        findViewById(R.id.btn_due_date).setOnClickListener(v -> showDatePicker());

        CardView btnCreate = findViewById(R.id.btn_create_assignment);
        btnCreate.setOnClickListener(v -> saveAssignment());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) ->
                        tvDueDate.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth),
                year, month, day);
        datePickerDialog.show();
    }

    private void saveAssignment() {
        String title = etTitle.getText().toString().trim();
        String pointsStr = etPoints.getText().toString().trim();
        String desc = etInstructions.getText().toString().trim();
        String date = tvDueDate.getText().toString();
        String className = tvClass.getText().toString(); // Currently fixed in XML or Logic

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(pointsStr)) {
            Toast.makeText(this, "Title and Points are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int points = Integer.parseInt(pointsStr);
        String subject = "Chemistry"; // Hardcoded for demo, or from spinner

        if (db.addAssignment(title, className, subject, date, points, desc)) {
            Toast.makeText(this, "Assignment Published!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving assignment", Toast.LENGTH_SHORT).show();
        }
    }
}
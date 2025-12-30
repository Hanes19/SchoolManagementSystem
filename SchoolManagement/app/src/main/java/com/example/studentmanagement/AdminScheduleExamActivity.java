package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AdminScheduleExamActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Spinner spCategory, spClass, spSubject;
    private TextView tvDate, tvTime;
    private EditText etRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_schedule_exams);

        db = new DatabaseHelper(this);

        spCategory = findViewById(R.id.sp_category);
        spClass = findViewById(R.id.sp_class);
        spSubject = findViewById(R.id.sp_subject);
        tvDate = findViewById(R.id.tv_exam_date); // Ensure ID added
        tvTime = findViewById(R.id.tv_exam_time); // Ensure ID added
        etRoom = findViewById(R.id.et_room_no);   // Ensure ID added

        setupSpinners();

        findViewById(R.id.header).setOnClickListener(v -> finish()); // Back button in header

        tvDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> tvDate.setText(y + "-" + (m+1) + "-" + d),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        tvTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, h, m) -> tvTime.setText(h + ":" + m),
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
        });

        findViewById(R.id.btn_save_schedule).setOnClickListener(v -> saveSchedule()); // Ensure ID added
    }

    private void setupSpinners() {
        // Dummy data for demo
        String[] classes = {"Grade 10", "Grade 11", "Grade 12"};
        String[] subjects = {"Math", "Science", "English", "History"};
        String[] exams = {"Midterm 2025", "Finals 2025"}; // Should fetch from DB

        spClass.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes));
        spSubject.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subjects));
        spCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, exams));
    }

    private void saveSchedule() {
        // In real app, map Exam Name to ID
        if (db.scheduleExam(1, spClass.getSelectedItem().toString(),
                spSubject.getSelectedItem().toString(),
                tvDate.getText().toString(),
                tvTime.getText().toString(),
                etRoom.getText().toString())) {
            Toast.makeText(this, "Exam Scheduled Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error Scheduling Exam", Toast.LENGTH_SHORT).show();
        }
    }
}
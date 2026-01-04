package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminScheduleExamActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Spinner spCategory, spClass, spSubject;
    private TextView tvDate, tvTime;
    private EditText etRoom;
    private List<String> examIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_schedule_exams);

        db = new DatabaseHelper(this);

        spCategory = findViewById(R.id.sp_category);
        spClass = findViewById(R.id.sp_class);
        spSubject = findViewById(R.id.sp_subject);
        tvDate = findViewById(R.id.tv_exam_date);
        tvTime = findViewById(R.id.tv_exam_time);
        etRoom = findViewById(R.id.et_room_no);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_save_schedule).setOnClickListener(v -> saveSchedule());

        tvDate.setOnClickListener(v -> showDatePicker());
        tvTime.setOnClickListener(v -> showTimePicker());

        loadSpinners();
    }

    private void loadSpinners() {
        // 1. Load Exam Categories
        List<String> exams = new ArrayList<>();
        examIds.clear();
        Cursor cExams = db.getAllExamCategories();
        if (cExams != null && cExams.moveToFirst()) {
            do {
                examIds.add(cExams.getString(0));
                exams.add(cExams.getString(1));
            } while (cExams.moveToNext());
            cExams.close();
        }
        ArrayAdapter<String> adapterExams = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, exams);
        spCategory.setAdapter(adapterExams);

        // 2. Load Classes
        List<String> classes = new ArrayList<>();
        Cursor cClasses = db.getAllClasses();
        if (cClasses != null && cClasses.moveToFirst()) {
            do {
                classes.add(cClasses.getString(1) + "-" + cClasses.getString(2));
            } while (cClasses.moveToNext());
            cClasses.close();
        }
        ArrayAdapter<String> adapterClasses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
        spClass.setAdapter(adapterClasses);

        // 3. Load Subjects (Hardcoded for now, or fetch from DB if you have a Subjects table)
        String[] subjects = {"Mathematics", "English", "Science", "History", "Physics", "Chemistry", "Computer Science"};
        ArrayAdapter<String> adapterSubjects = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subjects);
        spSubject.setAdapter(adapterSubjects);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, y, m, d) ->
                tvDate.setText(y + "-" + (m+1) + "-" + d),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, h, m) ->
                tvTime.setText(String.format("%02d:%02d", h, m)),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void saveSchedule() {
        if (spCategory.getSelectedItem() == null || spClass.getSelectedItem() == null) {
            Toast.makeText(this, "Please select Exam and Class", Toast.LENGTH_SHORT).show();
            return;
        }

        String examId = examIds.get(spCategory.getSelectedItemPosition());
        String className = spClass.getSelectedItem().toString();
        String subject = spSubject.getSelectedItem().toString();
        String date = tvDate.getText().toString();
        String time = tvTime.getText().toString();
        String room = etRoom.getText().toString();

        if (date.contains("Select") || time.contains("Select")) {
            Toast.makeText(this, "Please select Date and Time", Toast.LENGTH_SHORT).show();
            return;
        }

        db.addExamSchedule(examId, className, subject, date, time, room);
        Toast.makeText(this, "Exam Scheduled Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
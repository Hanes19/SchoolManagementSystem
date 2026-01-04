package com.example.studentmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminScheduleExamActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Spinner spCategory, spClass, spSubject;
    private TextView tvDate, tvTime;
    private EditText etRoom;
    private Button btnSave;
    private ImageView btnBack;
    private List<String> examIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_schedule_exams);

        db = new DatabaseHelper(this);

        // Initialize Views
        spCategory = findViewById(R.id.sp_category);
        spClass = findViewById(R.id.sp_class);
        spSubject = findViewById(R.id.sp_subject);
        tvDate = findViewById(R.id.tv_exam_date);
        tvTime = findViewById(R.id.tv_exam_time);
        etRoom = findViewById(R.id.et_room_no);
        btnSave = findViewById(R.id.btn_save_schedule);
        btnBack = findViewById(R.id.btn_back);

        // --- FORCE TEXT COLORS (Fix for White Text Issue) ---
        int darkColor = Color.parseColor("#1B254B");
        if (etRoom != null) {
            etRoom.setTextColor(darkColor);
            etRoom.setHintTextColor(Color.parseColor("#A3AED0"));
        }
        if (tvDate != null) tvDate.setTextColor(darkColor);
        if (tvTime != null) tvTime.setTextColor(darkColor);

        // Null Safety Check & Listeners
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
        if (tvDate != null) tvDate.setOnClickListener(v -> showDatePicker());
        if (tvTime != null) tvTime.setOnClickListener(v -> showTimePicker());
        if (btnSave != null) btnSave.setOnClickListener(v -> saveSchedule());

        loadSpinners();
    }

    // --- CUSTOM ADAPTER TO FORCE BLACK TEXT IN SPINNERS ---
    private ArrayAdapter<String> getColoredAdapter(List<String> items) {
        return new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(Color.parseColor("#1B254B")); // Force Dark Blue
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(Color.parseColor("#1B254B")); // Force Dark Blue
                }
                return view;
            }
        };
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
        } else {
            exams.add("No Exams Found");
        }

        // Use the custom colored adapter
        ArrayAdapter<String> adapterExams = getColoredAdapter(exams);
        adapterExams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (spCategory != null) spCategory.setAdapter(adapterExams);

        // 2. Load Classes
        List<String> classes = new ArrayList<>();
        Cursor cClasses = db.getAllClasses();
        if (cClasses != null && cClasses.moveToFirst()) {
            do {
                classes.add(cClasses.getString(1) + "-" + cClasses.getString(2));
            } while (cClasses.moveToNext());
            cClasses.close();
        } else {
            classes.add("No Classes Found");
        }

        ArrayAdapter<String> adapterClasses = getColoredAdapter(classes);
        adapterClasses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (spClass != null) spClass.setAdapter(adapterClasses);

        // 3. Load Subjects
        String[] subjects = {"Mathematics", "English", "Science", "History", "Physics", "Chemistry", "Computer Science"};
        List<String> subjectList = new ArrayList<>();
        for(String s : subjects) subjectList.add(s);

        ArrayAdapter<String> adapterSubjects = getColoredAdapter(subjectList);
        adapterSubjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (spSubject != null) spSubject.setAdapter(adapterSubjects);
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
        if (spCategory == null || spCategory.getSelectedItem() == null || examIds.isEmpty()) {
            Toast.makeText(this, "Please select an Exam", Toast.LENGTH_SHORT).show();
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
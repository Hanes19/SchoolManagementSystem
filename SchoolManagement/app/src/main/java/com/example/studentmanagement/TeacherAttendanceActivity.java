package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TeacherAttendanceActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView rvList;
    private AttendanceAdapter adapter;
    private List<AttendanceModel> list;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_attendance_sheet);

        db = new DatabaseHelper(this);
        rvList = findViewById(R.id.rv_attendance_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));

        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        list = new ArrayList<>();
        adapter = new AttendanceAdapter(list);
        rvList.setAdapter(adapter);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_save).setOnClickListener(v -> saveAttendance());

        findViewById(R.id.btn_mark_all).setOnClickListener(v -> {
            for(AttendanceModel m : list) m.setStatus("Present");
            adapter.notifyDataSetChanged();
        });

        loadStudents();
    }

    private void loadStudents() {
        list.clear();
        Cursor cursor = db.getStudentsWithAttendance(currentDate, "Grade 10-A");

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                if (status == null) status = "Absent"; // Default if not marked yet

                list.add(new AttendanceModel(id, name, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void saveAttendance() {
        for (AttendanceModel model : list) {
            db.saveAttendance(model.getId(), currentDate, model.getStatus(), "Grade 10-A");
        }
        Toast.makeText(this, "Attendance Submitted Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
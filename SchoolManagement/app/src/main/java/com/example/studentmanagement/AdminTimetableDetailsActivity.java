package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminTimetableDetailsActivity extends AppCompatActivity {

    DatabaseHelper db;
    String scheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_timetable_details);

        db = new DatabaseHelper(this);

        // Initialize Views
        TextView tvSubject = findViewById(R.id.tv_detail_subject);
        TextView tvClass = findViewById(R.id.tv_detail_class);
        TextView tvTeacher = findViewById(R.id.tv_detail_teacher);
        TextView tvTime = findViewById(R.id.tv_detail_time);
        TextView tvRoom = findViewById(R.id.tv_detail_room);
        ImageView btnBack = findViewById(R.id.btn_back);
        View btnDelete = findViewById(R.id.btn_delete_schedule);

        // Get Data from Intent
        if (getIntent() != null) {
            scheduleId = getIntent().getStringExtra("SCHEDULE_ID");
            tvSubject.setText(getIntent().getStringExtra("SUBJECT"));
            tvClass.setText(getIntent().getStringExtra("CLASS_NAME"));
            tvTeacher.setText(getIntent().getStringExtra("TEACHER"));
            tvTime.setText(getIntent().getStringExtra("TIME"));
            tvRoom.setText("Room: " + getIntent().getStringExtra("ROOM"));
        }

        // Back Action
        btnBack.setOnClickListener(v -> finish());

        // Delete Action
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Class")
                .setMessage("Are you sure you want to remove this class from the schedule?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (scheduleId != null) {
                        boolean isDeleted = db.deleteSchedule(scheduleId);
                        if (isDeleted) {
                            Toast.makeText(this, "Class deleted successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Return to master list
                        } else {
                            Toast.makeText(this, "Failed to delete class", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
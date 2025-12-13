package com.example.studentmanagement;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AdminMasterTimetableActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_master_timetable);

        findViewById(R.id.header).setOnClickListener(v -> finish());
        // Logic to load all timetables goes here
    }
}
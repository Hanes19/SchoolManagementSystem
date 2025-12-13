package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminClassTimetableActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_class_timetable);

        findViewById(R.id.header).setOnClickListener(v -> finish());

        // Get Data from Intent
        String className = getIntent().getStringExtra("CLASS_NAME");
        if(className != null) {
            // There isn't an ID for the title in the XML provided,
            // but normally you would do:
            // ((TextView)findViewById(R.id.tv_class_title)).setText(className);
        }
    }
}
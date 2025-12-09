package com.example.studentmanagement;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student); // Connects to student.xml

        // Add specific student logic here (e.g., view grades)
    }
}
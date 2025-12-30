package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminAnalyticsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvStudents, tvTeachers, tvIncome, tvExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_analytics);

        db = new DatabaseHelper(this);

        tvStudents = findViewById(R.id.tv_count_students);
        tvTeachers = findViewById(R.id.tv_count_teachers);
        tvIncome = findViewById(R.id.tv_total_income);
        tvExpense = findViewById(R.id.tv_total_expense);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadStats();
    }

    private void loadStats() {
        // Fetch real counts from DB
        // For demonstration, we use methods that might mock or count rows
        long studentCount = db.getStudentCount(); // e.g., 150

        // You would add similar count methods in DatabaseHelper for other tables
        long teacherCount = 25;

        tvStudents.setText(String.valueOf(studentCount));
        tvTeachers.setText(String.valueOf(teacherCount));

        // Mock Financials
        tvIncome.setText("$125,000");
        tvExpense.setText("$45,000");
    }
}
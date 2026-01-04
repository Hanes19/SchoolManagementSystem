package com.example.studentmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class AdminAnalyticsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvStudents, tvTeachers, tvIncome, tvExpense;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_analytics);

        db = new DatabaseHelper(this);

        tvStudents = findViewById(R.id.tv_count_students);
        tvTeachers = findViewById(R.id.tv_count_teachers);
        tvIncome = findViewById(R.id.tv_total_income);
        tvExpense = findViewById(R.id.tv_total_expense);

        // Make sure this ID matches your XML
        barChart = findViewById(R.id.barChart);

        // Fix the back button crash
        if (findViewById(R.id.back_btn) != null) {
            findViewById(R.id.back_btn).setOnClickListener(v -> finish());
        }

        loadStats();
        setupChartData();
    }

    private void loadStats() {
        if (tvStudents != null) tvStudents.setText(String.valueOf(db.getStudentCount()));
        // Mock data for other fields
        if (tvTeachers != null) tvTeachers.setText("25");
        if (tvIncome != null) tvIncome.setText("$125,000");
        if (tvExpense != null) tvExpense.setText("$45,000");
    }

    private void setupChartData() {
        if (barChart == null) return;

        // 1. Data Entries
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 12000f));
        entries.add(new BarEntry(1, 15000f));
        entries.add(new BarEntry(2, 11000f));
        entries.add(new BarEntry(3, 18000f));
        entries.add(new BarEntry(4, 22000f));
        entries.add(new BarEntry(5, 20000f));

        // 2. Dataset Setup
        BarDataSet dataSet = new BarDataSet(entries, "Monthly Income");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        // Format the numbers on top of bars to be clean (e.g. "12k")
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int)(value/1000) + "k";
            }
        });

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);

        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);
        barChart.animateY(1000);
        barChart.setExtraBottomOffset(10f);

        // 3. X-Axis Setup (Months)
        String[] months = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        // 4. Y-Axis Setup
        barChart.getAxisRight().setEnabled(false); // Hide right axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        // 5. ATTACH THE MARKER VIEW (TOOLTIP)
        CustomMarkerView markerView = new CustomMarkerView(this, R.layout.custom_marker_view);
        markerView.setChartView(barChart);
        barChart.setMarker(markerView);

        barChart.invalidate();
    }
}
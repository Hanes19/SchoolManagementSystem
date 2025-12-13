package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class StudentAttendanceActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView calendarRecycler;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        studentId = getIntent().getStringExtra("STUDENT_ID");
        dbHelper = new DatabaseHelper(this);

        ImageView back = findViewById(R.id.btn_back_attendance);
        back.setOnClickListener(v -> finish());

        calendarRecycler = findViewById(R.id.calendar_recycler_view);
        calendarRecycler.setLayoutManager(new GridLayoutManager(this, 7)); // 7 Days a week

        loadAttendance();
    }

    private void loadAttendance() {
        // Mocking 30 days for visual
        List<DayStatus> days = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            days.add(new DayStatus(String.valueOf(i), "None"));
        }

        // Fetch actual data
        Cursor cursor = dbHelper.getStudentAttendance(studentId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                // Simple parsing: if date ends in '01', mark day 1. (Logic simplified for demo)
                // In real app, match Year/Month/Day properly.
                try {
                    int dayInt = Integer.parseInt(date.substring(date.length() - 2));
                    if (dayInt > 0 && dayInt <= 30) {
                        days.get(dayInt - 1).status = status;
                    }
                } catch (Exception e) {}
            } while (cursor.moveToNext());
            cursor.close();
        }

        calendarRecycler.setAdapter(new CalendarAdapter(days));
    }

    // --- Inner Classes ---
    private static class DayStatus {
        String day; String status;
        DayStatus(String d, String s) { day = d; status = s; }
    }

    private class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.Holder> {
        List<DayStatus> list;
        CalendarAdapter(List<DayStatus> l) { list = l; }

        @NonNull @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView tv = new TextView(parent.getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(14);
            return new Holder(tv);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            DayStatus item = list.get(position);
            holder.tv.setText(item.day);

            // Color Logic
            if (item.status.equals("Present")) {
                holder.tv.setBackgroundResource(R.drawable.circle_outline_white); // Use a drawable or color
                holder.tv.setTextColor(Color.parseColor("#4CAF50")); // Green
            } else if (item.status.equals("Absent")) {
                holder.tv.setTextColor(Color.RED);
            } else {
                holder.tv.setTextColor(Color.BLACK);
            }
        }

        @Override
        public int getItemCount() { return list.size(); }
        class Holder extends RecyclerView.ViewHolder {
            TextView tv; Holder(View v) { super(v); tv = (TextView)v; }
        }
    }
}
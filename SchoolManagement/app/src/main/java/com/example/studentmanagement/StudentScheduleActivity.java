package com.example.studentmanagement;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentScheduleActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView recyclerView;
    // private TextView tvCurrentDay; // ID not found in XML
    private String studentId;
    private String currentDay = "Monday";
    private final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private ScheduleAdapter adapter;
    private List<ScheduleItem> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_schedule);

        db = new DatabaseHelper(this);
        studentId = getIntent().getStringExtra("STUDENT_ID");

        // 1. Match XML ID: recycler_schedule instead of ll_schedule_list
        recyclerView = findViewById(R.id.recycler_schedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scheduleList = new ArrayList<>();
        adapter = new ScheduleAdapter(scheduleList);
        recyclerView.setAdapter(adapter);

        // 2. Match XML ID: btn_back_schedule instead of btn_back
        findViewById(R.id.btn_back_schedule).setOnClickListener(v -> finish());

        // 3. Logic for tvCurrentDay and btn_day_filter is commented out
        // because these IDs (tv_current_day, btn_day_filter) do not exist in your provided XML.

        // tvCurrentDay = findViewById(R.id.tv_current_day);
        // findViewById(R.id.btn_day_filter).setOnClickListener(v -> showDayPicker());

        loadSchedule(currentDay);
    }

    private void showDayPicker() {
        new AlertDialog.Builder(this)
                .setTitle("Select Day")
                .setItems(days, (dialog, which) -> {
                    currentDay = days[which];
                    // tvCurrentDay.setText(currentDay); // Text view missing in XML
                    loadSchedule(currentDay);
                })
                .show();
    }

    private void loadSchedule(String day) {
        scheduleList.clear();
        String className = db.getStudentClass(studentId);
        Cursor cursor = db.getClassSchedule(className, day);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String start = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String end = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
                String room = cursor.getString(cursor.getColumnIndexOrThrow("room_no"));

                scheduleList.add(new ScheduleItem(subject, start, room));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Update the adapter to refresh the RecyclerView
        adapter.notifyDataSetChanged();
    }

    // Simple Model Class for the data
    private static class ScheduleItem {
        String subject, time, room;

        ScheduleItem(String subject, String time, String room) {
            this.subject = subject;
            this.time = time;
            this.room = room;
        }
    }

    // RecyclerView Adapter Implementation
    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
        private final List<ScheduleItem> items;

        ScheduleAdapter(List<ScheduleItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate your existing item layout
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_teacher_schedule_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ScheduleItem item = items.get(position);
            holder.tvTime.setText(item.time);
            holder.tvSubject.setText(item.subject);
            holder.tvDetails.setText("Room: " + item.room);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTime, tvSubject, tvDetails;

            ViewHolder(View itemView) {
                super(itemView);
                // IDs from your item_teacher_schedule_card.xml
                tvTime = itemView.findViewById(R.id.tv_start_time);
                tvSubject = itemView.findViewById(R.id.tv_subject);
                tvDetails = itemView.findViewById(R.id.tv_details);
            }
        }
    }
}
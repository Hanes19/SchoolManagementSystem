package com.example.studentmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor; // Ensure Cursor is imported
import java.util.ArrayList;
import java.util.List;

public class StudentScheduleActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView recyclerView;
    private String studentId;
    private String currentDay = "Monday";

    private ScheduleAdapter adapter;
    private List<ScheduleItem> scheduleList;

    // Arrays to hold view IDs for easier iteration
    private final int[] cardIds = {R.id.card_mon, R.id.card_tue, R.id.card_wed, R.id.card_thu, R.id.card_fri};
    private final int[] txtDayIds = {R.id.txt_day_mon, R.id.txt_day_tue, R.id.txt_day_wed, R.id.txt_day_thu, R.id.txt_day_fri};
    private final int[] txtDateIds = {R.id.txt_date_mon, R.id.txt_date_tue, R.id.txt_date_wed, R.id.txt_date_thu, R.id.txt_date_fri};
    private final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_schedule);

        db = new DatabaseHelper(this);
        studentId = getIntent().getStringExtra("STUDENT_ID");

        recyclerView = findViewById(R.id.recycler_schedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scheduleList = new ArrayList<>();
        adapter = new ScheduleAdapter(scheduleList);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btn_back_schedule).setOnClickListener(v -> finish());

        // Initialize day click listeners
        setupDayClickListeners();

        // Load default day (Monday)
        loadSchedule(currentDay);
        updateDayUI(0); // 0 index for Monday
    }

    private void setupDayClickListeners() {
        for (int i = 0; i < cardIds.length; i++) {
            final int index = i;
            CardView card = findViewById(cardIds[i]);
            card.setOnClickListener(v -> {
                currentDay = days[index];
                updateDayUI(index);
                loadSchedule(currentDay);
            });
        }
    }

    private void updateDayUI(int selectedIndex) {
        // Loop through all days to reset or highlight
        for (int i = 0; i < cardIds.length; i++) {
            CardView card = findViewById(cardIds[i]);
            TextView txtDay = findViewById(txtDayIds[i]);
            TextView txtDate = findViewById(txtDateIds[i]);

            if (i == selectedIndex) {
                // Selected State: Dark Blue Background, White Text
                card.setCardBackgroundColor(Color.parseColor("#1B254B"));
                txtDay.setTextColor(Color.WHITE);
                txtDate.setTextColor(Color.WHITE);
            } else {
                // Unselected State: White Background, Gray/Dark Text
                card.setCardBackgroundColor(Color.WHITE);
                txtDay.setTextColor(Color.parseColor("#A3AED0")); // Light Gray
                txtDate.setTextColor(Color.parseColor("#1B254B")); // Dark Blue
            }
        }
    }

    private void loadSchedule(String day) {
        scheduleList.clear();
        String className = db.getStudentClass(studentId);
        // Ensure db.getClassSchedule is implemented correctly in DatabaseHelper
        Cursor cursor = db.getClassSchedule(className, day);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Adjust column names to match your database schema exactly
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String start = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                // String end = cursor.getString(cursor.getColumnIndexOrThrow("end_time")); // Optional if needed
                String room = cursor.getString(cursor.getColumnIndexOrThrow("room_no"));

                scheduleList.add(new ScheduleItem(subject, start, room));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter.notifyDataSetChanged();
    }

    // --- Inner Classes ---

    private static class ScheduleItem {
        String subject, time, room;

        ScheduleItem(String subject, String time, String room) {
            this.subject = subject;
            this.time = time;
            this.room = room;
        }
    }

    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
        private final List<ScheduleItem> items;

        ScheduleAdapter(List<ScheduleItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
                tvTime = itemView.findViewById(R.id.tv_start_time);
                tvSubject = itemView.findViewById(R.id.tv_subject);
                tvDetails = itemView.findViewById(R.id.tv_details);
            }
        }
    }
}
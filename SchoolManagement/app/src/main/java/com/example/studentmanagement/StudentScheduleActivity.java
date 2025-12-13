package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class StudentScheduleActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private String currentDay = "Mon"; // Default

    // Day Selectors (Assuming 5 days for brevity, add IDs in XML if needed)
    // For this example, we will just load Monday by default.
    // You can bind the CardViews in the horizontal scroll to change 'currentDay' and call loadSchedule().

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_schedule);

        dbHelper = new DatabaseHelper(this);

        ImageView backBtn = findViewById(R.id.btn_back_schedule);
        backBtn.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_schedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadSchedule(currentDay);
    }

    private void loadSchedule(String day) {
        List<ScheduleItem> items = new ArrayList<>();
        Cursor cursor = dbHelper.getScheduleForDay(day);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String room = cursor.getString(cursor.getColumnIndexOrThrow("room"));
                String teacher = cursor.getString(cursor.getColumnIndexOrThrow("teacher_name"));
                items.add(new ScheduleItem(subject, startTime, room, teacher));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new ScheduleAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    // --- Inner Model Class ---
    private static class ScheduleItem {
        String subject, time, room, teacher;
        ScheduleItem(String s, String t, String r, String tea) {
            this.subject = s; this.time = t; this.room = r; this.teacher = tea;
        }
    }

    // --- Inner Adapter Class ---
    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
        List<ScheduleItem> list;
        ScheduleAdapter(List<ScheduleItem> list) { this.list = list; }

        @NonNull @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // We can reuse the card design programmatically or inflate a simple layout
            // Here creating a simple view for demonstration to avoid extra XML files
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ScheduleItem item = list.get(position);
            holder.text1.setText(item.time + " - " + item.subject);
            holder.text2.setText("Room: " + item.room + " | " + item.teacher);
            holder.text1.setTextColor(Color.parseColor("#1B254B"));
            holder.text1.setTextSize(16);
        }

        @Override
        public int getItemCount() { return list.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1, text2;
            ViewHolder(View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                text2 = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}
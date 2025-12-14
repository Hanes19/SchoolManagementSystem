package com.example.studentmanagement;

import android.database.Cursor;
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
import java.util.ArrayList;
import java.util.List;

public class StudentScheduleActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;

    // UI References for Day Selectors
    private CardView cardMon, cardTue, cardWed, cardThu, cardFri;
    private TextView txtDayMon, txtDateMon, txtDayTue, txtDateTue;
    private TextView txtDayWed, txtDateWed, txtDayThu, txtDateThu, txtDayFri, txtDateFri;
    private TextView txtScheduleLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_schedule);

        dbHelper = new DatabaseHelper(this);

        // Header Back Button
        ImageView backBtn = findViewById(R.id.btn_back_schedule);
        backBtn.setOnClickListener(v -> finish());

        // Header Label
        txtScheduleLabel = findViewById(R.id.txt_schedule_label);

        // RecyclerView Setup
        recyclerView = findViewById(R.id.recycler_schedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Day Views
        initDayViews();

        // Set Click Listeners
        cardMon.setOnClickListener(v -> selectDay("Mon"));
        cardTue.setOnClickListener(v -> selectDay("Tue"));
        cardWed.setOnClickListener(v -> selectDay("Wed"));
        cardThu.setOnClickListener(v -> selectDay("Thu"));
        cardFri.setOnClickListener(v -> selectDay("Fri"));

        // Load Default Day (Monday)
        selectDay("Mon");
    }

    private void initDayViews() {
        cardMon = findViewById(R.id.card_mon);
        txtDayMon = findViewById(R.id.txt_day_mon);
        txtDateMon = findViewById(R.id.txt_date_mon);

        cardTue = findViewById(R.id.card_tue);
        txtDayTue = findViewById(R.id.txt_day_tue);
        txtDateTue = findViewById(R.id.txt_date_tue);

        cardWed = findViewById(R.id.card_wed);
        txtDayWed = findViewById(R.id.txt_day_wed);
        txtDateWed = findViewById(R.id.txt_date_wed);

        cardThu = findViewById(R.id.card_thu);
        txtDayThu = findViewById(R.id.txt_day_thu);
        txtDateThu = findViewById(R.id.txt_date_thu);

        cardFri = findViewById(R.id.card_fri);
        txtDayFri = findViewById(R.id.txt_day_fri);
        txtDateFri = findViewById(R.id.txt_date_fri);
    }

    private void selectDay(String day) {
        // 1. Reset all tabs to inactive style
        resetTabStyles();

        // 2. Set Active Style and Load Data
        switch (day) {
            case "Mon":
                setActiveStyle(cardMon, txtDayMon, txtDateMon);
                loadSchedule("Mon");
                txtScheduleLabel.setText("Monday's Classes");
                break;
            case "Tue":
                setActiveStyle(cardTue, txtDayTue, txtDateTue);
                loadSchedule("Tue");
                txtScheduleLabel.setText("Tuesday's Classes");
                break;
            case "Wed":
                setActiveStyle(cardWed, txtDayWed, txtDateWed);
                loadSchedule("Wed");
                txtScheduleLabel.setText("Wednesday's Classes");
                break;
            case "Thu":
                setActiveStyle(cardThu, txtDayThu, txtDateThu);
                loadSchedule("Thu");
                txtScheduleLabel.setText("Thursday's Classes");
                break;
            case "Fri":
                setActiveStyle(cardFri, txtDayFri, txtDateFri);
                loadSchedule("Fri");
                txtScheduleLabel.setText("Friday's Classes");
                break;
        }
    }

    private void setActiveStyle(CardView card, TextView txtDay, TextView txtDate) {
        card.setCardBackgroundColor(Color.parseColor("#1B254B")); // Active Blue
        card.setCardElevation(4);
        txtDay.setTextColor(Color.WHITE);
        txtDate.setTextColor(Color.WHITE);
    }

    private void resetTabStyles() {
        resetOneTab(cardMon, txtDayMon, txtDateMon);
        resetOneTab(cardTue, txtDayTue, txtDateTue);
        resetOneTab(cardWed, txtDayWed, txtDateWed);
        resetOneTab(cardThu, txtDayThu, txtDateThu);
        resetOneTab(cardFri, txtDayFri, txtDateFri);
    }

    private void resetOneTab(CardView card, TextView txtDay, TextView txtDate) {
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(0);
        txtDay.setTextColor(Color.parseColor("#A3AED0")); // Inactive Gray
        txtDate.setTextColor(Color.parseColor("#1B254B")); // Text Dark Blue
    }

    private void loadSchedule(String day) {
        List<ScheduleItem> items = new ArrayList<>();

        // ---------------------------------------------------------
        // SAMPLE DATA (Hardcoded for testing UI)
        // ---------------------------------------------------------
        switch (day) {
            case "Mon":
                items.add(new ScheduleItem("Mathematics", "08:00 AM", "Room 101", "Mr. Smith"));
                items.add(new ScheduleItem("Physics", "10:00 AM", "Room 202", "Ms. Johnson"));
                items.add(new ScheduleItem("History", "01:00 PM", "Room 105", "Mr. Black"));
                break;
            case "Tue":
                items.add(new ScheduleItem("English", "09:00 AM", "Room 301", "Mr. Brown"));
                items.add(new ScheduleItem("Chemistry", "11:00 AM", "Room 205", "Mrs. White"));
                break;
            case "Wed":
                items.add(new ScheduleItem("Biology", "08:30 AM", "Room 208", "Dr. Green"));
                items.add(new ScheduleItem("Computer Sci", "10:30 AM", "Lab 1", "Ms. Tech"));
                break;
            case "Thu":
                items.add(new ScheduleItem("Geography", "09:30 AM", "Room 102", "Ms. World"));
                items.add(new ScheduleItem("Art", "02:00 PM", "Studio B", "Mr. Paint"));
                break;
            case "Fri":
                items.add(new ScheduleItem("P.E.", "08:00 AM", "Gym", "Coach Fit"));
                items.add(new ScheduleItem("Music", "11:00 AM", "Room 404", "Mrs. Melody"));
                break;
        }

        // ---------------------------------------------------------
        // DATABASE CODE (Uncomment when ready)
        // ---------------------------------------------------------
        /*
        Cursor cursor = dbHelper.getScheduleForDay(day);
        if (cursor != null && cursor.moveToFirst()) {
            items.clear(); // Remove sample data if database has entries
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String room = cursor.getString(cursor.getColumnIndexOrThrow("room"));
                String teacher = cursor.getString(cursor.getColumnIndexOrThrow("teacher_name"));
                items.add(new ScheduleItem(subject, startTime, room, teacher));
            } while (cursor.moveToNext());
            cursor.close();
        }
        */

        adapter = new ScheduleAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    // --- Model Class ---
    private static class ScheduleItem {
        String subject, time, room, teacher;
        ScheduleItem(String s, String t, String r, String tea) {
            this.subject = s; this.time = t; this.room = r; this.teacher = tea;
        }
    }

    // --- Adapter Class ---
    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
        List<ScheduleItem> list;

        ScheduleAdapter(List<ScheduleItem> list) {
            this.list = list;
        }

        @NonNull @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate your specific XML layout: item_schedule_class_row
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_schedule_class_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ScheduleItem item = list.get(position);

            // Bind Subject
            holder.tvSubject.setText(item.subject);

            // Bind Location and Teacher
            holder.tvLocation.setText("Room: " + item.room + " • " + item.teacher);

            // Bind Time (Splitting "08:00 AM" into "08:00" and "AM")
            // This assumes your data format is always "HH:MM AM/PM"
            if (item.time.contains(" ")) {
                String[] timeParts = item.time.split(" ");
                holder.tvTime.setText(timeParts[0]);
                if (timeParts.length > 1) {
                    holder.tvMeridiem.setText(timeParts[1]);
                }
            } else {
                holder.tvTime.setText(item.time);
                holder.tvMeridiem.setText("");
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            // Define the Views from item_schedule_class_row.xml
            TextView tvTime, tvMeridiem, tvSubject, tvLocation;
            View statusIndicator;

            ViewHolder(View itemView) {
                super(itemView);
                // Find IDs that match your XML exactly
                tvTime = itemView.findViewById(R.id.tv_schedule_time);
                tvMeridiem = itemView.findViewById(R.id.tv_schedule_meridiem);
                tvSubject = itemView.findViewById(R.id.tv_schedule_subject);
                tvLocation = itemView.findViewById(R.id.tv_schedule_location);
                statusIndicator = itemView.findViewById(R.id.view_status_indicator);
            }
        }
    }
}
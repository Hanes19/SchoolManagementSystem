package com.example.studentmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentScheduleActivity extends AppCompatActivity {

    private LinearLayout scheduleContainer;

    // Day Cards
    private CardView cardMon, cardTue, cardWed, cardThu, cardFri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_schedule);

        setupHeader();
        findContainer();
        setupDaySelectors();

        // Default to Monday
        loadScheduleForDay("Monday");
        updateDaySelection(cardMon);
    }

    private void setupHeader() {
        LinearLayout header = findViewById(R.id.header_schedule);
        if (header != null) {
            View backBtn = header.findViewById(R.id.btn_back_schedule);
            if (backBtn != null) backBtn.setOnClickListener(v -> finish());
        }
    }

    private void findContainer() {
        // Find the specific layout that holds the list items inside NestedScrollView
        androidx.core.widget.NestedScrollView scrollView = findViewById(R.id.recycler_schedule).getParent() instanceof LinearLayout ?
                (androidx.core.widget.NestedScrollView) ((LinearLayout)findViewById(R.id.recycler_schedule).getParent()).getParent() : null;

        if(scrollView != null && scrollView.getChildCount() > 0) {
            scheduleContainer = (LinearLayout) scrollView.getChildAt(0);
        } else {
            ViewGroup root = findViewById(android.R.id.content);
            findContainerRecursive(root);
        }
    }

    private void findContainerRecursive(View view) {
        if (view instanceof androidx.core.widget.NestedScrollView) {
            ViewGroup sv = (ViewGroup) view;
            if (sv.getChildCount() > 0 && sv.getChildAt(0) instanceof LinearLayout) {
                scheduleContainer = (LinearLayout) sv.getChildAt(0);
            }
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                findContainerRecursive(group.getChildAt(i));
                if (scheduleContainer != null) return;
            }
        }
    }

    private void setupDaySelectors() {
        cardMon = findViewById(R.id.card_mon);
        cardTue = findViewById(R.id.card_tue);
        cardWed = findViewById(R.id.card_wed);
        cardThu = findViewById(R.id.card_thu);
        cardFri = findViewById(R.id.card_fri);

        View.OnClickListener listener = v -> {
            String day = "Monday";
            if (v == cardMon) day = "Monday";
            else if (v == cardTue) day = "Tuesday";
            else if (v == cardWed) day = "Wednesday";
            else if (v == cardThu) day = "Thursday";
            else if (v == cardFri) day = "Friday";

            loadScheduleForDay(day);
            updateDaySelection((CardView) v);
        };

        if(cardMon != null) cardMon.setOnClickListener(listener);
        if(cardTue != null) cardTue.setOnClickListener(listener);
        if(cardWed != null) cardWed.setOnClickListener(listener);
        if(cardThu != null) cardThu.setOnClickListener(listener);
        if(cardFri != null) cardFri.setOnClickListener(listener);
    }

    private void updateDaySelection(CardView selected) {
        resetCard(cardMon); resetCard(cardTue); resetCard(cardWed); resetCard(cardThu); resetCard(cardFri);
        if(selected != null) selected.setCardBackgroundColor(Color.parseColor("#1B254B"));
    }

    private void resetCard(CardView card) {
        if(card != null) card.setCardBackgroundColor(Color.WHITE);
    }

    private void loadScheduleForDay(String day) {
        if (scheduleContainer == null) return;

        // Clear previous items (keep the Label "Classes" which is usually index 0)
        int childCount = scheduleContainer.getChildCount();
        if (childCount > 1) {
            scheduleContainer.removeViews(1, childCount - 1);
        }

        // --- SAMPLE DATA ---
        switch (day) {
            case "Monday":
                addScheduleItem("08:00 - 09:30", "Mathematics", "Mr. Smith", "Room 101", "#4CAF50");
                addScheduleItem("09:45 - 11:15", "Physics", "Ms. Frizzle", "Lab 2", "#2196F3");
                addScheduleItem("13:00 - 14:30", "History", "Mr. Binns", "Room 204", "#FF9800");
                break;
            case "Tuesday":
                addScheduleItem("08:00 - 09:30", "English Lit", "Mr. Keating", "Room 105", "#673AB7");
                addScheduleItem("09:45 - 11:15", "Chemistry", "Mr. White", "Lab 1", "#E91E63");
                addScheduleItem("13:00 - 14:30", "Physical Ed", "Coach Carter", "Gym", "#FF5722");
                break;
            case "Wednesday":
                addScheduleItem("08:00 - 09:30", "Mathematics", "Mr. Smith", "Room 101", "#4CAF50");
                addScheduleItem("09:45 - 11:15", "Biology", "Ms. Frizzle", "Lab 3", "#8BC34A");
                break;
            case "Thursday":
                addScheduleItem("08:00 - 09:30", "Physics", "Ms. Frizzle", "Lab 2", "#2196F3");
                addScheduleItem("09:45 - 11:15", "English Lit", "Mr. Keating", "Room 105", "#673AB7");
                addScheduleItem("13:00 - 14:30", "Computer Sci", "Mr. Robot", "Lab 4", "#607D8B");
                break;
            case "Friday":
                addScheduleItem("09:00 - 11:00", "Art & Design", "Ms. Potts", "Art Studio", "#9C27B0");
                addScheduleItem("13:00 - 14:00", "Assembly", "Principal Skinner", "Hall", "#795548");
                break;
        }
    }

    private void addScheduleItem(String time, String subject, String teacher, String room, String colorHex) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);
        card.setRadius(20);
        card.setCardElevation(2);
        card.setCardBackgroundColor(Color.WHITE);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);

        View strip = new View(this);
        strip.setLayoutParams(new LinearLayout.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT));
        strip.setBackgroundColor(Color.parseColor(colorHex));
        inner.addView(strip);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(32, 24, 32, 24);

        TextView tvTime = new TextView(this);
        tvTime.setText(time);
        tvTime.setTextColor(Color.parseColor(colorHex));
        tvTime.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvSubject = new TextView(this);
        tvSubject.setText(subject);
        tvSubject.setTextSize(18);
        tvSubject.setTextColor(Color.parseColor("#1B254B"));
        tvSubject.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvDetails = new TextView(this);
        tvDetails.setText(teacher + " • " + room);
        tvDetails.setTextColor(Color.GRAY);
        tvDetails.setTextSize(14);

        content.addView(tvTime);
        content.addView(tvSubject);
        content.addView(tvDetails);

        inner.addView(content);
        card.addView(inner);
        scheduleContainer.addView(card);
    }
}
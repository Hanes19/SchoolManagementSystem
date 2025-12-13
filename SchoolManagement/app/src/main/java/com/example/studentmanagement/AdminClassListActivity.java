package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminClassListActivity extends AppCompatActivity {

    DatabaseHelper db;
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_class_list);

        db = new DatabaseHelper(this);
        // This will now work because we added the ID to the XML
        gridLayout = findViewById(R.id.grid_class_list);

        // Header Back Button
        findViewById(R.id.header).setOnClickListener(v -> finish());

        // Master Timetable Link (ID added to XML)
        findViewById(R.id.card_master_timetable).setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminMasterTimetableActivity.class);
            startActivity(intent);
        });

        // Add Class FAB (ID added to XML)
        findViewById(R.id.fab_add_class).setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminAddClassActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClasses(); // Reload list when returning from "Add Class"
    }

    private void loadClasses() {
        gridLayout.removeAllViews(); // Clear existing views

        Cursor cursor = db.getAllClasses();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String grade = cursor.getString(1);
                String section = cursor.getString(2);
                String teacherName = cursor.getString(4);
                if(teacherName == null) teacherName = "Unassigned";

                addClassCard(id, grade + "-" + section, "0 Students", teacherName);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void addClassCard(String classId, String className, String studentCount, String teacherName) {
        CardView card = new CardView(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(16, 16, 16, 16);
        card.setLayoutParams(params);
        card.setRadius(60f);
        card.setCardElevation(0);
        card.setCardBackgroundColor(Color.WHITE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        // Class Name
        TextView tvName = new TextView(this);
        tvName.setText(className);
        tvName.setTextSize(18); // FIXED: Used setter method
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        tvName.setTextColor(Color.parseColor("#1B254B"));
        layout.addView(tvName);

        // Student Count
        TextView tvCount = new TextView(this);
        tvCount.setText(studentCount);
        tvCount.setTextSize(12); // FIXED: Used setter method
        tvCount.setTextColor(Color.parseColor("#A3AED0"));
        tvCount.setPadding(0, 0, 0, 30);
        layout.addView(tvCount);

        // Teacher Row
        LinearLayout teacherRow = new LinearLayout(this);
        teacherRow.setOrientation(LinearLayout.HORIZONTAL);
        teacherRow.setGravity(Gravity.CENTER_VERTICAL);

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.profile);
        icon.setColorFilter(Color.parseColor("#A3AED0"));
        icon.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
        teacherRow.addView(icon);

        TextView tvTeacher = new TextView(this);
        tvTeacher.setText(teacherName);
        tvTeacher.setTextSize(12); // FIXED: Used setter method
        tvTeacher.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTeacher.setTextColor(Color.parseColor("#1B254B"));
        tvTeacher.setPadding(15, 0, 0, 0);
        teacherRow.addView(tvTeacher);

        layout.addView(teacherRow);
        card.addView(layout);

        // Click Listener to open Timetable
        card.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminClassTimetableActivity.class);
            intent.putExtra("CLASS_ID", classId);
            intent.putExtra("CLASS_NAME", className);
            startActivity(intent);
        });

        gridLayout.addView(card);
    }
}
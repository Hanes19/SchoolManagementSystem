package com.example.studentmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminTeacherListActivity extends AppCompatActivity {

    private LinearLayout listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Uses the Staff Directory layout structure which is identical
        setContentView(R.layout.admin_user_directory_staff);

        listContainer = findViewById(R.id.listContainer);

        setupTabs();
        loadSampleTeachers();

        // Add Button
        if (findViewById(R.id.btn_add_staff) != null) {
            findViewById(R.id.btn_add_staff).setOnClickListener(v ->
                    startActivity(new Intent(this, AddTeacherActivity.class)));
        }
    }

    private void loadSampleTeachers() {
        if(listContainer == null) return;
        listContainer.removeAllViews(); // Clear any static XML placeholders

        // These IDs (TCH001, TCH002) match the Profile Activity logic above
        addTeacherCard("TCH001", "Mr. Walter White", "Chemistry", "Active");
        addTeacherCard("TCH002", "Ms. Valerie Frizzle", "Biology", "Active");
        addTeacherCard("TCH003", "Prof. Severus Snape", "Potions", "On Leave");
        addTeacherCard("TCH004", "Mr. Miyagi", "Physical Ed", "Active");
    }

    private void addTeacherCard(String id, String name, String subject, String status) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 30);
        card.setLayoutParams(params);
        card.setRadius(30);
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(0); // Flat look matching design

        // CRITICAL: This sends the ID to the Profile Activity
        card.setOnClickListener(v -> {
            Intent intent = new Intent(AdminTeacherListActivity.this, AdminTeacherProfileActivity.class);
            intent.putExtra("TEACHER_ID", id);
            startActivity(intent);
        });

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(40, 40, 40, 40);
        inner.setGravity(Gravity.CENTER_VERTICAL);

        // Profile Pic
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.profile_pic);
        img.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
        inner.addView(img);

        // Name & Subject
        LinearLayout textInfo = new LinearLayout(this);
        textInfo.setOrientation(LinearLayout.VERTICAL);
        textInfo.setPadding(40, 0, 0, 0);
        textInfo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextSize(18);
        tvName.setTextColor(Color.parseColor("#1B254B"));
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvSub = new TextView(this);
        tvSub.setText(subject);
        tvSub.setTextSize(14);
        tvSub.setTextColor(Color.parseColor("#A3AED0"));

        textInfo.addView(tvName);
        textInfo.addView(tvSub);
        inner.addView(textInfo);

        // Status Badge
        TextView tvStatus = new TextView(this);
        tvStatus.setText(status);
        tvStatus.setTextSize(12);
        tvStatus.setPadding(20, 10, 20, 10);
        if(status.equals("Active")) {
            tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9"));
        } else {
            tvStatus.setTextColor(Color.parseColor("#FF9800"));
            tvStatus.setBackgroundColor(Color.parseColor("#FFF3E0"));
        }
        inner.addView(tvStatus);

        card.addView(inner);
        listContainer.addView(card);
    }

    private void setupTabs() {
        // Fix Header Title manually
        LinearLayout header = findViewById(R.id.header);
        if (header != null) {
            for (int i = 0; i < header.getChildCount(); i++) {
                if (header.getChildAt(i) instanceof TextView) {
                    ((TextView) header.getChildAt(i)).setText("Teacher Directory");
                    break;
                }
            }
        }

        // Tab Navigation
        LinearLayout tabContainer = findViewById(R.id.tab_container);
        if(tabContainer != null) {
            TextView tabStudent = (TextView) tabContainer.getChildAt(0);
            TextView tabTeacher = (TextView) tabContainer.getChildAt(1);
            TextView tabStaff = (TextView) tabContainer.getChildAt(2);

            // Highlight Teacher
            tabTeacher.setTextColor(Color.WHITE);
            tabTeacher.setBackgroundResource(R.drawable.rounded_blue_bg_placeholder);

            // Un-highlight others
            tabStudent.setTextColor(Color.parseColor("#A3AED0"));
            tabStudent.setBackground(null);
            tabStaff.setTextColor(Color.parseColor("#A3AED0"));
            tabStaff.setBackground(null);

            tabStudent.setOnClickListener(v -> {
                startActivity(new Intent(this, AdminStudentListActivity.class));
                overridePendingTransition(0,0);
                finish();
            });
            tabStaff.setOnClickListener(v -> {
                startActivity(new Intent(this, AdminStaffListActivity.class));
                overridePendingTransition(0,0);
                finish();
            });
        }

        ImageView btnBack = findViewById(R.id.btn_back);
        if(btnBack != null) btnBack.setOnClickListener(v -> finish());
    }
}
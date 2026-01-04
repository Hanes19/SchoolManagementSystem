package com.example.studentmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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
        setContentView(R.layout.admin_user_directory_staff);

        listContainer = findViewById(R.id.listContainer);

        setupTabsAndHeader();
        loadSampleTeachers();

        findViewById(R.id.btn_add_staff).setOnClickListener(v ->
                startActivity(new Intent(this, AddTeacherActivity.class)));
    }

    private void setupTabsAndHeader() {
        // Change Header Title
        LinearLayout header = findViewById(R.id.header);
        if (header != null) {
            for (int i = 0; i < header.getChildCount(); i++) {
                View child = header.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setText("Teacher Directory");
                    break;
                }
            }
        }

        LinearLayout tabContainer = findViewById(R.id.tab_container);
        TextView tabStudent = (TextView) tabContainer.getChildAt(0);
        TextView tabTeacher = (TextView) tabContainer.getChildAt(1);
        TextView tabStaff = (TextView) tabContainer.getChildAt(2);

        // Highlight Teacher
        tabTeacher.setTextColor(Color.WHITE);
        tabTeacher.setBackgroundResource(R.drawable.rounded_blue_bg_placeholder);

        // Reset others
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
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void loadSampleTeachers() {
        if(listContainer == null) return;
        listContainer.removeAllViews();

        addTeacherCard("TCH001", "Mr. Walter White", "Chemistry", "Active");
        addTeacherCard("TCH002", "Ms. Frizzle", "Biology", "Active");
        addTeacherCard("TCH003", "Prof. Snape", "Potions", "On Leave");
    }

    private void addTeacherCard(String id, String name, String subject, String status) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 30);
        card.setLayoutParams(params);
        card.setRadius(30);
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(0);

        card.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminTeacherProfileActivity.class);
            intent.putExtra("TEACHER_ID", id);
            startActivity(intent);
        });

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(40, 40, 40, 40);
        inner.setGravity(Gravity.CENTER_VERTICAL);

        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.profile_pic);
        img.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
        inner.addView(img);

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
}
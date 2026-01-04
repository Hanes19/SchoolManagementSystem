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
        // We reuse the layout because it has the same structure
        setContentView(R.layout.admin_user_directory_staff);

        listContainer = findViewById(R.id.listContainer);

        setupTabsAndHeader();
        loadSampleTeachers();

        findViewById(R.id.btn_add_staff).setOnClickListener(v ->
                startActivity(new Intent(this, AddTeacherActivity.class)));
    }

    private void setupTabsAndHeader() {
        // Change Header Title
        TextView headerTitle = findViewById(R.id.header).findViewById(android.R.id.text1); // Actually we need to find by text if ID missing, but let's assume standard logic
        // Easier: Just find the TextView in header manually if ID is unknown, or ignore if XML uses "User Directory" generically.

        LinearLayout tabContainer = findViewById(R.id.tab_container);
        TextView tabStudent = (TextView) tabContainer.getChildAt(0);
        TextView tabTeacher = (TextView) tabContainer.getChildAt(1);
        TextView tabStaff = (TextView) tabContainer.getChildAt(2);

        // Highlight Teacher Tab
        tabTeacher.setTextColor(Color.WHITE);
        tabTeacher.setBackgroundResource(R.drawable.rounded_blue_bg_placeholder); // Assuming drawable exists

        // Un-highlight Staff
        tabStaff.setTextColor(Color.parseColor("#A3AED0"));
        tabStaff.setBackground(null);

        // Click Listeners
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
        addTeacherCard("TCH003", "Mr. Miyagi", "Physical Ed.", "On Leave");
        addTeacherCard("TCH004", "Prof. Dumbledore", "Headmaster", "Active");
    }

    private void addTeacherCard(String id, String name, String subject, String status) {
        // ... (Same Card Logic as Staff, just creating view programmatically)
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

        inner.addView(createStatusBadge(status)); // Helper method for badge
        card.addView(inner);
        listContainer.addView(card);
    }

    private TextView createStatusBadge(String status) {
        TextView tv = new TextView(this);
        tv.setText(status);
        tv.setTextSize(12);
        tv.setPadding(20, 10, 20, 10);
        if(status.equals("Active")) {
            tv.setTextColor(Color.parseColor("#4CAF50"));
            tv.setBackgroundColor(Color.parseColor("#E8F5E9"));
        } else {
            tv.setTextColor(Color.parseColor("#FF9800"));
            tv.setBackgroundColor(Color.parseColor("#FFF3E0"));
        }
        return tv;
    }
}
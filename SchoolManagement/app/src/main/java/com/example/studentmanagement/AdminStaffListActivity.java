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

public class AdminStaffListActivity extends AppCompatActivity {

    private LinearLayout listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_directory_staff);

        listContainer = findViewById(R.id.listContainer);
        setupTabs();
        loadSampleStaff();

        findViewById(R.id.btn_add_staff).setOnClickListener(v ->
                startActivity(new Intent(this, AddStaffActivity.class)));
    }

    private void loadSampleStaff() {
        if(listContainer == null) return;
        listContainer.removeAllViews();

        addStaffCard("STF001", "Argus Filch", "Caretaker", "Active");
        addStaffCard("STF002", "Madam Pomfrey", "Nurse", "Active");
        addStaffCard("STF003", "Rubeus Hagrid", "Groundskeeper", "Active");
    }

    private void addStaffCard(String id, String name, String role, String status) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 30);
        card.setLayoutParams(params);
        card.setRadius(30);
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(0);

        card.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminStaffProfileActivity.class);
            intent.putExtra("STAFF_ID", id);
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

        TextView tvRole = new TextView(this);
        tvRole.setText(role);
        tvRole.setTextSize(14);
        tvRole.setTextColor(Color.parseColor("#A3AED0"));

        textInfo.addView(tvName);
        textInfo.addView(tvRole);
        inner.addView(textInfo);

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
        LinearLayout header = findViewById(R.id.header);
        if (header != null) {
            for (int i = 0; i < header.getChildCount(); i++) {
                View child = header.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setText("Staff Directory");
                    break;
                }
            }
        }

        LinearLayout tabContainer = findViewById(R.id.tab_container);

        tabContainer.getChildAt(0).setOnClickListener(v -> { // Student
            startActivity(new Intent(this, AdminStudentListActivity.class));
            overridePendingTransition(0,0);
            finish();
        });
        tabContainer.getChildAt(1).setOnClickListener(v -> { // Teacher
            startActivity(new Intent(this, AdminTeacherListActivity.class));
            overridePendingTransition(0,0);
            finish();
        });
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}
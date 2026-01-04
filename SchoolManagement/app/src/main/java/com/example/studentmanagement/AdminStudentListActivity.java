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

public class AdminStudentListActivity extends AppCompatActivity {

    private LinearLayout listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_directory_staff);

        listContainer = findViewById(R.id.listContainer);

        setupTabsAndHeader();
        loadSampleStudents();

        findViewById(R.id.btn_add_staff).setOnClickListener(v ->
                startActivity(new Intent(this, AddStudentActivity.class)));
    }

    private void setupTabsAndHeader() {
        LinearLayout tabContainer = findViewById(R.id.tab_container);
        TextView tabStudent = (TextView) tabContainer.getChildAt(0);
        TextView tabTeacher = (TextView) tabContainer.getChildAt(1);
        TextView tabStaff = (TextView) tabContainer.getChildAt(2);

        // Highlight Student Tab
        tabStudent.setTextColor(Color.WHITE);
        tabStudent.setBackgroundResource(R.drawable.rounded_blue_bg_placeholder);

        // Un-highlight Staff
        tabStaff.setTextColor(Color.parseColor("#A3AED0"));
        tabStaff.setBackground(null);

        // Navigation
        tabTeacher.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminTeacherListActivity.class));
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

    private void loadSampleStudents() {
        if(listContainer == null) return;
        listContainer.removeAllViews();

        addStudentCard("stud01", "Jason Statham", "Grade 10 - Emerald");
        addStudentCard("stud02", "Harry Potter", "Grade 11 - Ruby");
        addStudentCard("stud03", "Hermione Granger", "Grade 11 - Ruby");
        addStudentCard("stud04", "Peter Parker", "Grade 12 - Diamond");
    }

    private void addStudentCard(String id, String name, String grade) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 30);
        card.setLayoutParams(params);
        card.setRadius(30);
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(0);

        card.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminStudentProfileActivity.class);
            intent.putExtra("STUDENT_ID", id);
            startActivity(intent);
        });

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(40, 40, 40, 40);
        inner.setGravity(Gravity.CENTER_VERTICAL);

        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.profile_pic); // Make sure you have this drawable
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

        TextView tvGrade = new TextView(this);
        tvGrade.setText(grade);
        tvGrade.setTextSize(14);
        tvGrade.setTextColor(Color.parseColor("#A3AED0"));

        textInfo.addView(tvName);
        textInfo.addView(tvGrade);
        inner.addView(textInfo);

        card.addView(inner);
        listContainer.addView(card);
    }
}
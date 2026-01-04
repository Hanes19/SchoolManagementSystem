package com.example.studentmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TeacherGradebookActivity extends AppCompatActivity {

    private LinearLayout studentListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_gradebook);

        setupHeader();
        findContainer();
        loadStudentList();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findHeaderLayout(root);
        if (header != null) {
            if (header.getChildCount() > 0) {
                header.getChildAt(0).setOnClickListener(v -> finish());
            }
            // Set dynamic title if found
            for(int i=0; i<header.getChildCount(); i++) {
                if(header.getChildAt(i) instanceof TextView) {
                    ((TextView)header.getChildAt(i)).setText("Gradebook: Chemistry 101");
                    break;
                }
            }
        }
    }

    private void findContainer() {
        ViewGroup root = findViewById(android.R.id.content);
        ScrollView scrollView = findScrollView(root);
        if (scrollView != null && scrollView.getChildCount() > 0) {
            if (scrollView.getChildAt(0) instanceof LinearLayout) {
                studentListContainer = (LinearLayout) scrollView.getChildAt(0);
            }
        }
    }

    private void loadStudentList() {
        if (studentListContainer == null) return;

        addStudentRow("Jason Statham", "ID: 2023-001", "95");
        addStudentRow("Harry Potter", "ID: 2023-002", "88");
        addStudentRow("Hermione Granger", "ID: 2023-003", "100");
        addStudentRow("Ron Weasley", "ID: 2023-004", "75");
        addStudentRow("Draco Malfoy", "ID: 2023-005", "82");
    }

    private void addStudentRow(String name, String id, String currentGrade) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);
        card.setRadius(20);
        card.setCardElevation(2);
        card.setCardBackgroundColor(Color.WHITE);

        card.setOnClickListener(v ->
                Toast.makeText(this, "Editing grade for " + name, Toast.LENGTH_SHORT).show()
        );

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(32, 32, 32, 32);
        inner.setGravity(Gravity.CENTER_VERTICAL);

        // Avatar
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.profile_pic);
        img.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        inner.addView(img);

        // Name & ID
        LinearLayout textInfo = new LinearLayout(this);
        textInfo.setOrientation(LinearLayout.VERTICAL);
        textInfo.setPadding(32, 0, 0, 0);
        textInfo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextSize(18);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        tvName.setTextColor(Color.parseColor("#1B254B"));

        TextView tvId = new TextView(this);
        tvId.setText(id);
        tvId.setTextColor(Color.GRAY);

        textInfo.addView(tvName);
        textInfo.addView(tvId);
        inner.addView(textInfo);

        // Grade Box
        TextView tvGrade = new TextView(this);
        tvGrade.setText(currentGrade);
        tvGrade.setTextSize(18);
        tvGrade.setTypeface(null, android.graphics.Typeface.BOLD);
        tvGrade.setTextColor(Color.parseColor("#4361EE")); // Blue text
        tvGrade.setBackgroundColor(Color.parseColor("#F4F7FE")); // Light blue bg
        tvGrade.setPadding(24, 12, 24, 12);

        inner.addView(tvGrade);
        card.addView(inner);
        studentListContainer.addView(card);
    }

    // --- Helpers ---
    private LinearLayout findHeaderLayout(View view) {
        if (view instanceof LinearLayout) return (LinearLayout) view;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                LinearLayout res = findHeaderLayout(group.getChildAt(i));
                if (res != null) return res;
            }
        }
        return null;
    }

    private ScrollView findScrollView(View view) {
        if (view instanceof ScrollView) return (ScrollView) view;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                ScrollView res = findScrollView(group.getChildAt(i));
                if (res != null) return res;
            }
        }
        return null;
    }
}
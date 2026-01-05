package com.example.studentmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TeacherAttendanceActivity extends AppCompatActivity {

    private LinearLayout studentListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_attendance_sheet);

        setupHeader();
        findContainer();
        loadStudentList();
        setupSubmitButton();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findHeaderLayout(root);
        if (header != null) {
            if (header.getChildCount() > 0) {
                header.getChildAt(0).setOnClickListener(v -> finish());
            }
            // Set dynamic title
            for(int i=0; i<header.getChildCount(); i++) {
                if(header.getChildAt(i) instanceof TextView) {
                    ((TextView)header.getChildAt(i)).setText("Attendance: Class 10-A");
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

        // Note: We are simulating checkboxes programmatically
        addStudentRow("Jason Statham", "ID: 2023-001");
        addStudentRow("Harry Potter", "ID: 2023-002");
        addStudentRow("Hermione Granger", "ID: 2023-003");
        addStudentRow("Ron Weasley", "ID: 2023-004");
        addStudentRow("Draco Malfoy", "ID: 2023-005");
    }

    private void addStudentRow(String name, String id) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        card.setLayoutParams(params);
        card.setRadius(16);
        card.setCardElevation(2);
        card.setCardBackgroundColor(Color.WHITE);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(32, 24, 32, 24);
        inner.setGravity(Gravity.CENTER_VERTICAL);

        // Avatar
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.profile_pic);
        img.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        inner.addView(img);

        // Info
        LinearLayout textInfo = new LinearLayout(this);
        textInfo.setOrientation(LinearLayout.VERTICAL);
        textInfo.setPadding(32, 0, 0, 0);
        textInfo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextSize(16);
        tvName.setTextColor(Color.parseColor("#1B254B"));
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvId = new TextView(this);
        tvId.setText(id);
        tvId.setTextColor(Color.GRAY);
        tvId.setTextSize(12);

        textInfo.addView(tvName);
        textInfo.addView(tvId);
        inner.addView(textInfo);

        // Checkbox (Present/Absent)
        CheckBox cb = new CheckBox(this);
        cb.setChecked(true); // Default Present
        cb.setText("Present");
        inner.addView(cb);

        card.addView(inner);
        studentListContainer.addView(card);
    }

    private void setupSubmitButton() {
        // Find the "Submit" or "Save" button usually at the bottom
        ViewGroup root = findViewById(android.R.id.content);
        View btn = findButtonRecursive(root);
        if(btn != null) {
            btn.setOnClickListener(v -> {
                Toast.makeText(this, "Attendance Saved Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
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

    private View findButtonRecursive(View view) {
        if (view instanceof CardView) {
            // Check if it's not a row item (rough check: not inside scrollview)
            if (!(view.getParent() instanceof LinearLayout && view.getParent().getParent() instanceof ScrollView)) {
                return view;
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View res = findButtonRecursive(group.getChildAt(i));
                if (res != null) return res;
            }
        }
        return null;
    }
}
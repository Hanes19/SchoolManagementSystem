package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        setupHeader();
        setupCards();
        setupProfileButton(); // Renamed from setupLogout
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null) {
            for (int i = 0; i < header.getChildCount(); i++) {
                View child = header.getChildAt(i);
                if (child instanceof LinearLayout) {
                    LinearLayout textContainer = (LinearLayout) child;
                    if (textContainer.getChildCount() > 1 && textContainer.getChildAt(1) instanceof TextView) {
                        ((TextView) textContainer.getChildAt(1)).setText("Jason Statham");
                    }
                    if (textContainer.getChildCount() > 2 && textContainer.getChildAt(2) instanceof TextView) {
                        ((TextView) textContainer.getChildAt(2)).setText("Grade 12 - Diamond");
                    }
                    break;
                }
            }
        }
    }

    private void setupCards() {
        ViewGroup root = findViewById(android.R.id.content);
        GridLayout grid = findGridLayout(root);

        if (grid != null) {
            setCardClickListener(grid, 0, StudentAttendanceActivity.class);
            setCardClickListener(grid, 1, StudentFeesActivity.class);
            setCardClickListener(grid, 2, StudentScheduleActivity.class);
            setCardClickListener(grid, 3, StudentGradesActivity.class);
        }
    }

    private void setupProfileButton() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null && header.getChildCount() > 0) {
            // Target the last child (Profile Icon)
            View lastChild = header.getChildAt(header.getChildCount() - 1);
            // navigate to Profile Activity instead of finish()
            lastChild.setOnClickListener(v -> startActivity(new Intent(this, StudentProfileActivity.class)));
        }
    }

    private void setCardClickListener(GridLayout grid, int index, Class<?> destination) {
        if (grid.getChildCount() > index) {
            View child = grid.getChildAt(index);
            child.setOnClickListener(v -> startActivity(new Intent(this, destination)));
        }
    }

    private GridLayout findGridLayout(View view) {
        if (view instanceof GridLayout) {
            return (GridLayout) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                GridLayout result = findGridLayout(group.getChildAt(i));
                if (result != null) return result;
            }
        }
        return null;
    }

    private LinearLayout findFirstLinearLayout(View view) {
        if (view instanceof LinearLayout) {
            return (LinearLayout) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof LinearLayout) return (LinearLayout) child;
                LinearLayout result = findFirstLinearLayout(child);
                if (result != null) return result;
            }
        }
        return null;
    }
}
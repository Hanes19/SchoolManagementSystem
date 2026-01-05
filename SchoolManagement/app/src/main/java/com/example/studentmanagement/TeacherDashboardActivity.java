package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_dashboard);

        setupHeader();
        setupCards();
        setupProfileButton();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null) {
            if (header.getChildCount() > 1 && header.getChildAt(1) instanceof LinearLayout) {
                LinearLayout textContainer = (LinearLayout) header.getChildAt(1);
                if (textContainer.getChildCount() > 0 && textContainer.getChildAt(0) instanceof TextView) {
                    ((TextView) textContainer.getChildAt(0)).setText("Mr. Walter White");
                }
                if (textContainer.getChildCount() > 1 && textContainer.getChildAt(1) instanceof TextView) {
                    ((TextView) textContainer.getChildAt(1)).setText("Next: Chemistry 101 (Room 302)");
                }
            }
        }
    }

    private void setupCards() {
        ViewGroup root = findViewById(android.R.id.content);
        GridLayout grid = findGridLayout(root);

        if (grid != null) {
            setCardClickListener(grid, 0, TeacherAttendanceActivity.class);
            setCardClickListener(grid, 1, TeacherGradebookActivity.class);
            setCardClickListener(grid, 2, TeacherScheduleActivity.class);
            setCardClickListener(grid, 3, TeacherHomeworkActivity.class);
        }
    }

    private void setupProfileButton() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null && header.getChildCount() > 0) {
            View lastChild = header.getChildAt(header.getChildCount() - 1);
            // Navigate to Profile Activity
            lastChild.setOnClickListener(v -> startActivity(new Intent(this, TeacherProfileActivity.class)));
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
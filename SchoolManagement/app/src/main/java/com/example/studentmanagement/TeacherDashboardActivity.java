package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TeacherDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_dashboard);

        // 1. Initialize Header (Name & Next Class Info) dynamically
        setupHeader();

        // 2. Initialize Navigation Cards (Gradebook, Homework, etc.) dynamically
        setupCards();

        // 3. Setup Logout
        setupLogout();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null) {
            // Check for the inner text container (usually the 2nd child, after the profile image)
            if (header.getChildCount() > 1 && header.getChildAt(1) instanceof LinearLayout) {
                LinearLayout textContainer = (LinearLayout) header.getChildAt(1);

                // Set Teacher Name (1st TextView)
                if (textContainer.getChildCount() > 0 && textContainer.getChildAt(0) instanceof TextView) {
                    ((TextView) textContainer.getChildAt(0)).setText("Mr. Walter White");
                }

                // Set Next Class Info (2nd TextView) - fixes 'tv_next_class_info' error
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
            // Assign actions based on card position (Index)

            // Card 0: Attendance
            setCardClickListener(grid, 0, TeacherAttendanceActivity.class);

            // Card 1: Gradebook (fixes 'card_gradebook' error)
            setCardClickListener(grid, 1, TeacherGradebookActivity.class);

            // Card 2: Schedule
            setCardClickListener(grid, 2, TeacherScheduleActivity.class);

            // Card 3: Homework (fixes 'card_homework' error)
            setCardClickListener(grid, 3, TeacherHomeworkActivity.class);
        }
    }

    private void setupLogout() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null && header.getChildCount() > 0) {
            // Logout is usually the last child in the header row
            View lastChild = header.getChildAt(header.getChildCount() - 1);
            lastChild.setOnClickListener(v -> finish());
        }
    }

    // --- Helper Methods to Traverse Layout ---

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
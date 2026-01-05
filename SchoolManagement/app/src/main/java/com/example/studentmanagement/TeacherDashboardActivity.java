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

    // 1. Declare SessionManager
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_dashboard);

        // 2. Initialize SessionManager
        session = new SessionManager(getApplicationContext());

        // Check if user is logged in (Optional but good practice)
        if (!session.isLoggedIn()) {
            session.logoutUser();
        }

        setupHeader();
        setupCards();
        setupLogout();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null) {
            // Note: In your XML, the text container is actually at index 0, not 1.
            // Child 0 = LinearLayout (Text), Child 1 = Notification, Child 2 = Profile
            if (header.getChildCount() > 0 && header.getChildAt(0) instanceof LinearLayout) {
                LinearLayout textContainer = (LinearLayout) header.getChildAt(0);

                if (textContainer.getChildCount() > 1) {
                    // Update Teacher Name
                    if (textContainer.getChildAt(1) instanceof TextView) {
                        ((TextView) textContainer.getChildAt(1)).setText("Mr. Walter White");
                    }
                    // Update Next Class Info
                    if (textContainer.getChildCount() > 2 && textContainer.getChildAt(2) instanceof TextView) {
                        ((TextView) textContainer.getChildAt(2)).setText("Next: Chemistry 101 (Room 302)");
                    }
                }
            }
        }
    }

    private void setupCards() {
        ViewGroup root = findViewById(android.R.id.content);
        GridLayout grid = findGridLayout(root);

        if (grid != null) {
            // Card 0: Attendance
            setCardClickListener(grid, 0, TeacherAttendanceActivity.class);
            // Card 1: Gradebook
            setCardClickListener(grid, 1, TeacherGradebookActivity.class);
            // Card 2: Schedule
            setCardClickListener(grid, 2, TeacherScheduleActivity.class);
            // Card 3: Homework
            setCardClickListener(grid, 3, TeacherHomeworkActivity.class);
        }
    }

    private void setupLogout() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null && header.getChildCount() > 0) {
            // 3. Target the Profile Picture (Last child in the header layout)
            View profileView = header.getChildAt(header.getChildCount() - 1);

            profileView.setOnClickListener(v -> {
                // 4. Call logout from SessionManager
                // This clears preferences and redirects to LoginActivity
                session.logoutUser();
            });
        }
    }

    // --- Helper Methods ---

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
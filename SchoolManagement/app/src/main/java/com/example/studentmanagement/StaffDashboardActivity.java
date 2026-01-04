package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StaffDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_dashboard);

        // 1. Initialize Header (Name & Department) using layout traversal
        setupHeader();

        // 2. Initialize Navigation Cards using layout traversal
        setupCards();

        // 3. Setup Logout
        setupLogout();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null) {
            // Traverse header children to find the text container
            for (int i = 0; i < header.getChildCount(); i++) {
                View child = header.getChildAt(i);
                if (child instanceof LinearLayout) {
                    LinearLayout textContainer = (LinearLayout) child;
                    // Assuming 1st text is Name, 2nd is Department
                    if (textContainer.getChildCount() > 0 && textContainer.getChildAt(0) instanceof TextView) {
                        ((TextView) textContainer.getChildAt(0)).setText("Argus Filch");
                    }
                    if (textContainer.getChildCount() > 1 && textContainer.getChildAt(1) instanceof TextView) {
                        ((TextView) textContainer.getChildAt(1)).setText("Maintenance");
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
            // Card 1: Fees Collection (Index 0)
            setCardClickListener(grid, 0, StaffFeesCollectionActivity.class);

            // Card 2: Leave Application (Index 1)
            setCardClickListener(grid, 1, StaffLeaveActivity.class);

            // Card 3: Payslips (Index 2)
            setCardClickListener(grid, 2, StaffPayslipActivity.class);
        }
    }

    private void setupLogout() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null && header.getChildCount() > 0) {
            // Logout is usually the last icon in the header
            View lastChild = header.getChildAt(header.getChildCount() - 1);
            lastChild.setOnClickListener(v -> finish());
        }
    }

    // --- Helper Methods to Find Views without IDs ---

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
                // Recursively find the header Linear Layout
                if (child instanceof LinearLayout) return (LinearLayout) child;

                LinearLayout result = findFirstLinearLayout(child);
                if (result != null) return result;
            }
        }
        return null;
    }
}
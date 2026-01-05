package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LibraryDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_dashboard);

        setupHeader();
        setupCards();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null) {
            // Update the name dynamically if you wish, defaulting to Madam Pince as requested
            if (header.getChildCount() > 1 && header.getChildAt(1) instanceof LinearLayout) {
                LinearLayout textContainer = (LinearLayout) header.getChildAt(1);
                if (textContainer.getChildCount() > 0 && textContainer.getChildAt(0) instanceof TextView) {
                    ((TextView) textContainer.getChildAt(0)).setText("Madam Pince");
                }
                if (textContainer.getChildCount() > 1 && textContainer.getChildAt(1) instanceof TextView) {
                    ((TextView) textContainer.getChildAt(1)).setText("Head Librarian");
                }
            }
        }
    }

    private void setupCards() {
        ViewGroup root = findViewById(android.R.id.content);
        GridLayout grid = findGridLayout(root);

        if (grid != null) {
            // 0: Catalog
            setCardClickListener(grid, 0, LibraryBookCatalogActivity.class);
            // 1: Circulation
            setCardClickListener(grid, 1, LibraryIssueReturnActivity.class);
            // 2: Late Fees
            setCardClickListener(grid, 2, LibraryLateFeesActivity.class);
            // 3: E-Resources
            setCardClickListener(grid, 3, LibraryEResourcesActivity.class);
            // 4: Settings (New)
            setCardClickListener(grid, 4, LibrarySettingsActivity.class);
        }
    }

    private void setCardClickListener(GridLayout grid, int index, Class<?> destination) {
        if (grid.getChildCount() > index) {
            View child = grid.getChildAt(index);
            child.setOnClickListener(v -> startActivity(new Intent(this, destination)));
        }
    }

    private GridLayout findGridLayout(View view) {
        if (view instanceof GridLayout) return (GridLayout) view;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                GridLayout result = findGridLayout(group.getChildAt(i));
                if (result != null) return result;
            }
        }
        return null;
    }

    private LinearLayout findFirstLinearLayout(View view) {
        if (view instanceof LinearLayout) return (LinearLayout) view;
        if (view instanceof ViewGroup) {
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
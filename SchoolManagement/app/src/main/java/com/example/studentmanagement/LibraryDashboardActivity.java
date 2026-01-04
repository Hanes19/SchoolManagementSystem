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

public class LibraryDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_dashboard);

        // 1. Initialize Header (Name & Role) using layout traversal
        setupHeader();

        // 2. Initialize Navigation Cards using layout traversal
        setupCards();

        // 3. Setup Logout Button
        // Assuming logout is the ImageButton/ImageView in the header (usually the last child)
        setupLogout();
    }

    private void setupHeader() {
        // Traverse to find the Header layout (usually the first LinearLayout container)
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content).getRootView();
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null) {
            // Usually: 0=Img, 1=TextContainer, 2=Logout
            if (header.getChildCount() > 1 && header.getChildAt(1) instanceof LinearLayout) {
                LinearLayout textContainer = (LinearLayout) header.getChildAt(1);

                if (textContainer.getChildCount() > 0 && textContainer.getChildAt(0) instanceof TextView) {
                    ((TextView) textContainer.getChildAt(0)).setText("Madam Pince"); // Name
                }
                if (textContainer.getChildCount() > 1 && textContainer.getChildAt(1) instanceof TextView) {
                    ((TextView) textContainer.getChildAt(1)).setText("Head Librarian"); // Role
                }
            }
        }
    }

    private void setupCards() {
        // Find the main GridLayout containing the menu cards
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content).getRootView();
        GridLayout grid = findGridLayout(root);

        if (grid != null) {
            // Card 1: Catalog
            setCardClickListener(grid, 0, LibraryBookCatalogActivity.class);
            // Card 2: Issue/Return
            setCardClickListener(grid, 1, LibraryIssueReturnActivity.class);
            // Card 3: Active Issues
            setCardClickListener(grid, 2, LibraryActiveIssuesActivity.class);
            // Card 4: Overdue Items
            setCardClickListener(grid, 3, LibraryOverdueItemsActivity.class);

            // Card 5: Add Book (if it exists in the grid)
            setCardClickListener(grid, 4, LibraryAddBookActivity.class);
            // Card 6: Add Resource (if it exists in the grid)
            setCardClickListener(grid, 5, LibraryAddEResourceActivity.class);
        }
    }

    private void setupLogout() {
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content).getRootView();
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null && header.getChildCount() > 0) {
            // Usually the logout button is the last child in the header row
            View lastChild = header.getChildAt(header.getChildCount() - 1);
            lastChild.setOnClickListener(v -> {
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                finish();
            });
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
        // Helper to find the Header container
        if (view instanceof LinearLayout) {
            return (LinearLayout) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                // We skip the root ConstraintLayout/ScrollView to find the inner containers
                View child = group.getChildAt(i);
                if (child instanceof LinearLayout) return (LinearLayout) child;

                // Recursively search deeper
                LinearLayout result = findFirstLinearLayout(child);
                if (result != null) return result;
            }
        }
        return null;
    }
}
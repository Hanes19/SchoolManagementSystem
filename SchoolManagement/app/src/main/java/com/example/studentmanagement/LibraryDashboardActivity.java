package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LibraryDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_dashboard);

        setupHeader();
        setupButtons();
    }

    private void setupButtons() {
        // 1. Book Catalog
        setListener(R.id.btn_catalog, LibraryBookCatalogActivity.class);

        // 2. Issue / Return (Circulation)
        setListener(R.id.btn_circulation, LibraryIssueReturnActivity.class);

        // 3. Late Fees
        setListener(R.id.btn_fines, LibraryLateFeesActivity.class);

        // 4. E-Resources
        setListener(R.id.btn_digital, LibraryEResourcesActivity.class);

        // 5. Settings
        setListener(R.id.btn_profile_settings, LibrarySettingsActivity.class);
    }

    private void setListener(int buttonId, Class<?> destination) {
        View view = findViewById(buttonId);
        if (view != null) {
            view.setOnClickListener(v -> startActivity(new Intent(this, destination)));
        }
    }

    private void setupHeader() {
        // Fix for updating the header text (Madam Pince)
        LinearLayout header = findViewById(R.id.header);
        if (header != null && header.getChildCount() > 0) {
            // The text container is the FIRST child (Index 0) in your XML
            View firstChild = header.getChildAt(0);

            if (firstChild instanceof LinearLayout) {
                LinearLayout textContainer = (LinearLayout) firstChild;

                // Update Name
                if (textContainer.getChildCount() > 0 && textContainer.getChildAt(0) instanceof TextView) {
                    ((TextView) textContainer.getChildAt(0)).setText("Madam Pince");
                }
                // Update Role
                if (textContainer.getChildCount() > 1 && textContainer.getChildAt(1) instanceof TextView) {
                    ((TextView) textContainer.getChildAt(1)).setText("Head Librarian");
                }
            }
        }
    }
}
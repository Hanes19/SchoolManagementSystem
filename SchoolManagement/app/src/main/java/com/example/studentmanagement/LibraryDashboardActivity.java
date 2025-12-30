package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.Map;

public class LibraryDashboardActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvActiveIssues, tvOverdueItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_dashboard);

        db = new DatabaseHelper(this);

        // Initialize UI Elements
        tvActiveIssues = findViewById(R.id.tv_active_issues_count); // *Note: Add ID to XML
        tvOverdueItems = findViewById(R.id.tv_overdue_items_count); // *Note: Add ID to XML

        // 1. Catalog Button
        CardView btnCatalog = findViewById(R.id.btn_catalog);
        btnCatalog.setOnClickListener(v -> {
            // Intent intent = new Intent(this, LibraryCatalogActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Book Catalog Coming Soon", Toast.LENGTH_SHORT).show();
        });

        // 2. Circulation Button
        CardView btnCirculation = findViewById(R.id.btn_circulation);
        btnCirculation.setOnClickListener(v -> {
            // Intent intent = new Intent(this, LibraryIssueReturnActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Circulation Module Coming Soon", Toast.LENGTH_SHORT).show();
        });

        // 3. FAB (Add Book)
        CardView fabAction = findViewById(R.id.fab_library_action);
        fabAction.setOnClickListener(v -> {
            Intent intent = new Intent(LibraryDashboardActivity.this, LibraryAddBookActivity.class);
            startActivity(intent);
        });

        // Note: You can hook up btn_fines and btn_digital similarly
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardStats();
    }

    private void loadDashboardStats() {
        Map<String, Integer> stats = db.getLibraryStats();

        // These TextViews need IDs in your library_dashboard.xml
        // Update your XML: <TextView android:id="@+id/tv_active_issues_count" ... text="142" />
        if(tvActiveIssues != null) tvActiveIssues.setText(String.valueOf(stats.getOrDefault("active_issues", 0)));

        // Update your XML: <TextView android:id="@+id/tv_overdue_items_count" ... text="18" />
        if(tvOverdueItems != null) tvOverdueItems.setText(String.valueOf(stats.getOrDefault("overdue_items", 0)));
    }
}
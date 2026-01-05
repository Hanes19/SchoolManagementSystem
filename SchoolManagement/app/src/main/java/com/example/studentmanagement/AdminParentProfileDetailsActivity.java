package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminParentProfileDetailsActivity extends AppCompatActivity {

    private String parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_parents_profile_details);

        // 1. Safe ID Retrieval (Prevents NullPointerException)
        if (getIntent().hasExtra("PARENT_ID")) {
            parentId = getIntent().getStringExtra("PARENT_ID");
        } else {
            parentId = "DEFAULT"; // Fallback so it doesn't crash
        }

        // 2. Setup Header (Back Button)
        setupHeader();

        // 3. Initialize Views & Load Data safely
        loadProfileData();
    }

    private void setupHeader() {
        // Find back button by ID or traversal
        View btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        } else {
            // Fallback: assume first clickable image in top layout is back
            ViewGroup root = findViewById(android.R.id.content);
            findBackBtnRecursive(root);
        }
    }

    private void loadProfileData() {
        // Find Views by ID first, if null, code continues safely
        TextView tvName = findViewById(R.id.tv_parent_name);
        TextView tvEmail = findViewById(R.id.tv_parent_email);
        TextView tvPhone = findViewById(R.id.tv_parent_phone);
        LinearLayout llChildren = findViewById(R.id.ll_children_container);

        // Define Data
        String name = "Parent Name";
        String email = "parent@email.com";
        String phone = "+1 (555) 000-0000";

        if ("PAR001".equals(parentId)) {
            name = "Mrs. Sarah Smith";
            email = "sarah.smith@email.com";
            phone = "+1 555-0199";
            addChildSafe(llChildren, "Jason Smith (Grade 10)");
            addChildSafe(llChildren, "Emily Smith (Grade 8)");
        } else if ("PAR002".equals(parentId)) {
            name = "Mr. Thomas Wayne";
            email = "thomas.wayne@gotham.com";
            phone = "+1 555-BAT1";
            addChildSafe(llChildren, "Bruce Wayne (Grade 11)");
        } else {
            // Default Profile
            name = "Default Parent";
            addChildSafe(llChildren, "No linked students");
        }

        // Set Text Safely (Check if View exists)
        if (tvName != null) tvName.setText(name);
        if (tvEmail != null) tvEmail.setText(email);
        if (tvPhone != null) tvPhone.setText(phone);
    }

    private void addChildSafe(LinearLayout container, String text) {
        if (container == null) return; // Prevent crash if container missing
        TextView child = new TextView(this);
        child.setText("• " + text);
        child.setTextSize(16);
        child.setPadding(0, 8, 0, 8);
        child.setTextColor(android.graphics.Color.parseColor("#1B254B"));
        container.addView(child);
    }

    // Recursive helper to find back button if ID is missing
    private void findBackBtnRecursive(View view) {
        if (view.isClickable() && view.getClass().getName().contains("Image")) {
            int[] loc = new int[2];
            view.getLocationOnScreen(loc);
            if (loc[1] < 300 && loc[0] < 150) { // Top Left corner check
                view.setOnClickListener(v -> finish());
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                findBackBtnRecursive(group.getChildAt(i));
            }
        }
    }
}
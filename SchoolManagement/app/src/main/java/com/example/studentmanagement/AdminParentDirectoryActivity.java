package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminParentDirectoryActivity extends AppCompatActivity {

    private CardView cardParent1;
    private CardView cardParent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_parent_directory);

        // 1. Initialize Views
        ImageView btnBack = findViewById(R.id.btn_back);
        EditText etSearch = findViewById(R.id.et_search_input);
        CardView fabAddParent = findViewById(R.id.fab_add_parent);

        cardParent1 = findViewById(R.id.card_parent_1);
        cardParent2 = findViewById(R.id.card_parent_2);

        // 2. Setup Interactions

        // Back Button
        btnBack.setOnClickListener(v -> finish());

        // FAB: Add New Parent
        fabAddParent.setOnClickListener(v -> {
            // Opens the "Edit/Add" screen directly, but in "Add Mode" (logic to be handled inside that activity)
            Intent intent = new Intent(AdminParentDirectoryActivity.this, AdminEditParentProfileActivity.class);
            startActivity(intent);
        });

        // --- NEW: Click Card 1 -> Open Profile Details ---
        cardParent1.setOnClickListener(v -> {
            Intent intent = new Intent(AdminParentDirectoryActivity.this, AdminParentProfileDetailsActivity.class);
            intent.putExtra("PARENT_NAME", "Mrs. Sarah Smith"); // Pass data if needed
            startActivity(intent);
        });

        // --- NEW: Click Card 2 -> Open Profile Details ---
        cardParent2.setOnClickListener(v -> {
            Intent intent = new Intent(AdminParentDirectoryActivity.this, AdminParentProfileDetailsActivity.class);
            intent.putExtra("PARENT_NAME", "Mr. Thomas Wayne");
            startActivity(intent);
        });

        // Search Logic (Existing)
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterParentList(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterParentList(String query) {
        String lowerQuery = query.toLowerCase();
        if (cardParent1 != null) {
            boolean match = "sarah smith".contains(lowerQuery) || "jason smith".contains(lowerQuery);
            cardParent1.setVisibility(match ? View.VISIBLE : View.GONE);
        }
        if (cardParent2 != null) {
            boolean match = "thomas wayne".contains(lowerQuery) || "bruce wayne".contains(lowerQuery);
            cardParent2.setVisibility(match ? View.VISIBLE : View.GONE);
        }
    }
}
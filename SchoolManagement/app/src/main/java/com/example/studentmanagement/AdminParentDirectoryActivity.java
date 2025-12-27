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

    // Variables for the specific parent cards we want to filter
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

        // Initialize the static parent cards (Make sure you added IDs in XML!)
        cardParent1 = findViewById(R.id.card_parent_1);
        cardParent2 = findViewById(R.id.card_parent_2);

        // 2. Back Button Function
        btnBack.setOnClickListener(v -> finish());

        // 3. Floating Action Button - Go to Add/Edit Profile
        fabAddParent.setOnClickListener(v -> {
            Intent intent = new Intent(AdminParentDirectoryActivity.this, AdminEditParentProfileActivity.class);
            startActivity(intent);
        });

        // 4. Search Filter Logic
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

    // Function to hide/show cards based on the name typed
    private void filterParentList(String query) {
        String lowerQuery = query.toLowerCase();

        // Filter Parent 1: Sarah Smith / Jason Smith
        if (cardParent1 != null) {
            boolean match = "sarah smith".contains(lowerQuery) || "jason smith".contains(lowerQuery);
            if (match) {
                cardParent1.setVisibility(View.VISIBLE);
            } else {
                cardParent1.setVisibility(View.GONE);
            }
        }

        // Filter Parent 2: Thomas Wayne / Bruce Wayne
        if (cardParent2 != null) {
            boolean match = "thomas wayne".contains(lowerQuery) || "bruce wayne".contains(lowerQuery);
            if (match) {
                cardParent2.setVisibility(View.VISIBLE);
            } else {
                cardParent2.setVisibility(View.GONE);
            }
        }
    }
}
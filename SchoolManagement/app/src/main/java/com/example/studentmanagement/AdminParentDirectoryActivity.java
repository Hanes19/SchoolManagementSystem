package com.example.studentmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import java.util.List;

public class AdminParentDirectoryActivity extends AppCompatActivity {

    private LinearLayout listContainer;
    private EditText etSearch;
    private List<ParentModel> allParents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_parent_directory);

        setupViews();
        setupSampleData();
        populateList("");
    }

    private void setupViews() {
        // 1. Find Search Input
        etSearch = findViewById(R.id.et_search_input);
        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) { populateList(s.toString()); }
                @Override public void afterTextChanged(Editable s) {}
            });
        }

        // 2. Find List Container (Safe Search)
        listContainer = findViewById(R.id.parent_list_container);
        if (listContainer == null) {
            // Find the ScrollView's child if ID is missing
            ViewGroup root = findViewById(android.R.id.content);
            findContainerRecursive(root);
        }

        // 3. Find Back Button
        View btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // 4. Find Add Button
        View fab = findViewById(R.id.fab_add_parent);
        if (fab != null) fab.setOnClickListener(v -> startActivity(new Intent(this, AdminAddParentActivity.class)));
    }

    private void findContainerRecursive(View view) {
        if (view instanceof ScrollView) {
            ScrollView sv = (ScrollView) view;
            if (sv.getChildCount() > 0 && sv.getChildAt(0) instanceof LinearLayout) {
                listContainer = (LinearLayout) sv.getChildAt(0);
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                findContainerRecursive(group.getChildAt(i));
            }
        }
    }

    private void setupSampleData() {
        allParents.clear();
        allParents.add(new ParentModel("PAR001", "Mrs. Sarah Smith", "sarah.smith@email.com", "Jason Smith"));
        allParents.add(new ParentModel("PAR002", "Mr. Thomas Wayne", "thomas.wayne@email.com", "Bruce Wayne"));
        allParents.add(new ParentModel("PAR003", "Mrs. Molly Weasley", "molly@burrow.com", "Ron Weasley"));
    }

    private void populateList(String query) {
        if (listContainer == null) return;
        listContainer.removeAllViews();

        for (ParentModel parent : allParents) {
            if (parent.name.toLowerCase().contains(query.toLowerCase())) {
                listContainer.addView(createParentCard(parent));
            }
        }
    }

    private View createParentCard(ParentModel parent) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 30);
        card.setLayoutParams(params);
        card.setRadius(30);
        card.setCardElevation(0);
        card.setCardBackgroundColor(Color.WHITE);

        // Click Listener -> Sends ID to Profile
        card.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminParentProfileDetailsActivity.class);
            intent.putExtra("PARENT_ID", parent.id);
            startActivity(intent);
        });

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(40, 40, 40, 40);
        inner.setGravity(Gravity.CENTER_VERTICAL);

        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.profile);
        img.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        inner.addView(img);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(40, 0, 0, 0);

        TextView tvName = new TextView(this);
        tvName.setText(parent.name);
        tvName.setTextSize(18);
        tvName.setTextColor(Color.parseColor("#1B254B"));
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvChild = new TextView(this);
        tvChild.setText("Child: " + parent.childInfo);
        tvChild.setTextColor(Color.GRAY);

        textLayout.addView(tvName);
        textLayout.addView(tvChild);
        inner.addView(textLayout);

        card.addView(inner);
        return card;
    }

    static class ParentModel {
        String id, name, email, childInfo;
        ParentModel(String id, String name, String email, String childInfo) {
            this.id = id; this.name = name; this.email = email; this.childInfo = childInfo;
        }
    }
}
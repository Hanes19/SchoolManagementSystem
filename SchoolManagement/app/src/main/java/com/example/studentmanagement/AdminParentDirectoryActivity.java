package com.example.studentmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        // 1. Initialize Views
        ImageView btnBack = findViewById(R.id.btn_back);
        etSearch = findViewById(R.id.et_search_input);
        CardView fabAddParent = findViewById(R.id.fab_add_parent);

        // IMPORTANT: Ensure your XML has a LinearLayout with id 'parent_list_container' inside the ScrollView
        // If not, rename the one inside the ScrollView to this ID.
        listContainer = findViewById(R.id.parent_list_container);
        if (listContainer == null) {
            // Fallback if ID is missing in XML, try finding the first child of ScrollView's child
            // Ideally, just add android:id="@+id/parent_list_container" to the XML LinearLayout
        }

        // 2. Setup Dummy Data
        setupSampleData();

        // 3. Load List
        populateList("");

        // 4. Listeners
        btnBack.setOnClickListener(v -> finish());
        fabAddParent.setOnClickListener(v -> startActivity(new Intent(this, AdminAddParentActivity.class)));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                populateList(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupSampleData() {
        allParents.add(new ParentModel("PAR001", "Mrs. Sarah Smith", "sarah.smith@email.com", "Jason Smith (10-A)"));
        allParents.add(new ParentModel("PAR002", "Mr. Thomas Wayne", "thomas.wayne@email.com", "Bruce Wayne (11-B)"));
        allParents.add(new ParentModel("PAR003", "Mrs. Molly Weasley", "molly@burrow.com", "Ron Weasley (10-A)"));
        allParents.add(new ParentModel("PAR004", "Dr. Stephen Strange", "doc@strange.com", "Donna Strange (12-C)"));
    }

    private void populateList(String query) {
        if (listContainer == null) return;
        listContainer.removeAllViews();

        for (ParentModel parent : allParents) {
            if (parent.name.toLowerCase().contains(query.toLowerCase()) ||
                    parent.childInfo.toLowerCase().contains(query.toLowerCase())) {
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
        card.setRadius(40);
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(0);

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
        img.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
        inner.addView(img);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(40, 0, 0, 0);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvName = new TextView(this);
        tvName.setText(parent.name);
        tvName.setTextSize(18);
        tvName.setTextColor(Color.parseColor("#1B254B"));
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvChild = new TextView(this);
        tvChild.setText("Child: " + parent.childInfo);
        tvChild.setTextSize(14);
        tvChild.setTextColor(Color.parseColor("#A3AED0"));

        textLayout.addView(tvName);
        textLayout.addView(tvChild);
        inner.addView(textLayout);

        // Call Icon
        ImageView callIcon = new ImageView(this);
        callIcon.setImageResource(android.R.drawable.ic_menu_call);
        callIcon.setColorFilter(Color.parseColor("#4CAF50")); // Green
        callIcon.setPadding(20,20,20,20);
        inner.addView(callIcon);

        card.addView(inner);
        return card;
    }

    // Helper Class
    static class ParentModel {
        String id, name, email, childInfo;
        ParentModel(String id, String name, String email, String childInfo) {
            this.id = id; this.name = name; this.email = email; this.childInfo = childInfo;
        }
    }
}
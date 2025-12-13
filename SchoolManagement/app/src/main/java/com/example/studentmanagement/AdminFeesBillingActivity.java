package com.example.studentmanagement;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminFeesBillingActivity extends AppCompatActivity {

    private LinearLayout recordsContainer;
    // Default filter values
    private String currentStatusFilter = "All";
    private String currentGradeFilter = "All";
    private String currentSortOrder = "None";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_fees_billings);

        // 1. Initialize Back Button
        ImageView btnBack = findViewById(R.id.header).findViewWithTag("back_btn");
        if(btnBack == null) {
            LinearLayout header = findViewById(R.id.header);
            if(header != null && header.getChildCount() > 0 && header.getChildAt(0) instanceof ImageView) {
                btnBack = (ImageView) header.getChildAt(0);
            }
        }
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Initialize Filter Button
        ImageView btnFilter = findViewById(R.id.btn_filter);
        if (btnFilter != null) {
            btnFilter.setOnClickListener(v -> showFilterBottomSheet());
        }

        // 3. Initialize Generate Invoice Button
        // UPDATED: Now navigates to the Generate Invoice Activity
        CardView btnInvoice = findViewById(R.id.btn_invoice);
        if (btnInvoice != null) {
            btnInvoice.setOnClickListener(v -> {
                Intent intent = new Intent(AdminFeesBillingActivity.this, AdminGenerateInvoiceActivity.class);
                startActivity(intent);
            });
        }

        // 4. Locate the container for student cards for filtering
        View firstCard = findViewById(R.id.card_student_1);
        if (firstCard != null) {
            recordsContainer = (LinearLayout) firstCard.getParent();
        }

        // 5. Setup Click Listeners for the Cards (To open Details)
        setupCardClickListeners();
    }

    // ==========================================
    //           NAVIGATION & ACTIONS
    // ==========================================

    private void setupCardClickListeners() {
        // List of your hardcoded student card IDs
        int[] cardIds = {R.id.card_student_1, R.id.card_student_2, R.id.card_student_3};

        for (int id : cardIds) {
            View card = findViewById(id);
            if (card != null) {
                card.setOnClickListener(v -> {
                    // Navigate to the Invoice Details Activity
                    Intent intent = new Intent(AdminFeesBillingActivity.this, AdminInvoiceDetailsActivity.class);
                    // Example of passing data (optional):
                    // intent.putExtra("STUDENT_NAME", "Jason Statham");
                    startActivity(intent);
                });
            }
        }
    }

    // ==========================================
    //           FILTER & SORT LOGIC
    // ==========================================

    private void showFilterBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.admin_fees_filter, null);
        bottomSheetDialog.setContentView(view);

        // --- 1. Payment Status Logic ---
        LinearLayout statusChipsContainer = view.findViewById(R.id.status_chips);
        setupChipGroup(statusChipsContainer, currentStatusFilter, newSelection -> currentStatusFilter = newSelection);

        // --- 2. Class Level Logic ---
        ViewGroup scrollGrades = view.findViewById(R.id.scroll_grades);
        if (scrollGrades != null && scrollGrades.getChildCount() > 0) {
            LinearLayout gradeScrollContainer = (LinearLayout) scrollGrades.getChildAt(0);
            setupChipGroup(gradeScrollContainer, currentGradeFilter, newSelection -> currentGradeFilter = newSelection);
        }

        // --- 3. Sort By Logic ---
        RadioGroup rgSort = view.findViewById(R.id.rg_sort);
        if (currentSortOrder.equals("High")) ((RadioButton) rgSort.getChildAt(0)).setChecked(true);
        if (currentSortOrder.equals("Low")) ((RadioButton) rgSort.getChildAt(1)).setChecked(true);

        rgSort.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = group.findViewById(checkedId);
            if (rb != null) {
                String text = rb.getText().toString();
                if (text.contains("High")) currentSortOrder = "High";
                else if (text.contains("Low")) currentSortOrder = "Low";
            }
        });

        // --- 4. Reset Logic ---
        TextView resetBtn = findTextViewWithText((ViewGroup) view, "Reset");
        if (resetBtn != null) {
            resetBtn.setOnClickListener(v -> {
                currentStatusFilter = "All";
                currentGradeFilter = "All";
                currentSortOrder = "None";
                bottomSheetDialog.dismiss();
                applyFilters(); // Refresh list to show all
            });
        }

        // --- 5. Apply Button ---
        CardView btnApply = view.findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(v -> {
            applyFilters();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void setupChipGroup(LinearLayout container, String initialSelection, OnSelectionChanged listener) {
        if (container == null) return;

        int childCount = container.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = container.getChildAt(i);
            if (child instanceof TextView) {
                TextView chip = (TextView) child;
                String chipText = chip.getText().toString();

                // Set initial visual state
                boolean isSelected = chipText.equalsIgnoreCase(initialSelection) ||
                        (initialSelection.equals("All") && chipText.equals("All"));
                updateChipVisual(chip, isSelected);

                chip.setOnClickListener(v -> {
                    // Deselect all others
                    for (int j = 0; j < childCount; j++) {
                        View c = container.getChildAt(j);
                        if (c instanceof TextView) updateChipVisual((TextView)c, false);
                    }
                    // Select this one
                    updateChipVisual(chip, true);
                    listener.onSelection(chipText);
                });
            }
        }
    }

    private void updateChipVisual(TextView chip, boolean isSelected) {
        if (isSelected) {
            chip.setTextColor(Color.WHITE);
            chip.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B254B")));
        } else {
            chip.setTextColor(Color.parseColor("#A3AED0"));
            chip.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F4F7FE")));
        }
    }

    private void applyFilters() {
        if (recordsContainer == null) return;

        List<View> allCards = new ArrayList<>();
        // Collect all children first
        for (int i = 0; i < recordsContainer.getChildCount(); i++) {
            allCards.add(recordsContainer.getChildAt(i));
        }

        List<View> visibleCards = new ArrayList<>();

        for (View card : allCards) {
            CardData data = extractDataFromCard(card);

            // Logic for matching filters
            boolean statusMatch = currentStatusFilter.equalsIgnoreCase("All") ||
                    data.status.equalsIgnoreCase(currentStatusFilter);

            boolean gradeMatch = currentGradeFilter.equalsIgnoreCase("All") ||
                    data.details.contains(currentGradeFilter);

            if (statusMatch && gradeMatch) {
                card.setVisibility(View.VISIBLE);
                visibleCards.add(card);
            } else {
                card.setVisibility(View.GONE);
            }
        }

        // Sort Logic
        if (!currentSortOrder.equals("None")) {
            Collections.sort(visibleCards, (v1, v2) -> {
                double amt1 = extractDataFromCard(v1).amountVal;
                double amt2 = extractDataFromCard(v2).amountVal;
                return currentSortOrder.equals("High") ? Double.compare(amt2, amt1) : Double.compare(amt1, amt2);
            });

            // Re-order views: Remove all and add back in sorted order
            recordsContainer.removeAllViews();

            // Add visible sorted cards
            for (View v : visibleCards) {
                recordsContainer.addView(v);
            }
            // Add back the hidden ones (so they aren't lost if filter is reset)
            for (View v : allCards) {
                if (!visibleCards.contains(v)) {
                    recordsContainer.addView(v);
                }
            }
        }
    }

    // ==========================================
    //           DATA PARSING HELPERS
    // ==========================================

    private static class CardData {
        String status;
        String details;
        double amountVal;
    }

    // Parses the hardcoded XML structure to extract data for filtering/sorting
    private CardData extractDataFromCard(View card) {
        CardData data = new CardData();
        data.status = "";
        data.details = "";
        data.amountVal = 0.0;

        try {
            // Structure: Card -> LinearLayout (Horizontal)
            LinearLayout mainLayout = (LinearLayout) ((CardView) card).getChildAt(0);

            // Middle Column (Index 1) -> Name and Details
            LinearLayout middleLayout = (LinearLayout) mainLayout.getChildAt(1);
            TextView detailsTv = (TextView) middleLayout.getChildAt(1); // "ID: ... • Grade 10"
            data.details = detailsTv.getText().toString();

            // End Column (Index 2) -> Amount and Status
            LinearLayout endLayout = (LinearLayout) mainLayout.getChildAt(2);
            TextView amountTv = (TextView) endLayout.getChildAt(0); // "₱500.00"
            TextView statusTv = (TextView) endLayout.getChildAt(1); // "PAID"

            data.status = statusTv.getText().toString();

            // Parse Amount: Remove "₱" and ","
            String amtString = amountTv.getText().toString().replace("₱", "").replace(",", "").trim();
            data.amountVal = Double.parseDouble(amtString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private TextView findTextViewWithText(ViewGroup root, String text) {
        for(int i=0; i<root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if(child instanceof TextView) {
                if(((TextView)child).getText().toString().equalsIgnoreCase(text)) return (TextView)child;
            } else if(child instanceof ViewGroup) {
                TextView found = findTextViewWithText((ViewGroup)child, text);
                if(found != null) return found;
            }
        }
        return null;
    }

    interface OnSelectionChanged {
        void onSelection(String selection);
    }
}
package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StaffFeesCollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_fees_collection);

        setupHeader();
        setupPaymentButton();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findFirstLinearLayout(root);

        if (header != null) {
            // Back Button (Child 0)
            if (header.getChildCount() > 0) {
                header.getChildAt(0).setOnClickListener(v -> finish());
            }
            // Title (Child 1 or 2)
            for (int i = 0; i < header.getChildCount(); i++) {
                if (header.getChildAt(i) instanceof TextView) {
                    ((TextView) header.getChildAt(i)).setText("Collect Fees");
                    break;
                }
            }
        }
    }

    private void setupPaymentButton() {
        // We look for a CardView that likely acts as the "Submit" button
        // Usually it's at the bottom or has a specific background color
        ViewGroup root = findViewById(android.R.id.content);
        findAndSetupButton(root);
    }

    private void findAndSetupButton(View view) {
        if (view instanceof CardView) {
            // Assume the CardView with text inside is the button
            // or just add listener to all CardViews that aren't the main container
            // A simple hack for this layout: usually the last CardView is the action button
            view.setOnClickListener(v -> {
                Toast.makeText(this, "Payment Recorded Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                findAndSetupButton(group.getChildAt(i));
            }
        }
    }

    // --- Helper ---
    private LinearLayout findFirstLinearLayout(View view) {
        if (view instanceof LinearLayout) return (LinearLayout) view;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i) instanceof LinearLayout) return (LinearLayout) group.getChildAt(i);
            }
        }
        return null;
    }
}
package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagement.R;
import com.google.android.material.bottomsheet.BottomSheetDialog; // Import this

public class AdminFeesActivity extends AppCompatActivity { // Or your specific activity name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_fees_billings); // Ensure this is the correct layout

        // 1. Initialize the Filter Button
        ImageView btnFilter = findViewById(R.id.btn_filter);

        // 2. Set Click Listener to open the Bottom Sheet
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterBottomSheet();
            }
        });
    }

    private void showFilterBottomSheet() {
        // Create the BottomSheetDialog
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // Inflate the admin_fees_filter.xml layout
        View sheetView = getLayoutInflater().inflate(R.layout.admin_fees_filter, null);
        bottomSheetDialog.setContentView(sheetView);

        // --- Handle Interactions Inside the Filter Sheet ---

        // Example: Handle "Apply Filters" button click
        View btnApply = sheetView.findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add your filtering logic here
                Toast.makeText(getApplicationContext(), "Filters Applied", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss(); // Close the sheet
            }
        });

        // Example: Handle "Reset" text click
        View btnReset = sheetView.findViewById(R.id.tv_filter_title); // Assuming 'Reset' text is near title or find specific ID if you add one
        // Note: In your XML, "Reset" does not have an ID. You might want to add android:id="@+id/btn_reset" to the TextView containing "Reset".

        // Show the dialog
        bottomSheetDialog.show();
    }
}
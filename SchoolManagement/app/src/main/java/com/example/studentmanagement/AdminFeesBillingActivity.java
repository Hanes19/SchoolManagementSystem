package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AdminFeesBillingActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout recordsContainer;
    private String currentStatusFilter = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_fees_billings);

        db = new DatabaseHelper(this);

        // 1. Initialize Views
        ImageView btnBack = null;
        View header = findViewById(R.id.header);

        // Try to find back button by tag, otherwise get the first child of header
        if (header != null) {
            btnBack = header.findViewWithTag("back_btn");
            if (btnBack == null && header instanceof ViewGroup) {
                ViewGroup headerGroup = (ViewGroup) header;
                if (headerGroup.getChildCount() > 0 && headerGroup.getChildAt(0) instanceof ImageView) {
                    btnBack = (ImageView) headerGroup.getChildAt(0);
                }
            }
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        findViewById(R.id.btn_filter).setOnClickListener(v -> showFilterBottomSheet());

        findViewById(R.id.btn_invoice).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminGenerateInvoiceActivity.class));
        });

        // --- FIX IS HERE: Changed R.id.records_container to R.id.ll_transaction_list ---
        recordsContainer = findViewById(R.id.ll_transaction_list);

        // Load Data
        loadStudentFees();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudentFees(); // Refresh when coming back
    }

    private void loadStudentFees() {
        if (recordsContainer == null) return;
        recordsContainer.removeAllViews(); // Clear hardcoded/old views

        Cursor cursor = db.getAllStudentsFeeStatus();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                double totalDue = cursor.getDouble(2);
                double totalPaid = cursor.getDouble(3);
                double balance = totalDue - totalPaid;

                String status;
                if (balance <= 0 && totalDue > 0) status = "Paid";
                else if (totalPaid > 0) status = "Pending";
                else status = "Unpaid";

                // Filter Logic
                if (!currentStatusFilter.equals("All") && !status.equalsIgnoreCase(currentStatusFilter)) {
                    continue;
                }

                // Add Card
                addStudentFeeCard(id, name, totalDue, balance, status);

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            TextView empty = new TextView(this);
            empty.setText("No student records found.");
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(20, 50, 20, 20);
            recordsContainer.addView(empty);
        }
    }

    private void addStudentFeeCard(String studentId, String name, double total, double balance, String status) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 30);
        card.setLayoutParams(params);
        card.setCardBackgroundColor(Color.WHITE);
        card.setRadius(20);
        card.setCardElevation(5);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(40, 40, 40, 40);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        // Avatar (Placeholder)
        ImageView avatar = new ImageView(this);
        avatar.setImageResource(R.drawable.profile_pic);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(120, 120);
        imgParams.setMargins(0, 0, 30, 0);
        avatar.setLayoutParams(imgParams);

        // Text Info
        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTypeface(null, Typeface.BOLD);
        tvName.setTextSize(16);
        tvName.setTextColor(Color.parseColor("#1B254B"));

        TextView tvId = new TextView(this);
        tvId.setText("ID: " + studentId);
        tvId.setTextSize(12);
        tvId.setTextColor(Color.GRAY);

        TextView tvAmount = new TextView(this);
        tvAmount.setText("Balance: ₱" + String.format("%.2f", balance));
        tvAmount.setTextSize(14);
        tvAmount.setTextColor(balance > 0 ? Color.RED : Color.parseColor("#05CD99"));
        tvAmount.setTypeface(null, Typeface.BOLD);

        textLayout.addView(tvName);
        textLayout.addView(tvId);
        textLayout.addView(tvAmount);

        // Status Badge
        TextView tvStatus = new TextView(this);
        tvStatus.setText(status);
        tvStatus.setTextSize(12);
        tvStatus.setPadding(20, 10, 20, 10);
        tvStatus.setTypeface(null, Typeface.BOLD);

        if (status.equals("Paid")) {
            tvStatus.setTextColor(Color.parseColor("#05CD99"));
            tvStatus.setBackgroundColor(Color.parseColor("#E0FBF3")); // Light Green
        } else if (status.equals("Pending")) {
            tvStatus.setTextColor(Color.parseColor("#FFB547"));
            tvStatus.setBackgroundColor(Color.parseColor("#FFF5E0")); // Light Orange
        } else {
            tvStatus.setTextColor(Color.RED);
            tvStatus.setBackgroundColor(Color.parseColor("#FFEEEE")); // Light Red
        }

        layout.addView(avatar);
        layout.addView(textLayout);
        layout.addView(tvStatus);

        card.addView(layout);

        // Click Listener
        card.setOnClickListener(v -> {
            Intent intent = new Intent(AdminFeesBillingActivity.this, AdminInvoiceDetailsActivity.class);
            intent.putExtra("STUDENT_ID", studentId);
            startActivity(intent);
        });

        recordsContainer.addView(card);
    }

    private void showFilterBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.admin_fees_filter, null);
        bottomSheetDialog.setContentView(view);

        LinearLayout statusContainer = view.findViewById(R.id.status_chips);

        if(statusContainer != null) {
            for(int i=0; i<statusContainer.getChildCount(); i++) {
                View child = statusContainer.getChildAt(i);
                if(child instanceof TextView) {
                    child.setOnClickListener(v -> {
                        currentStatusFilter = ((TextView) v).getText().toString();
                        loadStudentFees();
                        bottomSheetDialog.dismiss();
                    });
                }
            }
        }

        // Reset Button
        View btnReset = view.findViewWithTag("reset_btn");
        if(btnReset != null) {
            btnReset.setOnClickListener(v -> {
                currentStatusFilter = "All";
                loadStudentFees();
                bottomSheetDialog.dismiss();
            });
        }

        bottomSheetDialog.show();
    }
}
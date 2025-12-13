package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminFeesBillingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_fees_billings);

        // Initialize Back Button
        // Ensure you added android:id="@+id/btn_back" to the back arrow ImageView in your XML
        ImageView btnBack = findViewById(R.id.header).findViewWithTag("back_btn");
        // fallback if you haven't added ID yet, let's try finding it by ID assuming you added it:
        View backButton = findViewById(R.id.btn_back);
        if (backButton == null) {
            // If ID not found, try finding the first image view in header or just skip
            // For now, I'll assume you will add the ID as recommended.
        } else {
            backButton.setOnClickListener(v -> finish());
        }

        // Initialize Generate Invoice Button
        CardView btnInvoice = findViewById(R.id.btn_invoice);
        btnInvoice.setOnClickListener(v -> {
            Toast.makeText(this, "Generate Invoice Module Coming Soon", Toast.LENGTH_SHORT).show();
            // You can link to AdminGenerateInvoiceActivity here later
            // Intent intent = new Intent(AdminFeesBillingActivity.this, AdminGenerateInvoiceActivity.class);
            // startActivity(intent);
        });
    }
}
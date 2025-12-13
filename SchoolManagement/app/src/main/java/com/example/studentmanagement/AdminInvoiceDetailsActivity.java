package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminInvoiceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_invoice_details);

        // --- Back Button ---
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // --- Download Button ---
        CardView btnDownload = findViewById(R.id.btn_download);
        if (btnDownload != null) {
            btnDownload.setOnClickListener(v -> {
                Toast.makeText(this, "Downloading Invoice PDF...", Toast.LENGTH_SHORT).show();
                // Add PDF generation or download logic here
            });
        }

        // --- Share Button ---
        CardView btnShare = findViewById(R.id.btn_share);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> {
                Toast.makeText(this, "Opening Share Dialog...", Toast.LENGTH_SHORT).show();
                // Add Share Intent logic here
                /*
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Here is the invoice for Jason Statham.");
                startActivity(Intent.createChooser(shareIntent, "Share Invoice"));
                */
            });
        }
    }
}
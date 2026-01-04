package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_notification);

        // --- 1. Header Actions ---

        // Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Mark All Read
        TextView btnMarkRead = findViewById(R.id.btn_mark_read);
        if (btnMarkRead != null) {
            btnMarkRead.setOnClickListener(v -> {
                Toast.makeText(this, "All notifications marked as read", Toast.LENGTH_SHORT).show();
            });
        }

        // --- 2. Notification Item Actions ---

        // Notification 1: "New Student Registered"
        // FIX: Changed target to AdminUserDirectoryActivity (the same as your Dashboard button)
        View cardStudent = findViewById(R.id.card_notif_student);
        if (cardStudent != null) {
            cardStudent.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminUserDirectoryActivity.class);
                // Optional: Pass an extra to ensure it opens the "Student" tab specifically
                intent.putExtra("directory_type", "Student");
                startActivity(intent);
            });
        }

        // Notification 2: "Payment Received" -> Go to Fees/Billing
        View cardFees = findViewById(R.id.card_notif_fees);
        if (cardFees != null) {
            cardFees.setOnClickListener(v -> {
                startActivity(new Intent(this, AdminFeesBillingActivity.class));
            });
        }

        // Notification 3: "System Update" -> Go to System Config
        View cardSystem = findViewById(R.id.card_notif_system);
        if (cardSystem != null) {
            cardSystem.setOnClickListener(v -> {
                startActivity(new Intent(this, AdminSystemConfigActivity.class));
            });
        }
    }
}
package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_notification);

        // --- Header Actions ---

        // Back Button
        // Ensure you add android:id="@+id/btn_back" to the ImageView in admin_notification.xml
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Mark All Read
        // Ensure you add android:id="@+id/btn_mark_read" to the TextView in admin_notification.xml
        TextView btnMarkRead = findViewById(R.id.btn_mark_read);
        if (btnMarkRead != null) {
            btnMarkRead.setOnClickListener(v -> {
                // Logic to update database/shared prefs to mark notifications as read
                Toast.makeText(this, "All notifications marked as read", Toast.LENGTH_SHORT).show();
            });
        }

        // Note: The notification list in your XML is currently hardcoded static views.
        // In a real implementation, you would use a RecyclerView here to load data dynamically.
    }
}
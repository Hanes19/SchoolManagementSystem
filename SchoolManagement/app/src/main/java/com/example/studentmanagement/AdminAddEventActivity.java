package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminAddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_event_calendar);

        // --- Header ---
        ImageView btnBack = findViewById(R.id.btn_back); // Add this ID to XML
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // --- Input Fields (Get references for data saving) ---
        // Note: You might need to add IDs to your EditTexts in XML to extract text (e.g., et_title, et_desc)
        // EditText etTitle = findViewById(R.id.et_title); 
        // EditText etDesc = findViewById(R.id.et_description);

        // --- Save Button ---
        CardView btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            // 1. Validate inputs
            // 2. Save to Database (SQLite/Firebase)
            Toast.makeText(this, "Event Saved Successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity and return to calendar
        });
    }
}
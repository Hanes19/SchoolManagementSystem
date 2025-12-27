package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminParentProfileDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_parents_profile_details);

        // 1. Initialize Views
        ImageView btnBack = findViewById(R.id.btn_back);
        ImageView btnEditProfile = findViewById(R.id.btn_edit_profile); // Top right edit pencil
        CardView btnSendMessage = findViewById(R.id.btn_contact_container); // Bottom floating button

        // 2. Back Button
        btnBack.setOnClickListener(v -> finish());

        // 3. Edit Profile Button (Header)
        // Navigates to the Edit Form we created earlier
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(AdminParentProfileDetailsActivity.this, AdminEditParentProfileActivity.class);
            // Optional: Pass the current name so the Edit screen knows who to load
            intent.putExtra("PARENT_NAME", "Mrs. Sarah Smith");
            startActivity(intent);
        });

        // 4. Send Message Button (Footer)
        btnSendMessage.setOnClickListener(v -> {
            // Logic to open chat or send SMS
            Toast.makeText(this, "Opening messaging for Sarah Smith...", Toast.LENGTH_SHORT).show();
        });

        // 5. (Optional) Dynamic Data Loading
        // If we passed data from the Directory, we can display it here.
        // Example:
        // String name = getIntent().getStringExtra("PARENT_NAME");
        // if (name != null) {
        //     TextView tvName = findViewById(R.id.tv_hero_name); // You'd need to add this ID to XML
        //     tvName.setText(name);
        // }
    }
}
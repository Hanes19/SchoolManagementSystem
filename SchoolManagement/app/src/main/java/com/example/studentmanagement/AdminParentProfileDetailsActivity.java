package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class AdminParentProfileDetailsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String parentId;
    private TextView tvParentName;
    private ImageView ivProfileImage;

    // UNCOMMENT THESE LINES to fix the error and display email/phone
    private TextView tvParentEmail, tvParentPhone;
    private LinearLayout llChildrenContainer; // <--- This was causing the "cannot find symbol" error

    private View btnSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_parents_profile_details);

        db = new DatabaseHelper(this);

        // Get Intent Data
        if (getIntent().hasExtra("PARENT_ID")) {
            parentId = getIntent().getStringExtra("PARENT_ID");
        } else {
            Toast.makeText(this, "Error: No Parent ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- Initialize Views based on YOUR XML Layout ---

        // 1. Back Button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // 2. Parent Name & Image
        tvParentName = findViewById(R.id.tv_parent_name);
        ivProfileImage = findViewById(R.id.iv_profile_image);

        // 3. Send Message Button
        btnSendMessage = findViewById(R.id.btn_contact_container);
        if (btnSendMessage != null) {
            btnSendMessage.setOnClickListener(v -> {
                Toast.makeText(this, "Message feature pending", Toast.LENGTH_SHORT).show();
            });
        }

        // UNCOMMENT THESE LINES so the variables are initialized
        tvParentEmail = findViewById(R.id.tv_parent_email);
        tvParentPhone = findViewById(R.id.tv_parent_phone);
        llChildrenContainer = findViewById(R.id.ll_children_container); // <--- Required for loadLinkedChildren()

        loadParentDetails();
        loadLinkedChildren(); // Uncomment this to actually run the function
    }

    private void loadParentDetails() {
        Cursor cursor = db.getParentById(parentId);
        if (cursor != null && cursor.moveToFirst()) {
            // Adjust column indices based on your database schema
            String name = cursor.getString(1);
            String imageUri = cursor.getString(4);

            if (tvParentName != null) tvParentName.setText(name);

            if (ivProfileImage != null && imageUri != null && !imageUri.isEmpty()) {
                Glide.with(this).load(Uri.parse(imageUri)).placeholder(R.drawable.profile_pic).into(ivProfileImage);
            }

            // Uncommented to display email and phone since the views are now active
            String email = cursor.getString(2);
            String phone = cursor.getString(3);
            if (tvParentEmail != null) tvParentEmail.setText(email);
            if (tvParentPhone != null) tvParentPhone.setText(phone);

            cursor.close();
        }
    }

    private void loadLinkedChildren() {
        if (llChildrenContainer == null) return;

        Cursor childrenCursor = db.getLinkedChildren(parentId);
        if (childrenCursor != null && childrenCursor.moveToFirst()) {
            do {
                String studentName = childrenCursor.getString(1); // Assuming col 1 is name
                TextView childView = new TextView(this);
                childView.setText("• " + studentName);
                childView.setTextSize(16);
                childView.setPadding(0, 8, 0, 8);
                childView.setTextColor(android.graphics.Color.parseColor("#1B254B")); // Added color for visibility
                llChildrenContainer.addView(childView);
            } while (childrenCursor.moveToNext());
            childrenCursor.close();
        }
    }
}
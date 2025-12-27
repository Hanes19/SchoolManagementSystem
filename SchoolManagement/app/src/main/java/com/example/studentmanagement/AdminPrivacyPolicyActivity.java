package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminPrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_privacy_policy);

        // Initialize Views
        ImageView btnBack = findViewById(R.id.btn_back);

        // Set Click Listeners
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish()); // Closes the activity and goes back
        }
    }
}
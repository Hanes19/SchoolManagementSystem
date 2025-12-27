package com.example.studentmanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class AdminHelpCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_help_center);

        // Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // --- POPULAR TOPICS LINKS ---

        // 1. App Navigation Guide
        View btnAppGuide = findViewById(R.id.btn_app_guide);
        if (btnAppGuide != null) {
            btnAppGuide.setOnClickListener(v -> {
                Intent intent = new Intent(AdminHelpCenterActivity.this, AdminAppGuideActivity.class);
                startActivity(intent);
            });
        }

        // 2. Common FAQs
        View btnFaq = findViewById(R.id.btn_faq);
        if (btnFaq != null) {
            btnFaq.setOnClickListener(v -> {
                Intent intent = new Intent(AdminHelpCenterActivity.this, AdminFaqActivity.class);
                startActivity(intent);
            });
        }

        // 3. Exporting Reports Guide
        View btnReports = findViewById(R.id.btn_reports);
        if (btnReports != null) {
            btnReports.setOnClickListener(v -> {
                Intent intent = new Intent(AdminHelpCenterActivity.this, AdminReportsGuideActivity.class);
                startActivity(intent);
            });
        }

        // --- SUPPORT CONTACTS ---

        TextView tvEmail = findViewById(R.id.tv_email);
        if (tvEmail != null) {
            tvEmail.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:support@schoolsystem.edu"));
                startActivity(intent);
            });
        }

        TextView btnCall = findViewById(R.id.btn_call);
        if (btnCall != null) {
            btnCall.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1234567890"));
                startActivity(intent);
            });
        }
    }
}
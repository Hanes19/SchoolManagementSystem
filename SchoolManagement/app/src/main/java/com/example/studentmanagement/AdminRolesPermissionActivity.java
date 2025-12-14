package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminRolesPermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_roles_permission);

        // 1. Back Button
        LinearLayout header = findViewById(R.id.header);
        header.setOnClickListener(v -> finish());

        // 2. Add Role FAB
        CardView btnAddRole = findViewById(R.id.btn_add_role);
        btnAddRole.setOnClickListener(v -> {
            Toast.makeText(this, "Add New Role Dialog", Toast.LENGTH_SHORT).show();
        });

        // You can add onClick listeners for the specific role cards here if you add IDs to them in XML
    }
}
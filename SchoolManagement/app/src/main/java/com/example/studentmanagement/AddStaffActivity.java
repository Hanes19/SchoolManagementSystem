package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AddStaffActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_directory_add_staff);

        // Back Button
        LinearLayout header = findViewById(R.id.header);
        if (header != null && header.getChildCount() > 0) {
            header.getChildAt(0).setOnClickListener(v -> finish());
        }

        // Register Button
        CardView btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> {
            Toast.makeText(this, "Staff Registration Logic goes here", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
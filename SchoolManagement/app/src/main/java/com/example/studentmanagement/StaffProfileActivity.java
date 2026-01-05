package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StaffProfileActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private TextView tvName, tvRole, tvEmail, tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_profile);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        tvName = findViewById(R.id.tv_profile_name);
        tvRole = findViewById(R.id.tv_profile_role);
        tvEmail = findViewById(R.id.tv_profile_email);
        tvStatus = findViewById(R.id.tv_profile_status);

        loadProfileData();

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_change_password).setOnClickListener(v ->
                startActivity(new Intent(this, ChangePasswordActivity.class))
        );

        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            session.logoutUser();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadProfileData() {
        String userId = session.getUserId();
        String name = db.getUserName(userId);
        String role = session.getRole();
        String email = userId + "@school.edu";

        if(tvName != null) tvName.setText(name);
        if(tvRole != null) tvRole.setText(role);
        if(tvEmail != null) tvEmail.setText(email);
        if(tvStatus != null) tvStatus.setText("Active");
    }
}
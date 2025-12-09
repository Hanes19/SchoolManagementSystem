package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminRoleActivity extends AppCompatActivity {

    DatabaseHelper db;
    String targetUserId = "stud01"; // HARDCODED for testing. In real app, pass this via Intent

    // UI Elements
    Switch statusSwitch;
    Button btnReactivate, btnPerformActions;
    TextView tvStatusLabel, btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_actions_userdirectory); // Your existing XML

        db = new DatabaseHelper(this);

        // Check for Intent Data (Uncomment if you are passing data from Directory)
        // if (getIntent().hasExtra("USER_ID")) {
        //     targetUserId = getIntent().getStringExtra("USER_ID");
        // }

        // Bind Views
        statusSwitch = findViewById(R.id.switch3);
        btnReactivate = findViewById(R.id.button21);
        btnPerformActions = findViewById(R.id.button22);
        tvStatusLabel = findViewById(R.id.textView24);

        // Note: You might need to make the "Reset Password" TextView clickable in XML or find it by ID
        // Assuming textView29 is the label "Reset Password"
        btnResetPassword = findViewById(R.id.textView29);

        // 1. Load Initial State
        loadUserStatus();

        // 2. Switch Toggle Listener
        statusSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> updateStatusUI(isChecked));

        // 3. Reactivate Button Listener
        btnReactivate.setOnClickListener(v -> {
            statusSwitch.setChecked(true);
            updateStatusUI(true);
        });

        // 4. "Perform Selected Actions" (Save Changes)
        btnPerformActions.setOnClickListener(v -> {
            boolean isActive = statusSwitch.isChecked();
            boolean success = db.updateUserStatus(targetUserId, isActive);

            if (success) {
                db.logAction("admin", "Updated status for user: " + targetUserId); // Log it
                Toast.makeText(this, "Account Updated Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Reset Password Logic (Simple implementation)
        btnResetPassword.setOnClickListener(v -> {
            boolean reset = db.resetPassword(targetUserId, "123456"); // Resets to default
            if(reset) {
                db.logAction("admin", "Reset password for user: " + targetUserId);
                Toast.makeText(this, "Password reset to '123456'", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUserStatus() {
        boolean isActive = db.isUserActive(targetUserId);
        statusSwitch.setChecked(isActive);
        updateStatusUI(isActive);
    }

    private void updateStatusUI(boolean isActive) {
        if (isActive) {
            tvStatusLabel.setText("Account Status: Active");
        } else {
            tvStatusLabel.setText("Account Status: Inactive");
        }
    }
}
package com.example.studentmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;



public class AdminEditParentProfileActivity extends AppCompatActivity {

    private EditText etName, etJob, etPhone, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_parent_profile);

        // 1. Initialize Views
        ImageView btnBack = findViewById(R.id.btn_back);
        CardView btnSave = findViewById(R.id.btn_save_container);

        etName = findViewById(R.id.et_edit_parent_name);
        etJob = findViewById(R.id.et_edit_parent_job);
        etPhone = findViewById(R.id.et_edit_parent_phone);
        etEmail = findViewById(R.id.et_edit_parent_email);

        // 2. Back Button
        btnBack.setOnClickListener(v -> finish());

        // 3. Save Button with Validation
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveData();
            }
        });
    }

    // Function to check if fields are empty
    private boolean validateInputs() {
        if (TextUtils.isEmpty(etName.getText())) {
            etName.setError("Full name is required");
            etName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etPhone.getText())) {
            etPhone.setError("Phone number is required");
            etPhone.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("Email address is required");
            etEmail.requestFocus();
            return false;
        }
        return true;
    }

    // Function to simulate saving data
    private void saveData() {
        String name = etName.getText().toString();

        // In a real app, this is where you would update the database
        Toast.makeText(this, "Profile for " + name + " updated successfully.", Toast.LENGTH_SHORT).show();

        // Return to the previous screen
        finish();
    }
}
package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminEditProfileActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etName, etEmail, etPhone, etAddress, etEmergencyName, etEmergencyPhone;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_profile);

        db = new DatabaseHelper(this);
        userId = getIntent().getStringExtra("USER_ID");

        // Bind Views
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        etEmergencyName = findViewById(R.id.et_emergency_name);
        etEmergencyPhone = findViewById(R.id.et_emergency_phone);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_save_changes).setOnClickListener(v -> saveChanges());

        loadData();
    }

    private void loadData() {
        Cursor cursor = db.getUserDetails(userId);
        if (cursor != null && cursor.moveToFirst()) {
            etName.setText(cursor.getString(cursor.getColumnIndexOrThrow("full_name")));
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone_number")));

            // Handle potentially null columns gracefully
            int addrIndex = cursor.getColumnIndex("address");
            if (addrIndex != -1) etAddress.setText(cursor.getString(addrIndex));

            int emNameIndex = cursor.getColumnIndex("emergency_contact_name");
            if (emNameIndex != -1) etEmergencyName.setText(cursor.getString(emNameIndex));

            int emPhoneIndex = cursor.getColumnIndex("emergency_contact_phone");
            if (emPhoneIndex != -1) etEmergencyPhone.setText(cursor.getString(emPhoneIndex));

            cursor.close();
        }
    }

    private void saveChanges() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String emName = etEmergencyName.getText().toString().trim();
        String emPhone = etEmergencyPhone.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and Email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Updated method call
        if (db.updateUserProfile(userId, name, email, phone, address, emName, emPhone)) {
            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminEditProfileActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etName, etEmail, etPhone;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_profile); // Reusing this generic edit layout

        db = new DatabaseHelper(this);
        userId = getIntent().getStringExtra("USER_ID");

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_save_changes).setOnClickListener(v -> saveChanges());

        loadData();
    }

    private void loadData() {
        Cursor cursor = db.getUserDetails(userId);
        if (cursor.moveToFirst()) {
            etName.setText(cursor.getString(cursor.getColumnIndexOrThrow("full_name")));
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            // etPhone.setText(...) // If phone column exists
        }
        cursor.close();
    }

    private void saveChanges() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and Email required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.updateUserProfile(userId, name, email, phone)) {
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
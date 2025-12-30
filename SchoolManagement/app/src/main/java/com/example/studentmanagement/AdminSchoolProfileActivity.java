package com.example.studentmanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminSchoolProfileActivity extends AppCompatActivity {

    private EditText etName, etAddress, etPhone, etEmail;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_school_profile);

        prefs = getSharedPreferences("SchoolPrefs", Context.MODE_PRIVATE);

        // FIX: Updated IDs to match admin_school_profile.xml
        etName = findViewById(R.id.et_school_name); // This one usually has the prefix in your XML
        etAddress = findViewById(R.id.et_address);  // CHANGED: Removed 'school_'
        etPhone = findViewById(R.id.et_phone);      // CHANGED: Removed 'school_'
        etEmail = findViewById(R.id.et_email);      // CHANGED: Removed 'school_'

        findViewById(R.id.btn_save).setOnClickListener(v -> finish());

        // FIX: Check your XML for the save button ID. It is likely 'btn_save' or 'btn_save_changes'
        // If your XML has android:id="@+id/btn_save", use this:
        findViewById(R.id.btn_save).setOnClickListener(v -> saveProfile());

        loadProfile();
    }

    private void loadProfile() {
        etName.setText(prefs.getString("NAME", "My School"));
        etAddress.setText(prefs.getString("ADDRESS", "123 Education Lane"));
        etPhone.setText(prefs.getString("PHONE", "555-0123"));
        etEmail.setText(prefs.getString("EMAIL", "admin@school.com"));
    }

    private void saveProfile() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("NAME", etName.getText().toString());
        editor.putString("ADDRESS", etAddress.getText().toString());
        editor.putString("PHONE", etPhone.getText().toString());
        editor.putString("EMAIL", etEmail.getText().toString());
        editor.apply();
        Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
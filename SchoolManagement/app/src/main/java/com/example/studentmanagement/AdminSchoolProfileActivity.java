package com.example.studentmanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminSchoolProfileActivity extends AppCompatActivity {

    private EditText etName, etCode, etMotto, etEmail, etPhone, etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_school_profile);

        // 1. Back Button
        LinearLayout header = findViewById(R.id.header);
        header.setOnClickListener(v -> finish());

        // 2. Initialize Views (Add these IDs to your XML!)
        etName = findViewById(R.id.et_school_name);
        etCode = findViewById(R.id.et_school_code);
        etMotto = findViewById(R.id.et_motto);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);

        TextView btnSave = findViewById(R.id.btn_save);

        // 3. Load Data
        loadProfileData();

        // 4. Save Button Logic
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> saveProfileData());
        }
    }

    private void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences("SchoolConfig", MODE_PRIVATE);
        if(etName != null) etName.setText(prefs.getString("school_name", "Springfield High School"));
        if(etCode != null) etCode.setText(prefs.getString("school_code", "SHS-2025"));
        if(etMotto != null) etMotto.setText(prefs.getString("school_motto", "Excellence in Education"));
        if(etEmail != null) etEmail.setText(prefs.getString("school_email", ""));
        if(etPhone != null) etPhone.setText(prefs.getString("school_phone", ""));
        if(etAddress != null) etAddress.setText(prefs.getString("school_address", ""));
    }

    private void saveProfileData() {
        SharedPreferences.Editor editor = getSharedPreferences("SchoolConfig", MODE_PRIVATE).edit();
        if(etName != null) editor.putString("school_name", etName.getText().toString());
        if(etCode != null) editor.putString("school_code", etCode.getText().toString());
        if(etMotto != null) editor.putString("school_motto", etMotto.getText().toString());
        if(etEmail != null) editor.putString("school_email", etEmail.getText().toString());
        if(etPhone != null) editor.putString("school_phone", etPhone.getText().toString());
        if(etAddress != null) editor.putString("school_address", etAddress.getText().toString());

        editor.apply();
        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}
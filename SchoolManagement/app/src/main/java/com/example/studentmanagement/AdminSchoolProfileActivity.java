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

        etName = findViewById(R.id.et_school_name);
        etAddress = findViewById(R.id.et_school_address);
        etPhone = findViewById(R.id.et_school_phone);
        etEmail = findViewById(R.id.et_school_email);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_save_profile).setOnClickListener(v -> saveProfile());

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

        Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
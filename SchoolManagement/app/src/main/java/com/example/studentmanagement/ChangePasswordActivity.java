package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etNew, etConfirm;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_change_password);

        db = new DatabaseHelper(this);
        // Get User ID from intent or session
        userId = "ADMIN-001";

        etNew = findViewById(R.id.et_new_pass);
        etConfirm = findViewById(R.id.et_confirm_pass);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_save_password).setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String newPass = etNew.getText().toString();
        String confirmPass = etConfirm.getText().toString();

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.updatePassword(userId, newPass)) {
            Toast.makeText(this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error Updating Password", Toast.LENGTH_SHORT).show();
        }
    }
}
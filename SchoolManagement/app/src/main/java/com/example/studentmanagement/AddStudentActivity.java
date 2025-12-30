package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.Random;

public class AddStudentActivity extends AppCompatActivity {

    private EditText etName, etId, etYear, etEmail, etPrevSchool, etTransferCert, etEmergency;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_directory_add_student);

        db = new DatabaseHelper(this);

        // Bind Views
        etName = findViewById(R.id.et_student_name);
        etId = findViewById(R.id.et_student_id);
        etYear = findViewById(R.id.et_student_year);
        etEmail = findViewById(R.id.et_student_email);
        etPrevSchool = findViewById(R.id.et_prev_school);
        etTransferCert = findViewById(R.id.et_transfer_cert);
        etEmergency = findViewById(R.id.et_emergency_contact);

        // Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        if(btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Auto-Generate ID
        TextView btnGenId = findViewById(R.id.btn_generate_student_id);
        btnGenId.setOnClickListener(v -> {
            int randomId = 100000 + new Random().nextInt(900000);
            etId.setText("STU" + randomId);
        });

        // Register Button
        CardView btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> saveStudent());
    }

    private void saveStudent() {
        String name = etName.getText().toString().trim();
        String id = etId.getText().toString().trim();
        String year = etYear.getText().toString().trim();
        String prevSchool = etPrevSchool.getText().toString().trim();
        String transferCert = etTransferCert.getText().toString().trim();
        // Simple logic to parse emergency contact if needed, or save as one string

        if (name.isEmpty() || id.isEmpty() || year.isEmpty()) {
            Toast.makeText(this, "Please fill in required fields (Name, ID, Class)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the method in DatabaseHelper (Ensure you added this method in the previous step)
        boolean isInserted = db.addStudentWithHistory(name, id, prevSchool, transferCert);

        if (isInserted) {
            Toast.makeText(this, "Student Registered Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error: Student ID may already exist.", Toast.LENGTH_SHORT).show();
        }
    }
}
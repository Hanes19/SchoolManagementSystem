package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AdminTeacherListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GLOBAL FIX:
        // Redirect to the working User Directory
        Intent intent = new Intent(this, AdminUserDirectoryActivity.class);
        intent.putExtra("type", "Teacher");
        startActivity(intent);

        finish();
    }
}
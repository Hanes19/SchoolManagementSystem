package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AdminStudentListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GLOBAL FIX:
        // Instead of showing an empty screen, automatically redirect
        // the user to the working 'AdminUserDirectoryActivity'.
        Intent intent = new Intent(this, AdminUserDirectoryActivity.class);
        // Optional: Signal that we want to see Students (if your directory supports filtering)
        intent.putExtra("type", "Student");
        startActivity(intent);

        // Close this empty activity so the user doesn't get stuck here when pressing Back
        finish();
    }
}
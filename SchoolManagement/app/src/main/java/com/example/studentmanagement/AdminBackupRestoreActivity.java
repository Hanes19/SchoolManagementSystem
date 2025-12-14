package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminBackupRestoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_backup_restore);

        // 1. Back Button
        LinearLayout header = findViewById(R.id.header);
        header.setOnClickListener(v -> finish());

        // 2. Create Backup Action
        // IMPORTANT: Add android:id="@+id/btn_create_backup" to the Create Backup CardView in XML
        CardView btnCreateBackup = findViewById(R.id.btn_create_backup);
        if (btnCreateBackup != null) {
            btnCreateBackup.setOnClickListener(v -> performBackup());
        }

        // 3. Restore Action
        // IMPORTANT: Add android:id="@+id/btn_restore" to the Restore CardView in XML
        CardView btnRestore = findViewById(R.id.btn_restore);
        if (btnRestore != null) {
            btnRestore.setOnClickListener(v -> performRestore());
        }
    }

    private void performBackup() {
        Toast.makeText(this, "Creating Database Backup...", Toast.LENGTH_SHORT).show();
        // Implement database export logic here
    }

    private void performRestore() {
        Toast.makeText(this, "Select File to Restore...", Toast.LENGTH_SHORT).show();
        // Implement file picker and database import logic here
    }
}
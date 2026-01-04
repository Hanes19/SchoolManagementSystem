package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminBackupRestoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_backup_restore);

        // 1. Back Button
        View btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Last Backup Text
        TextView txtLastBackup = findViewById(R.id.txt_last_backup);
        if (txtLastBackup != null) {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
            txtLastBackup.setText("Last Backup: " + currentTime);
        }

        // 3. Backup Button
        View btnBackup = findViewById(R.id.btn_backup);
        if (btnBackup != null) {
            btnBackup.setOnClickListener(v -> {
                Toast.makeText(this, "Backup Started...", Toast.LENGTH_SHORT).show();
            });
        }

        // 4. Restore Button
        View btnRestore = findViewById(R.id.btn_restore);
        if (btnRestore != null) {
            btnRestore.setOnClickListener(v -> {
                Toast.makeText(this, "Restore Started...", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
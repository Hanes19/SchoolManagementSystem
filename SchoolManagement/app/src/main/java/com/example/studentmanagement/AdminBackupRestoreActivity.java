package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminBackupRestoreActivity extends AppCompatActivity {

    private TextView tvLastBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_backup_restore);

        tvLastBackup = findViewById(R.id.tv_last_backup);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        CardView btnBackup = findViewById(R.id.card_create_backup);
        btnBackup.setOnClickListener(v -> {
            // Simulate Backup Process
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
            tvLastBackup.setText("Last Backup: " + time);
            Toast.makeText(this, "Backup Created Successfully!", Toast.LENGTH_SHORT).show();
        });

        CardView btnRestore = findViewById(R.id.card_restore_backup);
        btnRestore.setOnClickListener(v -> {
            Toast.makeText(this, "Restore feature requires storage permissions.", Toast.LENGTH_LONG).show();
        });
    }
}
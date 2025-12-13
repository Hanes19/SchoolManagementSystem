package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SystemLogActivity extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout logContainer;
    ImageView btnBack;
    Button btnExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // UPDATED: Pointing to the new layout file name 'admin_system_log'
        setContentView(R.layout.admin_system_log);

        db = new DatabaseHelper(this);

        logContainer = findViewById(R.id.logContainer);
        btnBack = findViewById(R.id.btnBack);
        btnExport = findViewById(R.id.btnExport);

        // Back Button
        btnBack.setOnClickListener(v -> finish());

        // Export Button (Stub)
        btnExport.setOnClickListener(v ->
                Toast.makeText(this, "Export feature not implemented yet.", Toast.LENGTH_SHORT).show()
        );

        // Load and Display Logs
        loadLogs();
    }

    private void loadLogs() {
        logContainer.removeAllViews();

        Cursor cursor = db.getAllLogs();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get data from DB
                String userId = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
                String action = cursor.getString(cursor.getColumnIndexOrThrow("action"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                // Add row to UI
                View row = createLogRow(action, userId, timestamp);
                logContainer.addView(row);

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            TextView tv = new TextView(this);
            tv.setText("No logs available.");
            tv.setPadding(30, 30, 30, 30);
            logContainer.addView(tv);
        }
    }

    private View createLogRow(String action, String user, String time) {
        LinearLayout wrapper = new LinearLayout(this);
        wrapper.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 16, 0, 0);
        wrapper.setLayoutParams(params);

        TextView tvAction = new TextView(this);
        tvAction.setText(action);
        tvAction.setTextSize(16);
        tvAction.setTextColor(Color.parseColor("#424242"));
        tvAction.setPadding(30, 30, 30, 10);
        tvAction.setBackgroundColor(Color.WHITE);

        TextView tvDetails = new TextView(this);
        tvDetails.setText("User: " + user + " | " + time);
        tvDetails.setTextSize(12);
        tvDetails.setTextColor(Color.parseColor("#757575"));
        tvDetails.setPadding(30, 0, 30, 30);
        tvDetails.setBackgroundColor(Color.WHITE);

        wrapper.addView(tvAction);
        wrapper.addView(tvDetails);
        return wrapper;
    }
}
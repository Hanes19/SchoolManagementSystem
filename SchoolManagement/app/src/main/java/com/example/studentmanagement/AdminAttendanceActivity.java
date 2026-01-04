package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminAttendanceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_attendance_sheet);

        // Header Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Date Display
        TextView tvDate = findViewById(R.id.tv_date); // Now matches XML
        if (tvDate != null) {
            String currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
            tvDate.setText(currentDate);
        }

        // Save Button (Fixed: Uses View instead of Button to avoid ClassCastException)
        View btnSave = findViewById(R.id.btn_save);
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                Toast.makeText(this, "Attendance Saved Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }

        // RecyclerView (Optional, currently using hardcoded ScrollView in XML)
        // If you plan to switch to dynamic list later:
        RecyclerView rv = findViewById(R.id.rv_attendance_list);
        if (rv != null) rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
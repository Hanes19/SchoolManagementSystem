package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
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

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        android.widget.TextView tvDate = findViewById(R.id.tv_date);
        if (tvDate != null) {
            String currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
            tvDate.setText(currentDate);
        }

        Button btnSave = findViewById(R.id.btn_save_attendance);
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                Toast.makeText(this, "Attendance Saved", Toast.LENGTH_SHORT).show();
                finish();
            });
        }

        RecyclerView rv = findViewById(R.id.rv_attendance_list);
        if (rv != null) rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
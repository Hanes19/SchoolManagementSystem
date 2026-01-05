package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_notification);

        // Initialize Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Optional: Logic for "Clear All"
        // To use this, you must add android:id="@+id/btn_clear_all" to the "Clear All" TextView in your XML
        /*
        TextView btnClearAll = findViewById(R.id.btn_clear_all);
        if (btnClearAll != null) {
            btnClearAll.setOnClickListener(v -> {
                Toast.makeText(this, "All notifications cleared", Toast.LENGTH_SHORT).show();
            });
        }
        */
    }
}
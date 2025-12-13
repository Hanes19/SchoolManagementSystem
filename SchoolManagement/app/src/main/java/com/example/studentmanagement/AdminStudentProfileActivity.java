package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminStudentProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_student_profile); // Loads your XML

        // Back Button Logic
        ImageView btnBack = findViewById(R.id.header).findViewWithTag("back");
        // Note: Since your XML header uses an ImageView for back but no ID,
        // we'll rely on a simpler find if you add an ID later.
        // For now, let's use a generic approach or assume you add ID "btnBack" to the profile XMLs.

        // Let's assume you will add android:id="@+id/btnBack" to the back arrow in your profile XMLs
        // If not, clicks won't work, but the page will load.
        ImageView back = findViewById(R.id.btnBack); // Make sure to add this ID in XML
        if(back != null) {
            back.setOnClickListener(v -> finish());
        }
    }
}
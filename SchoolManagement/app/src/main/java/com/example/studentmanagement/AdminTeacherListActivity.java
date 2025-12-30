package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminTeacherListActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_directory_teacher); // Ensure XML exists

        db = new DatabaseHelper(this);
        llList = findViewById(R.id.ll_user_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.fab_add_user).setOnClickListener(v ->
                startActivity(new Intent(this, AddTeacherActivity.class))
        );

        loadTeachers();
    }

    private void loadTeachers() {
        llList.removeAllViews();
        Cursor cursor = db.getUsersByRole("Teacher");
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String id = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));

                View view = inflater.inflate(R.layout.item_student_row, llList, false);
                TextView tvName = view.findViewById(R.id.tv_student_name);
                TextView tvId = view.findViewById(R.id.tv_student_id);

                tvName.setText(name);
                tvId.setText("ID: " + id);

                llList.addView(view);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
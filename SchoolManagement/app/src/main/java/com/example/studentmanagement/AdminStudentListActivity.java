package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminStudentListActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_student_list);

        db = new DatabaseHelper(this);
        llList = findViewById(R.id.ll_user_list);
        etSearch = findViewById(R.id.et_search);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.fab_add_user).setOnClickListener(v ->
                startActivity(new Intent(this, AddStudentActivity.class))
        );

        etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadUsers(s.toString());
            }
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers("");
    }

    private void loadUsers(String query) {
        llList.removeAllViews();
        Cursor cursor = db.getUsersByRole("Student");
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String id = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));

                if (!query.isEmpty() && !name.toLowerCase().contains(query.toLowerCase())) {
                    continue;
                }

                // Reuse item_student_row.xml
                View view = inflater.inflate(R.layout.item_student_row, llList, false);

                TextView tvName = view.findViewById(R.id.tv_student_name); // Check IDs in item xml
                TextView tvId = view.findViewById(R.id.tv_student_id);
                // TextView tvEmail = view.findViewById(R.id.tv_email); // If exists

                tvName.setText(name);
                tvId.setText("ID: " + id);

                view.setOnClickListener(v -> {
                    // Open Profile Details (Optional)
                    // Intent intent = new Intent(this, AdminStudentProfileActivity.class);
                    // intent.putExtra("USER_ID", id);
                    // startActivity(intent);
                });

                llList.addView(view);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
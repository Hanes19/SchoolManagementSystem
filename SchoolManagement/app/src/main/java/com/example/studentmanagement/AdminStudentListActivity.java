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
        // FIX 1: Changed layout to match your XML filename
        setContentView(R.layout.admin_user_directory_students);

        db = new DatabaseHelper(this);

        // These IDs must exist in admin_user_directory_students.xml
        llList = findViewById(R.id.ll_user_list);
        etSearch = findViewById(R.id.et_search); // Ensure this ID exists if you have a search bar, otherwise remove/comment

        // FIX: The XML has an ImageView in the header, likely acting as back button.
        // Ensure the ID in XML is 'btn_back' or change it here.
        View backBtn = findViewById(R.id.btn_back);
        if (backBtn != null) backBtn.setOnClickListener(v -> finish());

        findViewById(R.id.fab_add_user).setOnClickListener(v ->
                startActivity(new Intent(this, AddStudentActivity.class))
        );

        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    loadUsers(s.toString());
                }
                public void afterTextChanged(Editable s) {}
            });
        }

        loadUsers("");
    }

    private void loadUsers(String query) {
        if (llList == null) return;
        llList.removeAllViews();
        Cursor cursor = db.getUsersByRole("Student");
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String id = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));

                if (!query.isEmpty() && !name.toLowerCase().contains(query.toLowerCase())) {
                    continue;
                }

                // Ensure 'item_student_row.xml' exists and has these IDs
                View view = inflater.inflate(R.layout.item_student_row, llList, false);

                TextView tvName = view.findViewById(R.id.tv_student_name);
                TextView tvId = view.findViewById(R.id.tv_student_id);

                tvName.setText(name);
                tvId.setText("ID: " + id);

                llList.addView(view);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
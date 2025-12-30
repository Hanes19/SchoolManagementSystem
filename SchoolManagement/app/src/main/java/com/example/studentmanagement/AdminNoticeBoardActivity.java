package com.example.studentmanagement;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminNoticeBoardActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_notice_board);

        db = new DatabaseHelper(this);
        llList = findViewById(R.id.ll_notice_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.fab_add_notice).setOnClickListener(v -> showAddDialog());

        loadNotices();
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Post New Notice");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etTitle = new EditText(this);
        etTitle.setHint("Title (e.g., Holiday)");
        layout.addView(etTitle);

        final EditText etDesc = new EditText(this);
        etDesc.setHint("Details...");
        etDesc.setHeight(200);
        layout.addView(etDesc);

        builder.setView(layout);

        builder.setPositiveButton("Post", (dialog, which) -> {
            String title = etTitle.getText().toString();
            String desc = etDesc.getText().toString();
            if (!title.isEmpty()) {
                db.addNotice(title, desc, "All");
                loadNotices();
                Toast.makeText(this, "Notice Posted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void loadNotices() {
        llList.removeAllViews();
        Cursor cursor = db.getAllNotices();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date_posted"));

                // Reuse a simple layout or item_role_card.xml
                View view = inflater.inflate(R.layout.item_role_card, llList, false);
                TextView tvTitle = view.findViewById(R.id.tv_role_name);
                TextView tvDesc = view.findViewById(R.id.tv_role_desc);

                tvTitle.setText(title);
                tvDesc.setText(date + "\n" + desc);

                llList.addView(view);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
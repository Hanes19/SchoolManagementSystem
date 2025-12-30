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

public class AdminExamCategoriesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_exam_categories);

        db = new DatabaseHelper(this);
        llList = findViewById(R.id.ll_exam_list); // Ensure ID exists in XML

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.fab_add_exam).setOnClickListener(v -> showAddDialog()); // Ensure ID exists

        loadExams();
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Exam Category");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputName = new EditText(this);
        inputName.setHint("Exam Name (e.g., Finals 2025)");
        layout.addView(inputName);

        final EditText inputStart = new EditText(this);
        inputStart.setHint("Start Date (YYYY-MM-DD)");
        layout.addView(inputStart);

        builder.setView(layout);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String name = inputName.getText().toString();
            String start = inputStart.getText().toString();
            if (!name.isEmpty()) {
                db.addExamCategory(name, start, "TBD");
                loadExams();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void loadExams() {
        llList.removeAllViews();
        Cursor cursor = db.getAllExams();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("exam_name"));
                String start = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                // Inflate a simple card layout (or reuse an existing item layout)
                // For simplicity, reusing item_role_card.xml structure or similar
                View view = inflater.inflate(R.layout.item_role_card, llList, false);
                TextView tvTitle = view.findViewById(R.id.tv_role_name); // Assuming IDs match
                TextView tvDesc = view.findViewById(R.id.tv_role_desc);

                tvTitle.setText(name);
                tvDesc.setText(start + " • " + status);

                llList.addView(view);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
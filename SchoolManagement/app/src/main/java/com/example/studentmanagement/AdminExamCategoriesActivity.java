package com.example.studentmanagement;

import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminExamCategoriesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_exam_categories);

        db = new DatabaseHelper(this);
        llList = findViewById(R.id.ll_exam_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.fab_add_exam).setOnClickListener(v -> showAddDialog());

        loadExams();
    }

    private void loadExams() {
        llList.removeAllViews();
        Cursor cursor = db.getAllExamCategories();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String start = cursor.getString(2);
                String end = cursor.getString(3);

                addExamCard(id, name, start + " to " + end);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void addExamCard(String id, String name, String dateRange) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        card.setLayoutParams(params);
        card.setCardBackgroundColor(Color.WHITE);
        card.setRadius(16);
        card.setContentPadding(32, 32, 32, 32);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextSize(18);
        tvName.setTextColor(Color.parseColor("#1B254B"));
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvDates = new TextView(this);
        tvDates.setText(dateRange);
        tvDates.setTextColor(Color.GRAY);

        layout.addView(tvName);
        layout.addView(tvDates);
        card.addView(layout);

        // Long click to delete
        card.setOnLongClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Exam?")
                    .setMessage("Are you sure you want to delete " + name + "?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        db.deleteExamCategory(id);
                        loadExams();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });

        llList.addView(card);
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_role, null); // Reusing generic dialog layout if available, or create custom
        // Customizing the view dynamically for simplicity
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etName = new EditText(this);
        etName.setHint("Exam Name (e.g. Final Term)");
        layout.addView(etName);

        final EditText etStart = new EditText(this);
        etStart.setHint("Start Date (YYYY-MM-DD)");
        layout.addView(etStart);

        final EditText etEnd = new EditText(this);
        etEnd.setHint("End Date (YYYY-MM-DD)");
        layout.addView(etEnd);

        builder.setView(layout);
        builder.setTitle("Add New Exam");
        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = etName.getText().toString();
            String start = etStart.getText().toString();
            String end = etEnd.getText().toString();
            if(!name.isEmpty()) {
                db.addExamCategory(name, start, end);
                loadExams();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
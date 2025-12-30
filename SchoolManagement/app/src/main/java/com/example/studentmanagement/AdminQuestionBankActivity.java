package com.example.studentmanagement;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminQuestionBankActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout llList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_question_bank);

        dbHelper = new DatabaseHelper(this);
        // Ensure your XML layout has a LinearLayout with this ID inside a ScrollView
        llList = findViewById(R.id.ll_question_list);

        findViewById(R.id.btn_back_questions).setOnClickListener(v -> finish());

        // Floating Action Button to Add Question
        findViewById(R.id.fab_add_question).setOnClickListener(v -> showAddDialog());

        loadQuestions();
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Question");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etQuestion = new EditText(this);
        etQuestion.setHint("Enter Question");
        layout.addView(etQuestion);

        final Spinner spSubject = new Spinner(this);
        String[] subjects = {"Mathematics", "Science", "History", "English"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subjects);
        spSubject.setAdapter(adapter);
        layout.addView(spSubject);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String question = etQuestion.getText().toString();
            String subject = spSubject.getSelectedItem().toString();
            if (!question.isEmpty()) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("question_text", question);
                values.put("subject", subject);
                values.put("grade_level", "10"); // Default or add spinner
                values.put("type", "Multiple Choice"); // Default

                db.insert("question_bank", null, values);
                loadQuestions();
                Toast.makeText(this, "Question Added", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void loadQuestions() {
        llList.removeAllViews();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Ensure table exists or handle exception
        Cursor cursor = db.rawQuery("SELECT * FROM question_bank ORDER BY subject ASC", null);

        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String grade = cursor.getString(cursor.getColumnIndexOrThrow("grade_level"));
                String text = cursor.getString(cursor.getColumnIndexOrThrow("question_text"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));

                // Reusing item_expense_row.xml as requested
                View view = inflater.inflate(R.layout.item_expense_row, llList, false);

                // FIX: Map to the IDs that actually exist in item_expense_row.xml
                TextView tvTitle = view.findViewById(R.id.tv_expense_title);
                TextView tvDesc = view.findViewById(R.id.tv_requested_by); // CHANGED from tv_expense_category

                tvTitle.setText(text);
                tvDesc.setText(String.format("%s • %s (%s)", subject, grade, type));

                llList.addView(view);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
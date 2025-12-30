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

        // Create a layout for the dialog inputs
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        // Subject Input
        final EditText etSubject = new EditText(this);
        etSubject.setHint("Subject (e.g. Math)");
        layout.addView(etSubject);

        // Grade Input
        final EditText etGrade = new EditText(this);
        etGrade.setHint("Grade Level (e.g. Grade 10)");
        layout.addView(etGrade);

        // Question Input
        final EditText etQuestion = new EditText(this);
        etQuestion.setHint("Question Text");
        layout.addView(etQuestion);

        // Type Dropdown (Spinner)
        final Spinner spType = new Spinner(this);
        String[] types = {"MCQ", "Theory"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spType.setAdapter(adapter);
        layout.addView(spType);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String subject = etSubject.getText().toString();
            String grade = etGrade.getText().toString();
            String question = etQuestion.getText().toString();
            String type = spType.getSelectedItem().toString();

            if (!subject.isEmpty() && !question.isEmpty()) {
                saveQuestionToDB(subject, grade, question, type);
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void saveQuestionToDB(String subject, String grade, String question, String type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // These keys must match the columns in your DatabaseHelper TABLE_QUESTION_BANK creation string
        values.put("subject", subject);
        values.put("grade_level", grade);
        values.put("question_text", question);
        values.put("type", type);

        long id = db.insert("question_bank", null, values);

        if (id != -1) {
            Toast.makeText(this, "Question Added Successfully!", Toast.LENGTH_SHORT).show();
            loadQuestions(); // Refresh the list
        } else {
            Toast.makeText(this, "Error Saving Question", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadQuestions() {
        llList.removeAllViews();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Fetch all questions ordered by Subject
        Cursor cursor = db.rawQuery("SELECT * FROM question_bank ORDER BY subject ASC", null);

        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                // Get data from cursor
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String grade = cursor.getString(cursor.getColumnIndexOrThrow("grade_level"));
                String text = cursor.getString(cursor.getColumnIndexOrThrow("question_text"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));

                // Inflate a simple row layout
                // Using 'item_expense_row' as a fallback if you don't have a specific 'item_question.xml'
                // Ideally, create a specific layout for this.
                View view = inflater.inflate(R.layout.item_expense_row, llList, false);

                TextView tvTitle = view.findViewById(R.id.tv_expense_title); // Reuse as Question Text
                TextView tvDesc = view.findViewById(R.id.tv_expense_category); // Reuse as Subject/Grade

                tvTitle.setText(text);
                tvDesc.setText(String.format("%s • %s (%s)", subject, grade, type));

                llList.addView(view);
            } while (cursor.moveToNext());
        } else {
            // Optional: Show "No questions found" text
        }
        cursor.close();
    }
}
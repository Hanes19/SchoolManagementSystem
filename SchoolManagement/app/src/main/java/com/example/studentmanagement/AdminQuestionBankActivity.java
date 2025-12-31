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

        // This ID must exist in admin_question_bank.xml
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
        etQuestion.setHint("Question Text");
        layout.addView(etQuestion);

        final EditText etSubject = new EditText(this);
        etSubject.setHint("Subject (e.g. Science)");
        layout.addView(etSubject);

        // Simple Grade Input
        final EditText etGrade = new EditText(this);
        etGrade.setHint("Grade Level (e.g. 10)");
        layout.addView(etGrade);

        // Simple Type Selector (In real app, use Spinner)
        final EditText etType = new EditText(this);
        etType.setHint("Type (MCQ, Theory)");
        layout.addView(etType);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String qText = etQuestion.getText().toString();
            String sub = etSubject.getText().toString();
            String grade = etGrade.getText().toString();
            String type = etType.getText().toString();

            if (!qText.isEmpty() && !sub.isEmpty()) {
                // Insert into DB (Mock logic or actual DB call)
                // dbHelper.addQuestion(qText, sub, grade, type);
                Toast.makeText(this, "Question Added", Toast.LENGTH_SHORT).show();
                // Reload list to show changes (if DB is connected)
                // loadQuestions();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void loadQuestions() {
        if (llList == null) return;

        llList.removeAllViews();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Ensure table exists or handle exception gracefully
        try {
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

                    // FIXED: Use IDs that actually exist in item_expense_row.xml
                    TextView tvTitle = view.findViewById(R.id.tv_expense_title);

                    // FIXED: Changed from tv_requested_by to tv_expense_category
                    TextView tvDesc = view.findViewById(R.id.tv_expense_category);

                    // We can hide the amount view since it's not needed for questions
                    TextView tvAmount = view.findViewById(R.id.tv_expense_amount);
                    tvAmount.setVisibility(View.GONE);

                    tvTitle.setText(text);
                    tvDesc.setText(String.format("%s • Grade %s (%s)", subject, grade, type));

                    llList.addView(view);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // Table might not exist yet
            Toast.makeText(this, "No questions found.", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.studentmanagement;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminQuestionBankActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_question_bank);

        db = new DatabaseHelper(this);
        llList = findViewById(R.id.ll_question_list); // Ensure ID added

        findViewById(R.id.btn_back_questions).setOnClickListener(v -> finish());

        findViewById(R.id.fab_add_question).setOnClickListener(v -> showAddDialog());

        loadQuestions();
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Question");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputText = new EditText(this);
        inputText.setHint("Question Text");
        layout.addView(inputText);

        builder.setView(layout);
        builder.setPositiveButton("Add", (dialog, which) -> {
            db.addQuestion("Mathematics", "Grade 10", inputText.getText().toString(), "MCQ");
            loadQuestions();
        });
        builder.show();
    }

    private void loadQuestions() {
        llList.removeAllViews();
        // Hardcoded filter for demo
        Cursor cursor = db.getQuestions("Mathematics", "Grade 10");
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String text = cursor.getString(cursor.getColumnIndexOrThrow("question_text"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));

                // Reuse a simple card or create item_question_row.xml
                // Using existing card structure from XML roughly
                View view = inflater.inflate(R.layout.item_expense_row, llList, false); // Reuse expense row for now
                TextView tvTitle = view.findViewById(R.id.tv_expense_title);
                TextView tvDesc = view.findViewById(R.id.tv_expense_category);

                tvTitle.setText(text);
                tvDesc.setText(type);

                llList.addView(view);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
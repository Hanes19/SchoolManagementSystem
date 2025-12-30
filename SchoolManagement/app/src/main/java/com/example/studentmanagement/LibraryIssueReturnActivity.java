package com.example.studentmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LibraryIssueReturnActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private boolean isIssueMode = true;

    private TextView tabIssue, tabReturn;
    private EditText etIsbn, etStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_issue_return);

        db = new DatabaseHelper(this);

        // Bind Views
        tabIssue = findViewById(R.id.tab_issue);
        tabReturn = findViewById(R.id.tab_return);
        etIsbn = findViewById(R.id.et_isbn);
        etStudentId = findViewById(R.id.et_student_id);
        CardView btnSubmit = findViewById(R.id.btn_submit_issue);
        View btnClose = findViewById(R.id.btn_close);

        // Listeners
        btnClose.setOnClickListener(v -> finish());

        tabIssue.setOnClickListener(v -> setMode(true));
        tabReturn.setOnClickListener(v -> setMode(false));

        btnSubmit.setOnClickListener(v -> processTransaction());
    }

    private void setMode(boolean issue) {
        isIssueMode = issue;
        if (issue) {
            tabIssue.setBackgroundResource(R.drawable.rounded_corner_white_bg); // Needs a white shape or similar
            tabIssue.getBackground().setTint(Color.parseColor("#111C44"));
            tabIssue.setTextColor(Color.WHITE);

            tabReturn.setBackground(null);
            tabReturn.setTextColor(Color.parseColor("#A3AED0"));
        } else {
            tabReturn.setBackgroundResource(R.drawable.rounded_corner_white_bg);
            tabReturn.getBackground().setTint(Color.parseColor("#111C44"));
            tabReturn.setTextColor(Color.WHITE);

            tabIssue.setBackground(null);
            tabIssue.setTextColor(Color.parseColor("#A3AED0"));
        }
    }

    private void processTransaction() {
        String isbn = etIsbn.getText().toString().trim();
        String studentId = etStudentId.getText().toString().trim();

        if (TextUtils.isEmpty(isbn) || TextUtils.isEmpty(studentId)) {
            Toast.makeText(this, "Please enter ISBN and Student ID", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isIssueMode) {
            boolean success = db.issueBook(isbn, studentId);
            if (success) {
                Toast.makeText(this, "Book Issued Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed: Book Unavailable or Invalid ISBN", Toast.LENGTH_LONG).show();
            }
        } else {
            boolean success = db.returnBook(isbn, studentId);
            if (success) {
                Toast.makeText(this, "Book Returned Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed: No Active Issue Found", Toast.LENGTH_LONG).show();
            }
        }
    }
}
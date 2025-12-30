package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LibraryActiveIssuesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llActiveList;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_active_issues);

        db = new DatabaseHelper(this);
        llActiveList = findViewById(R.id.ll_active_list);
        etSearch = findViewById(R.id.et_search);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        etSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadActiveIssues(s.toString());
            }
        });

        loadActiveIssues("");
    }

    private void loadActiveIssues(String query) {
        llActiveList.removeAllViews();
        Cursor cursor = db.getActiveLibraryIssues();

        LayoutInflater inflater = LayoutInflater.from(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date today = new Date();

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String studentId = cursor.getString(cursor.getColumnIndexOrThrow("student_id"));
                String dueDateStr = cursor.getString(cursor.getColumnIndexOrThrow("due_date"));

                // Filter logic
                if (!query.isEmpty() && !title.toLowerCase().contains(query.toLowerCase())
                        && !studentId.toLowerCase().contains(query.toLowerCase())) {
                    continue;
                }

                View itemView = inflater.inflate(R.layout.item_active_issue, llActiveList, false);
                TextView tvName = itemView.findViewById(R.id.tv_student_name);
                TextView tvDetails = itemView.findViewById(R.id.tv_issue_date);
                TextView tvBook = itemView.findViewById(R.id.tv_book_title);
                TextView tvStatus = itemView.findViewById(R.id.tv_due_status);

                tvName.setText(studentId); // Or lookup student name if you have Users table
                tvBook.setText(title);
                tvDetails.setText("Due: " + dueDateStr);

                // Calculate Days Left
                try {
                    Date dueDate = sdf.parse(dueDateStr);
                    long diff = dueDate.getTime() - today.getTime();
                    long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                    if (days < 0) {
                        tvStatus.setText("OVERDUE " + Math.abs(days) + "D");
                        tvStatus.setTextColor(Color.parseColor("#F44336")); // Red
                        tvStatus.setBackgroundColor(Color.parseColor("#FFEBEE"));
                    } else {
                        tvStatus.setText("DUE IN " + days + "D");
                        tvStatus.setTextColor(Color.parseColor("#4318FF")); // Blue
                        tvStatus.setBackgroundColor(Color.parseColor("#F4F7FE"));
                    }
                } catch (Exception e) {
                    tvStatus.setText("-");
                }

                llActiveList.addView(itemView);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
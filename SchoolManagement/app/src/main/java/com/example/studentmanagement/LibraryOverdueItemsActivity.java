package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LibraryOverdueItemsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llOverdueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_overdue_item);

        db = new DatabaseHelper(this);
        llOverdueList = findViewById(R.id.ll_overdue_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadOverdueItems();
    }

    private void loadOverdueItems() {
        llOverdueList.removeAllViews();
        Cursor cursor = db.getOverdueStudents(); // Reusing the method from previous step

        LayoutInflater inflater = LayoutInflater.from(this);
        double fineRate = 5.00;

        if (cursor.moveToFirst()) {
            do {
                // Note: Ensure getOverdueStudents returns 'full_name' or use 'student_id' if name not joined
                // Adjust column name below based on your DatabaseHelper implementation
                String name = "";
                try {
                    name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                } catch (Exception e) {
                    name = cursor.getString(cursor.getColumnIndexOrThrow("student_id"));
                }

                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int daysLate = cursor.getInt(cursor.getColumnIndexOrThrow("days_overdue"));

                View itemView = inflater.inflate(R.layout.item_overdue_alert, llOverdueList, false);
                TextView tvName = itemView.findViewById(R.id.tv_student_name);
                TextView tvDays = itemView.findViewById(R.id.tv_overdue_days);
                TextView tvBook = itemView.findViewById(R.id.tv_book_title);

                tvName.setText(name);
                tvBook.setText(title);

                double fine = daysLate * fineRate;
                tvDays.setText(daysLate + " Days Overdue • Fine: $" + String.format("%.2f", fine));

                llOverdueList.addView(itemView);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
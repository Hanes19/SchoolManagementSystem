package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LibraryLateFeesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llFineList;
    private TextView tvTotalOutstanding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_late_fees);

        db = new DatabaseHelper(this);
        llFineList = findViewById(R.id.ll_fine_list);
        tvTotalOutstanding = findViewById(R.id.tv_total_outstanding);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadFines();
    }

    private void loadFines() {
        llFineList.removeAllViews();
        Cursor cursor = db.getOverdueStudents();

        double totalFine = 0;
        double fineRatePerDay = 5.00;

        if (cursor.moveToFirst()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            do {
                String studentName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int daysOverdue = cursor.getInt(cursor.getColumnIndexOrThrow("days_overdue"));

                if (daysOverdue > 0) {
                    double fine = daysOverdue * fineRatePerDay;
                    totalFine += fine;

                    View itemView = inflater.inflate(R.layout.item_library_fine, llFineList, false);
                    TextView tvName = itemView.findViewById(R.id.tv_student_name);
                    TextView tvDetails = itemView.findViewById(R.id.tv_fine_details);
                    TextView tvAmount = itemView.findViewById(R.id.tv_fine_amount);

                    tvName.setText(studentName);
                    tvDetails.setText(bookTitle + " • " + daysOverdue + " days late");
                    tvAmount.setText(String.format("$%.2f", fine));

                    llFineList.addView(itemView);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        tvTotalOutstanding.setText(String.format("Total Outstanding: $%.2f", totalFine));
    }
}
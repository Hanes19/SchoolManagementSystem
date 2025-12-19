package com.example.studentmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class StudentFeesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_fees);

        studentId = getIntent().getStringExtra("STUDENT_ID");
        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.btn_back_fees).setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_fee_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFees();
    }

    private void loadFees() {
        List<FeeItem> items = new ArrayList<>();
        Cursor cursor = dbHelper.getStudentFees(studentId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double amt = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                items.add(new FeeItem(desc, amt, type, date));
            } while (cursor.moveToNext());
            cursor.close();
        }

        recyclerView.setAdapter(new FeeAdapter(items));
    }

    private static class FeeItem {
        String desc, type, date; double amount;
        FeeItem(String d, double a, String t, String dt) { desc = d; amount = a; type = t; date = dt; }
    }

    private class FeeAdapter extends RecyclerView.Adapter<FeeAdapter.ViewHolder> {
        List<FeeItem> list;
        FeeAdapter(List<FeeItem> list) { this.list = list; }

        @NonNull @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FeeItem item = list.get(position);
            holder.text1.setText(item.desc);

            if(item.type.equals("Payment")) {
                holder.text2.setText("- $ " + item.amount + " (" + item.date + ")");
                holder.text2.setTextColor(Color.parseColor("#4CAF50")); // Green
            } else {
                holder.text2.setText("+ $ " + item.amount + " (" + item.date + ")");
                holder.text2.setTextColor(Color.parseColor("#F44336")); // Red
            }
        }

        @Override
        public int getItemCount() { return list.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1, text2;
            ViewHolder(View v) { super(v); text1 = v.findViewById(android.R.id.text1); text2 = v.findViewById(android.R.id.text2); }
        }
    }
}
package com.example.studentmanagement;

import android.database.Cursor;
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

public class StudentGradesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_grades);

        studentId = getIntent().getStringExtra("STUDENT_ID");
        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.btn_back_grades).setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_grades);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadGrades();
    }

    private void loadGrades() {
        List<GradeItem> items = new ArrayList<>();
        Cursor cursor = dbHelper.getStudentGrades(studentId, "All"); // Fetch all for now

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String sub = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String grd = cursor.getString(cursor.getColumnIndexOrThrow("grade"));
                items.add(new GradeItem(sub, grd));
            } while (cursor.moveToNext());
            cursor.close();
        }

        recyclerView.setAdapter(new GradesAdapter(items));
    }

    private static class GradeItem {
        String subject, grade;
        GradeItem(String s, String g) { subject = s; grade = g; }
    }

    private class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.ViewHolder> {
        List<GradeItem> list;
        GradesAdapter(List<GradeItem> list) { this.list = list; }

        @NonNull @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            GradeItem item = list.get(position);
            holder.text.setText(item.subject + " : " + item.grade);
        }

        @Override
        public int getItemCount() { return list.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            ViewHolder(View v) { super(v); text = v.findViewById(android.R.id.text1); }
        }
    }
}   
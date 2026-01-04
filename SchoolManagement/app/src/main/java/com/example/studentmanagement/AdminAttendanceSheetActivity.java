package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AdminAttendanceSheetActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView rvStudents;
    private TextView tvDateHeader, tvClassHeader;
    private String selectedDate, className;
    private SheetAdapter adapter;
    private List<SheetModel> studentList;

    // Helper model for this sheet
    private static class SheetModel {
        String studentId, name, status; // status: Present, Absent, Late
        public SheetModel(String id, String n, String s) { studentId=id; name=n; status=s; }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_attendance_sheet); // Using your refined layout

        db = new DatabaseHelper(this);
        selectedDate = getIntent().getStringExtra("DATE");
        className = getIntent().getStringExtra("CLASS_NAME");

        if (selectedDate == null) selectedDate = "2025-01-01"; // Default
        if (className == null) className = "Grade 10-Emerald";

        tvDateHeader = findViewById(R.id.tv_date);
        tvClassHeader = findViewById(R.id.tv_class_name); // You'll need to add this ID to layout
        rvStudents = findViewById(R.id.rv_attendance_sheet); // Need to add RecyclerView to layout

        tvDateHeader.setText(selectedDate);
        if(tvClassHeader != null) tvClassHeader.setText(className);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_save).setOnClickListener(v -> saveAttendance());

        loadSheetData();
    }

    private void loadSheetData() {
        studentList = new ArrayList<>();
        // In a real scenario, you would query: "Get all students in Class X"
        // AND "Left Join Attendance on Date Y" to get their status.
        // For simplicity, I'll reuse getAllStudentsWithClassDetails and filter or fetch status manually.

        // 1. Get all students (simplified for demo)
        Cursor cursor = db.getAllStudentsWithClassDetails();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                // String grade = cursor.getString(4); // Filter by class if needed

                // 2. Fetch status for this student on this date
                String status = "Present"; // Default
                Cursor attCursor = db.getReadableDatabase().rawQuery(
                        "SELECT status FROM attendance WHERE student_id=? AND date=?",
                        new String[]{id, selectedDate});
                if (attCursor.moveToFirst()) {
                    status = attCursor.getString(0);
                }
                attCursor.close();

                studentList.add(new SheetModel(id, name, status));
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new SheetAdapter(studentList);
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        rvStudents.setAdapter(adapter);
    }

    private void saveAttendance() {
        // Iterate list and update DB
        for (SheetModel student : studentList) {
            // Update logic: Check if record exists, update it. If not, insert.
            // Simplified: Delete old record for that day and insert new (or use REPLACE INTO)
            db.getWritableDatabase().execSQL("DELETE FROM attendance WHERE student_id=? AND date=?",
                    new String[]{student.studentId, selectedDate});

            db.getWritableDatabase().execSQL("INSERT INTO attendance (student_id, date, status, class_name) VALUES (?, ?, ?, ?)",
                    new String[]{student.studentId, selectedDate, student.status, className});
        }
        Toast.makeText(this, "Attendance Saved Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // --- Internal Adapter ---
    private class SheetAdapter extends RecyclerView.Adapter<SheetAdapter.Holder> {
        List<SheetModel> list;
        public SheetAdapter(List<SheetModel> list) { this.list = list; }

        @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p, int t) {
            View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_attendance_sheet_row, p, false);
            return new Holder(v);
        }

        @Override public void onBindViewHolder(@NonNull Holder h, int pos) {
            SheetModel m = list.get(pos);
            h.tvName.setText(m.name);

            h.rgStatus.setOnCheckedChangeListener(null); // Prevent trigger during bind
            if ("Present".equalsIgnoreCase(m.status)) h.rbP.setChecked(true);
            else if ("Absent".equalsIgnoreCase(m.status)) h.rbA.setChecked(true);
            else h.rbL.setChecked(true);

            h.rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rb_p) m.status = "Present";
                else if (checkedId == R.id.rb_a) m.status = "Absent";
                else m.status = "Late";
            });
        }

        @Override public int getItemCount() { return list.size(); }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvName;
            RadioGroup rgStatus;
            RadioButton rbP, rbA, rbL;
            public Holder(View v) {
                super(v);
                tvName = v.findViewById(R.id.tv_student_name);
                rgStatus = v.findViewById(R.id.rg_status);
                rbP = v.findViewById(R.id.rb_p);
                rbA = v.findViewById(R.id.rb_a);
                rbL = v.findViewById(R.id.rb_l);
            }
        }
    }
}
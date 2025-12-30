package com.example.studentmanagement;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GradebookAdapter extends RecyclerView.Adapter<GradebookAdapter.ViewHolder> {

    private List<StudentGradeModel> students;

    public GradebookAdapter(List<StudentGradeModel> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_item_gradebook, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentGradeModel student = students.get(position);
        holder.tvName.setText(student.getName());
        holder.tvId.setText("ID: " + student.getId());

        if (student.getCurrentScore() != -1) {
            holder.etGrade.setText(String.valueOf(student.getCurrentScore()));
        } else {
            holder.etGrade.setText("");
        }

        // Remove previous listener to avoid recycling issues
        if (holder.textWatcher != null) {
            holder.etGrade.removeTextChangedListener(holder.textWatcher);
        }

        holder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    try {
                        int score = Integer.parseInt(s.toString());
                        student.setCurrentScore(score);
                    } catch (NumberFormatException e) {
                        // invalid input
                    }
                }
            }
        };
        holder.etGrade.addTextChangedListener(holder.textWatcher);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId;
        EditText etGrade;
        TextWatcher textWatcher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_student_name);
            tvId = itemView.findViewById(R.id.tv_student_id);
            etGrade = itemView.findViewById(R.id.et_grade_input);
        }
    }
}
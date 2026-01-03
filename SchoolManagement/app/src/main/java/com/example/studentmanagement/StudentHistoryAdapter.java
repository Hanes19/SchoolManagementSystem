package com.example.studentmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentHistoryAdapter extends RecyclerView.Adapter<StudentHistoryAdapter.ViewHolder> {

    private List<AttendanceModel> list;

    public StudentHistoryAdapter(List<AttendanceModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // You can reuse 'item_expense_row' if it has 3 textviews, or create a simple layout 'item_attendance_history'
        // For now, let's try to reuse a simple built-in layout or your existing row
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceModel model = list.get(position);
        // Assuming your AttendanceModel has getters for Date and Status
        // You might need to adjust these getters based on your actual model
        holder.tvDate.setText(model.getDate());
        holder.tvStatus.setText(model.getStatus());

        // Simple color coding
        if("Present".equalsIgnoreCase(model.getStatus())) {
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#4CAF50")); // Green
        } else if("Absent".equalsIgnoreCase(model.getStatus())) {
            holder.tvStatus.setTextColor(android.graphics.Color.RED);
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvStatus;

        public ViewHolder(View v) {
            super(v);
            // Link these IDs to whatever is in your item layout (e.g., item_expense_row.xml)
            tvDate = v.findViewById(R.id.tv_expense_title);   // Reusing title for Date
            tvStatus = v.findViewById(R.id.tv_expense_amount); // Reusing amount for Status
        }
    }
}
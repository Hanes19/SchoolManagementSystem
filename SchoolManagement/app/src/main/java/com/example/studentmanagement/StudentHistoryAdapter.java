package com.example.studentmanagement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentHistoryAdapter extends RecyclerView.Adapter<StudentHistoryAdapter.ViewHolder> {

    private List<AttendanceModel> list;
    private Context context;
    // Assume we pass the student's class name or we can fetch it,
    // but for now let's assume the Activity handles the context better.

    public StudentHistoryAdapter(Context context, List<AttendanceModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Using a custom layout for better control
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_history_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceModel model = list.get(position);
        holder.tvDate.setText(model.getDate());
        holder.tvStatus.setText(model.getStatus());

        if("Present".equalsIgnoreCase(model.getStatus())) {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
        } else if("Absent".equalsIgnoreCase(model.getStatus())) {
            holder.tvStatus.setTextColor(Color.RED);
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800")); // Orange/Late
        }

        // Edit Button Action
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminAttendanceSheetActivity.class);
            intent.putExtra("DATE", model.getDate());
            // In a real app, you'd pass the Class ID too.
            // For now, we'll let the Sheet Activity infer or default it,
            // or we could pass it if available in the model.
            intent.putExtra("CLASS_NAME", "Grade 10-Emerald"); // Example default or fetch from model
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvStatus;
        ImageView btnEdit;

        public ViewHolder(View v) {
            super(v);
            tvDate = v.findViewById(R.id.tv_att_date);
            tvStatus = v.findViewById(R.id.tv_att_status);
            btnEdit = v.findViewById(R.id.btn_edit_att);
        }
    }
}
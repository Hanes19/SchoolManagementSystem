package com.example.studentmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private List<AttendanceModel> list;

    public AttendanceAdapter(List<AttendanceModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_attendance_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceModel model = list.get(position);
        holder.tvName.setText(model.getName());
        holder.tvId.setText("ID: " + model.getId());

        // Prevent infinite loops with listeners
        holder.rgStatus.setOnCheckedChangeListener(null);

        if ("Present".equals(model.getStatus())) holder.rbPresent.setChecked(true);
        else if ("Absent".equals(model.getStatus())) holder.rbAbsent.setChecked(true);
        else if ("Late".equals(model.getStatus())) holder.rbLate.setChecked(true);

        holder.rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_present) model.setStatus("Present");
            else if (checkedId == R.id.rb_absent) model.setStatus("Absent");
            else if (checkedId == R.id.rb_late) model.setStatus("Late");
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId;
        RadioGroup rgStatus;
        RadioButton rbPresent, rbAbsent, rbLate;

        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_student_name);
            tvId = v.findViewById(R.id.tv_student_id);
            rgStatus = v.findViewById(R.id.rg_status);
            rbPresent = v.findViewById(R.id.rb_present);
            rbAbsent = v.findViewById(R.id.rb_absent);
            rbLate = v.findViewById(R.id.rb_late);
        }
    }
}

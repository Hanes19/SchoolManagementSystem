package com.example.studentmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {

    private List<Assignment> assignmentList;

    public AssignmentAdapter(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your specific layout "item_assignment_row.xml"
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assignment_row, parent, false);
        return new AssignmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
        Assignment assignment = assignmentList.get(position);

        // Bind data to views
        holder.tvTitle.setText(assignment.getTitle());

        // Format: "Due: Date • Class"
        String details = "Due: " + assignment.getDueDate() + " • " + assignment.getSubject();
        holder.tvDetails.setText(details);

        // Format: "Max Points: 100"
        holder.tvScore.setText("Max Points: " + assignment.getMaxPoints());

        // Optional: Logic to hide/show the "OPEN" badge could go here
        // if (!assignment.isOpen()) { holder.badgeStatus.setVisibility(View.GONE); }
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    // ViewHolder class to hold view references
    public static class AssignmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDetails, tvScore;

        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            // Match these IDs with your XML file
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDetails = itemView.findViewById(R.id.tv_details);
            tvScore = itemView.findViewById(R.id.tv_score);
        }
    }
}
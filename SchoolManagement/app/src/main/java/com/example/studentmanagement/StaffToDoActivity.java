package com.example.studentmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StaffToDoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to your provided XML file
        setContentView(R.layout.staff_to_do_list_details);

        // Initialize the "Mark as Completed" button
        // In your XML, this is a CardView with ID 'btn_complete_task'
        CardView btnComplete = findViewById(R.id.btn_complete_task);

        if (btnComplete != null) {
            btnComplete.setOnClickListener(v -> markTaskAsComplete());
        }

        // Optional: Handle the Drag Handle if you want it to close the activity (like a bottom sheet)
        View dragHandle = findViewById(R.id.drag_handle);
        if (dragHandle != null) {
            dragHandle.setOnClickListener(v -> finish());
        }
    }

    private void markTaskAsComplete() {
        // Logic to mark the specific task as done
        // For now, we just show a toast and close the screen
        Toast.makeText(this, "Task Marked as Completed!", Toast.LENGTH_SHORT).show();

        // Add database update logic here if needed, e.g.:
        // db.updateTaskStatus(taskId, "Completed");

        finish(); // Close the details screen
    }
}
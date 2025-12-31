package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherMessageActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llMessageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_message);

        db = new DatabaseHelper(this);
        llMessageContainer = findViewById(R.id.ll_message_container);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.fab_new_message).setOnClickListener(v -> {
            // For now, let's simulate sending a message to a parent
            db.sendMessage("teach01", "parent01", "Mr. Langdon", "Homework Update", "Just a reminder about the homework due.");
            Toast.makeText(this, "Test Message Sent!", Toast.LENGTH_SHORT).show();
            loadMessages(); // Refresh
        });

        loadMessages();
    }

    private void loadMessages() {
        llMessageContainer.removeAllViews();
        // Assuming "teach01" is the current logged-in teacher
        Cursor cursor = db.getMessagesForUser("teach01");

        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String sender = cursor.getString(cursor.getColumnIndexOrThrow("sender_name"));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                View itemView = inflater.inflate(R.layout.item_message_row, llMessageContainer, false);

                // FIXED: IDs matched to item_message_row.xml
                TextView tvSender = itemView.findViewById(R.id.tv_sender_name);
                TextView tvPreview = itemView.findViewById(R.id.tv_message_preview);
                TextView tvTime = itemView.findViewById(R.id.tv_timestamp); // Changed from tv_time

                // FIXED: Removed tv_message_subject logic
                tvSender.setText(sender);
                tvPreview.setText(subject + ": " + body); // Combine Subject and Body

                // Extract just time or date for brevity
                try {
                    if (timestamp.length() > 11) {
                        tvTime.setText(timestamp.substring(11)); // "HH:mm" part
                    } else {
                        tvTime.setText(timestamp);
                    }
                } catch (Exception e) {
                    tvTime.setText(timestamp);
                }

                // Optional: Hide action buttons if not needed for this view,
                // or add listeners like in ParentMessageActivity
                ImageView btnEmail = itemView.findViewById(R.id.btn_action_email);
                ImageView btnCall = itemView.findViewById(R.id.btn_action_call);

                btnEmail.setOnClickListener(v -> Toast.makeText(this, "Reply to " + sender, Toast.LENGTH_SHORT).show());
                btnCall.setOnClickListener(v -> Toast.makeText(this, "Call " + sender, Toast.LENGTH_SHORT).show());

                llMessageContainer.addView(itemView);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
package com.example.studentmanagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ParentMessageActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llList;
    private String parentId = "PARENT-001"; // Mock ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_message_dashboard);

        db = new DatabaseHelper(this);

        // Find the container we added ID to
        llList = findViewById(R.id.ll_message_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Removed FAB listener because the design uses buttons on the rows instead

        loadMessages();
    }

    private void showComposeDialog(String recipientName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message " + (recipientName != null ? recipientName : "Teacher"));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etSubject = new EditText(this);
        etSubject.setHint("Subject");
        layout.addView(etSubject);

        final EditText etBody = new EditText(this);
        etBody.setHint("Message...");
        etBody.setHeight(200);
        layout.addView(etBody);

        builder.setView(layout);

        builder.setPositiveButton("Send", (dialog, which) -> {
            String sub = etSubject.getText().toString();
            String body = etBody.getText().toString();
            if (!sub.isEmpty() && !body.isEmpty()) {
                // Sending to a default Admin/Teacher for demo
                db.sendMessage(parentId, "ADMIN-001", "Mrs. Parent", sub, body);
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
                loadMessages();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void loadMessages() {
        llList.removeAllViews();
        Cursor cursor = db.getMessagesForUser(parentId);
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String sender = cursor.getString(cursor.getColumnIndexOrThrow("sender_name"));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                // Inflate the new row design
                View view = inflater.inflate(R.layout.item_message_row, llList, false);

                TextView tvSender = view.findViewById(R.id.tv_sender_name);
                TextView tvPreview = view.findViewById(R.id.tv_message_preview);
                TextView tvTime = view.findViewById(R.id.tv_timestamp);

                // Action Buttons
                ImageView btnEmail = view.findViewById(R.id.btn_action_email);
                ImageView btnCall = view.findViewById(R.id.btn_action_call);

                tvSender.setText(sender);
                tvPreview.setText(subject + ": " + body); // Show subject + preview
                tvTime.setText(time);

                // Add Divider
                View divider = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 3); // 1dp height
                params.setMargins(0, 10, 0, 10);
                divider.setLayoutParams(params);
                divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

                // FUNCTIONALITY: Email Button opens Compose Dialog
                btnEmail.setOnClickListener(v -> showComposeDialog(sender));

                // FUNCTIONALITY: Call Button (Mock Dialer)
                btnCall.setOnClickListener(v -> {
                    Toast.makeText(this, "Calling " + sender + "...", Toast.LENGTH_SHORT).show();
                    // Intent intent = new Intent(Intent.ACTION_DIAL);
                    // intent.setData(Uri.parse("tel:1234567890"));
                    // startActivity(intent);
                });

                llList.addView(view);

                // Optional: Add a light divider between rows to match design style
                // llList.addView(divider);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
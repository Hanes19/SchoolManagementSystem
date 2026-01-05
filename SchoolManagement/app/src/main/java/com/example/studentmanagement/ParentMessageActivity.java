package com.example.studentmanagement;

import android.app.AlertDialog;
import android.database.Cursor;
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
    private String parentId = "PARENT-001"; // In real app, get from Session

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_message_dashboard);

        db = new DatabaseHelper(this);
        llList = findViewById(R.id.ll_message_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        loadMessages();
    }

    private void loadMessages() {
        llList.removeAllViews();
        Cursor cursor = db.getMessagesForUser(parentId);
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String sender = cursor.getString(cursor.getColumnIndexOrThrow("sender_name"));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                View row = inflater.inflate(R.layout.item_message_row, llList, false);

                TextView tvSender = row.findViewById(R.id.tv_sender_name);
                TextView tvPreview = row.findViewById(R.id.tv_message_preview);
                TextView tvTime = row.findViewById(R.id.tv_timestamp);
                ImageView btnReply = row.findViewById(R.id.btn_action_email);

                tvSender.setText(sender);
                tvPreview.setText(subject + ": " + body);
                tvTime.setText(time);

                // Logic: Reply Button
                btnReply.setOnClickListener(v -> showComposeDialog(sender));

                llList.addView(row);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void showComposeDialog(String recipientName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reply to " + recipientName);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etSubject = new EditText(this);
        etSubject.setHint("Subject");
        layout.addView(etSubject);

        final EditText etBody = new EditText(this);
        etBody.setHint("Write your message here...");
        etBody.setMinLines(3);
        layout.addView(etBody);

        builder.setView(layout);

        builder.setPositiveButton("Send", (dialog, which) -> {
            String sub = etSubject.getText().toString();
            String msg = etBody.getText().toString();
            if (!sub.isEmpty() && !msg.isEmpty()) {
                db.sendMessage(parentId, "ADMIN", "Parent", sub, msg);
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
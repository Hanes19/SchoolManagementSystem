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
        llList = findViewById(R.id.ll_message_list);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.fab_compose_message).setOnClickListener(v -> showComposeDialog());

        loadMessages();
    }

    private void showComposeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact Teacher/Admin");

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

                // Reuse a message row layout
                View view = inflater.inflate(R.layout.item_message_row, llList, false);
                TextView tvSender = view.findViewById(R.id.tv_sender_name);
                TextView tvSubject = view.findViewById(R.id.tv_message_subject);
                TextView tvPreview = view.findViewById(R.id.tv_message_preview);
                TextView tvTime = view.findViewById(R.id.tv_time);

                tvSender.setText(sender);
                tvSubject.setText(subject);
                tvPreview.setText(body);
                tvTime.setText(time);

                llList.addView(view);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
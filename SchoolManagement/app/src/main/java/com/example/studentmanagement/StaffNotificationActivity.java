package com.example.studentmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class StaffNotificationActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private ListView lvNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_notification);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        lvNotifications = findViewById(R.id.lv_notifications); // Ensure this ID is in staff_notification.xml

        // Back Button
        if (findViewById(R.id.btn_back) != null) {
            findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        }

        loadNotifications();
    }

    private void loadNotifications() {
        // Fetch messages/notifications for the logged-in staff
        Cursor cursor = db.getMessagesForUser(session.getUserId());

        if (cursor != null) {
            String[] from = new String[]{"subject", "message_body", "timestamp"};
            int[] to = new int[]{R.id.tv_notif_title, R.id.tv_notif_body, R.id.tv_notif_time}; // IDs from item_notification.xml

            // Using SimpleCursorAdapter for simplicity, or create a custom one like in StaffLeaveActivity
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.item_message_row, // Reusing existing row layout
                    cursor,
                    from,
                    to,
                    0
            );
            lvNotifications.setAdapter(adapter);
        }
    }
}
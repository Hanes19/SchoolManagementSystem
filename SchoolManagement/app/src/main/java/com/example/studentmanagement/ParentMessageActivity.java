package com.example.studentmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ParentMessageActivity extends AppCompatActivity {

    private ListView lvMessages;
    private List<String[]> messages; // [Sender, Preview, Time]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_message_dashboard);

        lvMessages = findViewById(R.id.lv_messages);

        // Dummy Data
        messages = new ArrayList<>();
        messages.add(new String[]{"Mr. Snape", "Your son missed Potions class.", "10:30 AM"});
        messages.add(new String[]{"Mrs. McGonagall", "Excellent performance in Transfiguration.", "Yesterday"});

        MessageAdapter adapter = new MessageAdapter();
        lvMessages.setAdapter(adapter);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private class MessageAdapter extends ArrayAdapter<String[]> {
        public MessageAdapter() {
            super(ParentMessageActivity.this, R.layout.item_message_row, messages);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                row = LayoutInflater.from(getContext()).inflate(R.layout.item_message_row, parent, false);
            }

            String[] msg = getItem(position);

            // FIX: Updated IDs to match item_message_row.xml
            TextView tvSender = row.findViewById(R.id.tv_notif_title);   // Was tv_sender_name
            TextView tvPreview = row.findViewById(R.id.tv_notif_body);   // Was tv_message_preview
            TextView tvTime = row.findViewById(R.id.tv_notif_time);      // Was tv_timestamp

            // Note: The 'reply' button (btn_action_email) might not exist in the current item_message_row.xml
            // If it doesn't, we skip it to prevent crash.
            // If you need it, you must add it to the XML. For now, I'll remove the reference to avoid build error.

            if (msg != null) {
                tvSender.setText(msg[0]);
                tvPreview.setText(msg[1]);
                tvTime.setText(msg[2]);
            }

            return row;
        }
    }
}
package com.example.studentmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TeacherMessageActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private MessageAdapter adapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_message);

        rvMessages = findViewById(R.id.rv_messages);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));

        messageList = new ArrayList<>();
        messageList.add(new Message("Mrs. Weasley", "Harry is sick today.", "08:30 AM"));
        messageList.add(new Message("Lucius Malfoy", "Regarding Draco's grades...", "Yesterday"));

        adapter = new MessageAdapter(messageList);
        rvMessages.setAdapter(adapter);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    // Simple Model Class
    private static class Message {
        String sender, text, time;
        Message(String s, String t, String tm) { sender = s; text = t; time = tm; }
    }

    // Adapter Class
    private class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        private List<Message> list;

        MessageAdapter(List<Message> list) { this.list = list; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Message msg = list.get(position);
            holder.tvSender.setText(msg.sender);
            holder.tvPreview.setText(msg.text);
            holder.tvTime.setText(msg.time);
        }

        @Override
        public int getItemCount() { return list.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvSender, tvPreview, tvTime;

            ViewHolder(View itemView) {
                super(itemView);
                // FIX: Updated IDs to match item_message_row.xml
                tvSender = itemView.findViewById(R.id.tv_notif_title); // Was tv_sender_name
                tvPreview = itemView.findViewById(R.id.tv_notif_body); // Was tv_message_preview
                tvTime = itemView.findViewById(R.id.tv_notif_time);    // Was tv_timestamp
            }
        }
    }
}
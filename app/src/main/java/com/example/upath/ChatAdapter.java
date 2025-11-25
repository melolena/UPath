package com.example.upath; // Certifique-se que este pacote é o mesmo do seu ActivityTeste

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private List<Message> messageList;

    public ChatAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.isSentByUser()) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageHolder) holder).bind(message);
        } else {
            ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // --- Classes Internas (ViewHolders) ---
    static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        SentMessageHolder(View itemView) {
            super(itemView);
            // Certifique-se de que o ID no XML item_message_sent é @+id/text_message_body
            messageText = itemView.findViewById(R.id.text_message_body);
        }
        void bind(Message message) { messageText.setText(message.getText()); }
    }

    static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        ReceivedMessageHolder(View itemView) {
            super(itemView);
            // Certifique-se de que o ID no XML item_message_received é @+id/text_message_body
            messageText = itemView.findViewById(R.id.text_message_body);
        }
        void bind(Message message) { messageText.setText(message.getText()); }
    }
}
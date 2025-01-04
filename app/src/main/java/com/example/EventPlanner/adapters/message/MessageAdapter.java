package com.example.EventPlanner.adapters.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.model.messages.MessageDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<MessageDTO> messages = new ArrayList<>();
    private final int currentUserId;

    public MessageAdapter(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        MessageDTO message = messages.get(position);
        return message.getSenderId() == currentUserId ? 1 : 0;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageDTO message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(List<MessageDTO> newMessages) {
        messages = newMessages;
        notifyDataSetChanged();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageBubble;
        private final TextView messageTime;
        private final ConstraintLayout messageContainer;

        MessageViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            messageBubble = itemView.findViewById(R.id.message_bubble);
            messageTime = itemView.findViewById(R.id.message_time);
            messageContainer = itemView.findViewById(R.id.message_container);

            // Setup constraints based on message type
            ConstraintLayout parentLayout = (ConstraintLayout) itemView;
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(parentLayout);

            if (viewType == 1) { // Sent message
                // Align container to end (right)
                constraintSet.clear(R.id.message_container, ConstraintSet.START);
                constraintSet.connect(R.id.message_container, ConstraintSet.END,
                        ConstraintSet.PARENT_ID, ConstraintSet.END);

                // Set sent message background
                messageBubble.setBackgroundResource(R.drawable.message_bubble_sent_background);

                // Align time to end
                ConstraintSet containerSet = new ConstraintSet();
                containerSet.clone((ConstraintLayout) messageContainer);
                containerSet.clear(R.id.message_time, ConstraintSet.START);
                containerSet.connect(R.id.message_time, ConstraintSet.END,
                        R.id.message_bubble, ConstraintSet.END);
                containerSet.applyTo(messageContainer);
            }

            constraintSet.applyTo(parentLayout);
        }

        void bind(MessageDTO message) {
            messageBubble.setText(message.getContent());
            if (message.getSentTime() != null) {
                // Format the date as per your requirement (e.g., "MM/dd/yyyy")
                String inputDate = message.getSentTime();

                // Parse the input string to LocalDateTime
                LocalDateTime dateTime = LocalDateTime.parse(inputDate);

                // Define the desired output format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                // Format the date to the desired string
                String formattedDate = dateTime.format(formatter);
                messageTime.setText("");
            }

        }

        private String formatDateTime(LocalDateTime dateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return dateTime.format(formatter);
        }
    }
}
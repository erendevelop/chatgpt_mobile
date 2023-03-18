package com.example.chatgpt_mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    List<Message> messageList;
    // getting the messages from list into messageList
    public MessageAdapter(List<Message> messageList){
        this.messageList = messageList;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, null);
        MessageViewHolder viewHolder = new MessageViewHolder(chatView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        if(message.getSentBy().equals(Message.SENT_BY_ME)){
            // setting the left message bubble invisible

            holder.leftChatView.setVisibility(View.GONE);
            // setting the right message bubble visible

            holder.rightChatView.setVisibility(View.VISIBLE);

            // adding message to textview
            holder.rightChatTextView.setText(message.getMessage());
        } else{
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftChatTextView.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatView, rightChatView;
        TextView leftChatTextView, rightChatTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView = itemView.findViewById(R.id.left_chat_view);
            rightChatView = itemView.findViewById(R.id.right_chat_view);

            leftChatTextView = itemView.findViewById(R.id.left_text_view);
            rightChatTextView = itemView.findViewById(R.id.right_text_view);
        }
    }
}

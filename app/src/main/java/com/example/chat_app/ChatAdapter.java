package com.example.chat_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.example.chat_app.databinding.LiRecievedMessageBinding;
import com.example.chat_app.databinding.LiSentMessageBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter
{
    private final Context context;
    private List<Message> messages;

    public ChatAdapter(Context context)
    {
        this.context = context;
    }

    public void setMessages(List<Message> messages)
    {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(viewType == 0)
        {
            LiSentMessageBinding binding = LiSentMessageBinding
                    .inflate(LayoutInflater.from(context), parent, false);
            return new SentMessageViewHolder(binding);
        }
        else
        {
            LiRecievedMessageBinding binding = LiRecievedMessageBinding
                    .inflate(LayoutInflater.from(context), parent, false);
            return new ReceivedMessageViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        int pos = holder.getAdapterPosition();

        if(holder.getItemViewType() == 0)
        {
            SentMessageViewHolder messageViewHolder = (SentMessageViewHolder) holder;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(messages.get(pos).getTimestamp());
            messageViewHolder.binding.tvMessageTime.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.getDefault()).format(calendar.getTime()));
            messageViewHolder.binding.tvMessage.setText(messages.get(pos).getText());
            Glide.with(context).load(messages.get(pos).getSender().getImg_url())
                    .into(messageViewHolder.binding.ivAvatar);

        }
        else
        {
            ReceivedMessageViewHolder messageViewHolder = (ReceivedMessageViewHolder) holder;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(messages.get(pos).getTimestamp());
            messageViewHolder.binding.tvMessageTime.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.getDefault()).format(calendar.getTime()));
            messageViewHolder.binding.tvMessage.setText(messages.get(pos).getText());
            Glide.with(context).load(messages.get(pos).getSender().getImg_url())
                    .into(messageViewHolder.binding.ivAvatar);

        }
    }

    @Override
    public int getItemCount()
    {
        if(messages == null)
            return 0;
        return messages.size();
    }


    @Override
    public int getItemViewType(int position)
    {
        Message message = messages.get(position);

        if(message.getSender().getId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return 0;
        }
        else
        {
            return 1;
        }

    }
}

class SentMessageViewHolder extends RecyclerView.ViewHolder
{
    LiSentMessageBinding binding;

    public SentMessageViewHolder(@NonNull LiSentMessageBinding binding)
    {
        super(binding.getRoot());
        this.binding = binding;
    }
}

class ReceivedMessageViewHolder extends RecyclerView.ViewHolder
{
    LiRecievedMessageBinding binding;

    public ReceivedMessageViewHolder(@NonNull LiRecievedMessageBinding binding)
    {
        super(binding.getRoot());
        this.binding = binding;
    }
}

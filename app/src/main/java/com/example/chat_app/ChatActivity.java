package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chat_app.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity
{

    User currentUser;
    ActivityChatBinding binding;
    ArrayList<Message> messages;

    ChatAdapter adapter;


    DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        currentUser = new User();
        currentUser.setId(FirebaseAuth.getInstance().getUid());
        currentUser.setName(SharedPreferenceManager.getInstance(this).getData("name"));
        currentUser.setImg_url(SharedPreferenceManager.getInstance(this).getData("photo"));

        chatRef = FirebaseDatabase.getInstance().getReference()
                .child("group")
                .child("messages");


        adapter = new ChatAdapter(this);
        binding.rvChat.setLayoutManager(new LinearLayoutManager(this));
        binding.rvChat.setHasFixedSize(true);
        binding.rvChat.setAdapter(adapter);


        binding.btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (binding.etMessage.getText().toString().isEmpty())
                {
                    Toast.makeText(ChatActivity.this, "Please type message", Toast.LENGTH_SHORT).show();
                    return;
                }

                String key = chatRef.push().getKey();

                Message message = new Message();
                message.setId(key);
                message.setText(binding.etMessage.getText().toString().trim());
                message.setTimestamp(Calendar.getInstance().getTimeInMillis());
                message.setSender(currentUser);

                assert key != null;
                chatRef.child(key).setValue(message);

                binding.etMessage.setText("");
            }
        });


        loadChatData();

    }


    private void loadChatData()
    {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading chat");
        dialog.show();

        FirebaseDatabase.getInstance().getReference()
                .child("group")
                .child("messages")
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        dialog.dismiss();
                        messages = new ArrayList<>();


                        if (snapshot.exists())
                        {
                            for (DataSnapshot messageSnap : snapshot.getChildren())
                            {
                                Message message = messageSnap.getValue(Message.class);
                                message.setId(messageSnap.getKey());
                                messages.add(message);
                            }

                            adapter.setMessages(messages);
                            adapter.notifyDataSetChanged();
                            binding.rvChat.scrollToPosition(messages.size() - 1);

                        } else
                        {
                            Toast.makeText(ChatActivity.this, "No Messages", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
    }
}
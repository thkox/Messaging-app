package eu.thkox.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.thkox.messaging_app.custom.tool.MessageAdapter;
import eu.thkox.messaging_app.data.model.Chat;
import eu.thkox.messaging_app.data.model.User;

public class ChatActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Toolbar toolbar;
    Intent intent;
    EditText messageText;
    RecyclerView recyclerView;
    FloatingActionButton sendButton;
    MessageAdapter messageAdapter;
    List<Chat> chatMessages;
    User receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageText = findViewById(R.id.editTextMessageText);
        sendButton = findViewById(R.id.floatingActionButtonSendMessage);

        toolbar = findViewById(R.id.app_toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set the layout of the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerViewChat);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        chatMessages = new ArrayList<>();

        intent = getIntent();
        String userid = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiver = snapshot.getValue(User.class);
                getSupportActionBar().setTitle(receiver.getNickname());
                //load the messages from the database
                loadMessagesFromDatabase(firebaseUser.getUid(), userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void sendMessageToDatabase(View view) {
        String message = messageText.getText().toString();
        int timestamp = (int) (System.currentTimeMillis() / 1000);

        if (!message.equals("")) {
            sendMessage(firebaseUser.getUid(), receiver.getId(), message, timestamp);
        }
        messageText.setText("");
    }

    private void sendMessage(String senderid, String receiverid, String text, int timestamp) {
        //get the reference of the database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("senderId", senderid);
        hashMap.put("receiverId", intent.getStringExtra("userid"));
        hashMap.put("text", text);
        hashMap.put("timestamp", timestamp);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void loadMessagesFromDatabase(String senderId, String receiverId) {
        //get the reference of the database
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    assert chat != null;
//                    if (chat.getReceiverId().equals(receiverId) && chat.getSenderId().equals(senderId) ||
//                            chat.getReceiverId().equals(senderId) && chat.getSenderId().equals(receiverId)) {
//                        chatMessages.add(chat);
//                    }
                    messageAdapter = new MessageAdapter(ChatActivity.this, chatMessages);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
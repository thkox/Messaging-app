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
    DatabaseReference reference1;

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

        Toolbar toolbar = findViewById(R.id.app_toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerViewChat);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        messageText = findViewById(R.id.editTextMessageText);
        sendButton = findViewById(R.id.floatingActionButtonSendMessage);

        chatMessages = new ArrayList<>();

        intent = getIntent();

        String userid = intent.getStringExtra("userid");

        // get the user id from the firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference1 = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get the user from the database
                receiver = snapshot.getValue(User.class);
                //set the title of the toolbar to the nickname of the user
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
        reference1 = FirebaseDatabase.getInstance().getReference("Chats");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceiverId().equals(receiverId) && chat.getSenderId().equals(senderId) ||
                            chat.getReceiverId().equals(senderId) && chat.getSenderId().equals(receiverId)) {
                        chatMessages.add(chat);
                    }
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
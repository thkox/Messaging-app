package eu.thkox.messaging_app.activity;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.thkox.messaging_app.R;
import eu.thkox.messaging_app.custom.tool.MessageAdapter;
import eu.thkox.messaging_app.data.model.Message;
import eu.thkox.messaging_app.data.model.User;

public class ChatActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;
    EditText messageText;
    RecyclerView recyclerView;
    FloatingActionButton sendButton;
    MessageAdapter messageAdapter;
    List<Message> chatMessages;
    User receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageText = findViewById(R.id.editTextMessageText);
        sendButton = findViewById(R.id.floatingActionButtonSendMessage);

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.app_toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set the layout of the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerViewChat);
        linearLayoutManager.setStackFromEnd(true); // TO BE CHECKED
        recyclerView.setLayoutManager(linearLayoutManager);


        intent = getIntent();
        String userId = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiver = snapshot.getValue(User.class);
                getSupportActionBar().setTitle(receiver.getNickname());
                //load the messages from the database
                loadMessagesFromDatabase(firebaseUser.getUid(), userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatMessages = new ArrayList<>();
    }


    public void sendMessageToDatabase(View view) {
        String text = messageText.getText().toString();
        int timestamp = (int) (System.currentTimeMillis() / 1000);

        if (!text.equals("")) {
            Message message = new Message(timestamp, firebaseUser.getUid(), receiver.getId(), text);
            sendMessage(message);
        }
        messageText.setText("");
    }

    private void sendMessage(Message message) {

        String chatId = generateChatId(message.getSenderId(), message.getReceiverId());

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Chats");
        // Create a new message entry
        DatabaseReference messageReference = reference2.child(chatId).child("messages").push();
        messageReference.setValue(message);
    }

    private String generateChatId(String user1Id, String user2Id) {
        List<String> ids = Arrays.asList(user1Id, user2Id);
        Collections.sort(ids);
        String concatenatedIds = String.join("_", ids);
        return Integer.toString(concatenatedIds.hashCode());
    }

    private void loadMessagesFromDatabase(String senderId, String receiverId) {
        String chatId = generateChatId(senderId, receiverId);

        //get the reference of the database
        reference = FirebaseDatabase.getInstance().getReference("Chats").child(chatId).child("messages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    assert message != null;
                    if (message.getReceiverId().equals(receiverId) && message.getSenderId().equals(senderId) ||
                            message.getReceiverId().equals(senderId) && message.getSenderId().equals(receiverId)) {
                        chatMessages.add(message);
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
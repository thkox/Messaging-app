package eu.thkox.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.util.HashMap;

import eu.thkox.messaging_app.data.model.Message;
import eu.thkox.messaging_app.data.model.User;

public class ChatActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;

    EditText messageText;

    FloatingActionButton sendButton;

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

        messageText = findViewById(R.id.editTextMessageText);
        sendButton = findViewById(R.id.floatingActionButtonSendMessage);

        intent = getIntent();

        String userid = intent.getStringExtra("userid");

        // get the user id from the firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get the user from the database
                receiver = snapshot.getValue(User.class);
                //set the title of the toolbar to the nickname of the user
                getSupportActionBar().setTitle(receiver.getNickname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void sendMessage(String senderid, String receiverid, String text, int timestamp) {
        //get the reference of the database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

//        //create a new message
//        Message message = new Message(timestamp, senderid, intent.getStringExtra("userid"), text);
//
//        //get the chat id
//        String chatid = intent.getStringExtra("chatid");
//
//        //add the message to the database
//        reference.child("Chats").child(chatid).child("Messages").push().setValue(message);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("senderid", senderid);
        hashMap.put("receiverid", intent.getStringExtra("userid"));
        hashMap.put("text", text);
        hashMap.put("timestamp", timestamp);

        reference.child("Chats").push().setValue(hashMap);

    }

    public void sendMessageToDatabase(View view) {

        String message = messageText.getText().toString();
        int timestamp = (int) (System.currentTimeMillis() / 1000);

        if (!message.equals("")) {
            sendMessage(firebaseUser.getUid(), receiver.getId(), message, timestamp);
        }

        messageText.setText("");
    }
}
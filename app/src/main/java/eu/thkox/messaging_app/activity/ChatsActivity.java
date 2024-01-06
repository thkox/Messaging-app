package eu.thkox.messaging_app.activity;

import static eu.thkox.messaging_app.utils.FirebaseUtils.getTheReferenceChat;
import static eu.thkox.messaging_app.utils.FirebaseUtils.getTheReferenceLastMessage;
import static eu.thkox.messaging_app.utils.FirebaseUtils.getTheReferenceMessages;
import static eu.thkox.messaging_app.utils.FirebaseUtils.getTheReferenceUser;
import static eu.thkox.messaging_app.utils.FirebaseUtils.getTheReferenceUsers;
import static eu.thkox.messaging_app.utils.FirebaseUtils.logOutUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import eu.thkox.messaging_app.R;
import eu.thkox.messaging_app.custom.tool.ChatRowAdapter;
import eu.thkox.messaging_app.data.model.Message;
import eu.thkox.messaging_app.data.model.User;
import eu.thkox.messaging_app.utils.ActivityUtils;
import eu.thkox.messaging_app.utils.FirebaseUtils;

public class ChatsActivity extends AppCompatActivity {

    RecyclerView recyclerViewChats;
    ChatRowAdapter adapter;

    List<User> users;
    List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        users = new ArrayList<>();
        messages = new ArrayList<>();

        //Set the layout of the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewChats = findViewById(R.id.recyclerViewChats);
        recyclerViewChats.setLayoutManager(layoutManager);

        // Set the toolbar
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //Firebase
        DatabaseReference referenceUser = getTheReferenceUser();
        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                getSupportActionBar().setTitle(String.format("Welcome back, %s", user.getNickname()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loadChats();
        displayChats();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logOutUser();
            ActivityUtils.goToMainActivity(ChatsActivity.this);
            return true;
        }
        return false;
    }


    int index = -1;


    private void loadChats(){
        // reference the "Users"
        DatabaseReference referenceUsers = getTheReferenceUsers();
        // for every user in the "Users", generate a chatId with the current user and compare it with the chatId that exists in the "Chats"
        referenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    // if the user is not the current user
                    if (!user.getId().equals(FirebaseUtils.getSignedInUser().getUid())) {
                        // generate the chatId
                        String chatId = FirebaseUtils.generateChatId(FirebaseUtils.getSignedInUser().getUid(), user.getId());
                        // reference the "Chats"
                        DatabaseReference referenceChats = FirebaseUtils.getTheReferenceChats();
                        // for every chat in the "Chats", check if the chatId exists
                        referenceChats.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Message message = dataSnapshot.getValue(Message.class);
                                    assert message != null;
                                    // if the chatId exists
                                    if (dataSnapshot.getKey().equals(chatId)) {
                                        // add the user and the last message to the lists
                                        if(!users.contains(user)){
                                            users.add(user);
                                            index = users.indexOf(user);
                                        } else {
                                            //get the user index
                                            index = users.indexOf(user);
                                        }

                                        DatabaseReference referenceMessages = getTheReferenceMessages(chatId);
                                        Query query = referenceMessages.orderByChild("timestamp").limitToLast(1);

                                        query.get().addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {

                                                for (DataSnapshot dataSnapshot1 : task.getResult().getChildren()) {
                                                    Message message1 = dataSnapshot1.getValue(Message.class);
                                                    assert message1 != null;
                                                    messages.add(message1);

                                                }
                                                // display the chats
                                                displayChats();

                                            }
                                        });
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void displayChats(){
        recyclerViewChats.removeAllViews();
        adapter = new ChatRowAdapter(this, users, messages);
        recyclerViewChats.setAdapter(adapter);
    }

    public void searchUserChat(View view) {
        ActivityUtils.goToSearchUserChatActivity(ChatsActivity.this);
    }
}
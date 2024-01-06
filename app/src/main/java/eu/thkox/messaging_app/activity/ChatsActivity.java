package eu.thkox.messaging_app.activity;

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
import android.view.Window;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import eu.thkox.messaging_app.R;
import eu.thkox.messaging_app.custom.tool.ChatRowAdapter;
import eu.thkox.messaging_app.data.model.Message;
import eu.thkox.messaging_app.data.model.User;
import eu.thkox.messaging_app.utils.ActivityUtils;
import eu.thkox.messaging_app.utils.FirebaseUtils;

public class ChatsActivity extends AppCompatActivity {

    RecyclerView recyclerViewChats;
    ChatRowAdapter adapter;

    HashMap<User, Message> userMessageHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        userMessageHashMap = new HashMap<>();

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.toolbar_color));

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

    private void loadChats(){
        DatabaseReference referenceUsers = getTheReferenceUsers();
        referenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    if (!user.getId().equals(FirebaseUtils.getSignedInUser().getUid())) {
                        String chatId = FirebaseUtils.generateChatId(FirebaseUtils.getSignedInUser().getUid(), user.getId());
                        DatabaseReference referenceChats = FirebaseUtils.getTheReferenceChats();
                        referenceChats.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot.getKey().equals(chatId)) {
                                        DatabaseReference referenceMessages = getTheReferenceMessages(chatId);
                                        Query query = referenceMessages.orderByChild("timestamp").limitToLast(1);
                                        query.get().addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {

                                                for (DataSnapshot dataSnapshot1 : task.getResult().getChildren()) {
                                                    Message message = dataSnapshot1.getValue(Message.class);
                                                    assert message != null;
                                                    userMessageHashMap.put(user, message);
                                                }
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
        adapter = new ChatRowAdapter(this, userMessageHashMap);
        recyclerViewChats.setAdapter(adapter);
    }

    public void searchUserChat(View view) {
        ActivityUtils.goToSearchUserChatActivity(ChatsActivity.this);
    }
}
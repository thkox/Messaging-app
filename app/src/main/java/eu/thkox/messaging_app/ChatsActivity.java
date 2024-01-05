package eu.thkox.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import eu.thkox.messaging_app.custom.tool.ChatRowAdapter;
import eu.thkox.messaging_app.data.model.Chat;
import eu.thkox.messaging_app.data.model.User;

public class ChatsActivity extends AppCompatActivity {

    RecyclerView recyclerViewChats;
    ChatRowAdapter adapter;
    List<Chat> chats;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        //Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        // Set the recycler view
        recyclerViewChats = findViewById(R.id.recyclerViewChats);

        // Set the toolbar
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        reference.addValueEventListener(new ValueEventListener() {
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
            // Perform logout
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            ActivityUtils.goToMainActivity(ChatsActivity.this);
            return true;
        }
        return false;
    }

    private void displayChats(){
        adapter = new ChatRowAdapter(this, chats);
        recyclerViewChats.setAdapter(adapter);
    }

    public void searchUserChat(View view) {
        ActivityUtils.goToSearchUserChatActivity(ChatsActivity.this);
    }
}
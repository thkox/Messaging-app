package eu.thkox.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import eu.thkox.messaging_app.custom.tool.SearchRowAdapter;
import eu.thkox.messaging_app.data.model.User;

public class SearchUserChatActivity extends AppCompatActivity {

    RecyclerView recyclerViewUsers;
    SearchRowAdapter adapter;


    List<User> users;


    DatabaseReference reference;
    EditText searchUser;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_chat);

        users = new ArrayList<>();

        // Set the recycler view
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        //Set the layout of the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewUsers.setLayoutManager(layoutManager);

        toolbar = findViewById(R.id.app_toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.search_chat_or_user);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the available users reference from the database
        reference = FirebaseDatabase.getInstance().getReference("Users");

        // Get the search user text view
        searchUser = findViewById(R.id.editTextSearchText);

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Handle the back button in the toolbar
            launchChatsActivity(); // This will finish the current activity and go back
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchUsers(View view) {
        String nickname = searchUser.getText().toString();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(!TextUtils.isEmpty(nickname)){
            reference.orderByChild("nickname").equalTo(nickname).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        users.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            User user = dataSnapshot.getValue(User.class);

                            assert user != null;
                            assert firebaseUser != null;

                            if (!user.getId().equals(firebaseUser.getUid())){
                                users.add(user);
                            } else {
                                Toast.makeText(SearchUserChatActivity.this, "You can't chat with yourself", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //Toast.makeText(SearchUserChatActivity.this, users.get(1).getEmail(), Toast.LENGTH_SHORT).show();


                        displayUsers();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Toast.makeText(this, "Please enter a nickname", Toast.LENGTH_SHORT).show();
        }
    }
    private void displayUsers(){
        adapter = new SearchRowAdapter(this, users);
        recyclerViewUsers.setAdapter(adapter);
    }

    private void launchChatsActivity() {
        Intent intent = new Intent(SearchUserChatActivity.this, ChatsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
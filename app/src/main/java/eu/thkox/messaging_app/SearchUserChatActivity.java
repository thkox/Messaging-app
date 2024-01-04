package eu.thkox.messaging_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class SearchUserChatActivity extends AppCompatActivity {


    RecyclerView recyclerViewUsers;
    SearchRowAdapter adapter;

    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_chat);

        // Get the chats from the database
        //chats = DatabaseManager.getInstance(this).getChats();

        // Set the recycler view
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        displayUsers();
    }

    public void displayUsers(){
        adapter = new SearchRowAdapter(this, users);
        recyclerViewUsers.setAdapter(adapter);
    }
}
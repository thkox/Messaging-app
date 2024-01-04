package eu.thkox.messaging_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    RecyclerView recyclerViewChats;
    ChatRowAdapter adapter;

    List<Chat> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        // Get the chats from the database
        //chats = DatabaseManager.getInstance(this).getChats();

        // Set the recycler view
        recyclerViewChats = findViewById(R.id.recyclerViewChats);

        displayChats();
    }

    private void displayChats(){
        adapter = new ChatRowAdapter(this, chats);
        recyclerViewChats.setAdapter(adapter);
    }
}
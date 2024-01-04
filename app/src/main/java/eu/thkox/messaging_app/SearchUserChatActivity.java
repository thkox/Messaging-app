package eu.thkox.messaging_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        toolbar = findViewById(R.id.app_toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.search_chat_or_user);

        // Get the available users reference from the database
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        // Get the search user text view
        searchUser = findViewById(R.id.editTextSearchText);


        // Set the recycler view
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        //displayUsers();
    }



    public void displayUsers(){
        adapter = new SearchRowAdapter(this, users);
        recyclerViewUsers.setAdapter(adapter);
    }

    public void searchUsers(View view) {
        String nickname = searchUser.getText().toString();
        if(!TextUtils.isEmpty(nickname)){

            reference.orderByChild("nickname").equalTo(nickname).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        if (users != null){
                            users.clear();
                        }
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            User user = dataSnapshot.getValue(User.class);
                            users.add(user);
                        }
                        Toast.makeText(SearchUserChatActivity.this, "User found", Toast.LENGTH_SHORT).show();
                        displayUsers();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
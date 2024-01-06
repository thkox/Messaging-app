package eu.thkox.messaging_app.custom.tool;

import static eu.thkox.messaging_app.utils.ActivityUtils.goToChatActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import eu.thkox.messaging_app.R;

import eu.thkox.messaging_app.data.model.Message;
import eu.thkox.messaging_app.data.model.User;

public class ChatRowAdapter extends RecyclerView.Adapter<ChatRowAdapter.RowViewHolder> {

    Context context;

    List<User> users;

    List<Message> messages;

    public ChatRowAdapter (Context context, HashMap<User, Message> userMessageHashMap) {
        this.context = context;

        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();

        for(User user : userMessageHashMap.keySet()) {
            this.users.add(user);
            this.messages.add(userMessageHashMap.get(user));
        }
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chat_row, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
        //position of the list of chats
        User user = users.get(position);
        Message message = messages.get(position);

        holder.textViewNickname.setText(user.getNickname());
        if (Objects.equals(user.getId(), message.getSenderId())) {
            holder.textViewLastMessage.setText(String.format("%s: %s", user.getNickname(), message.getText()));
        } else {
            holder.textViewLastMessage.setText(String.format("You: %s", message.getText()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity(context, user.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewNickname;
        public TextView textViewLastMessage;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNickname = itemView.findViewById(R.id.textViewNickname);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
        }
    }
}

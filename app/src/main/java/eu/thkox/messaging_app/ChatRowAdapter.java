package eu.thkox.messaging_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.Shapeable;

import java.util.ArrayList;
import java.util.List;

public class ChatRowAdapter extends RecyclerView.Adapter<ChatRowAdapter.RowViewHolder> {

    Context context;

    List<Chat> chats;
    public ChatRowAdapter (Context context, List<Chat> chats) {
        this.context = context;
        this.chats = chats;
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
        Chat chat = chats.get(position);
        // get the last message from the list of messages
        Message message = chat.getMessages().get(chat.getMessages().size()-1);

        //set the image of the user
        // holder.imageViewUser.setImageResource(chats.get(position).getUser().getImage()); -> to be implemented

        holder.textViewNickname.setText(message.getSender().getNickname());
        //get the last message from the list of messages
        holder.textViewMessage.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNickname;
        TextView textViewMessage;

        ShapeableImageView imageViewUser;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNickname = itemView.findViewById(R.id.textViewNickname);
            textViewMessage = itemView.findViewById(R.id.textViewLastMessage);
            imageViewUser = itemView.findViewById(R.id.imageViewUser);
        }
    }
}

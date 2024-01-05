package eu.thkox.messaging_app.custom.tool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import eu.thkox.messaging_app.R;
import eu.thkox.messaging_app.data.model.Chat;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.RowViewHolder> {

    int sender_message = 0;
    int receiver_message = 1;
    Context context;

    List<Chat> chatMessages;

    FirebaseUser firebaseUser;

    public MessageAdapter (Context context, List<Chat> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public MessageAdapter.RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (viewType == sender_message) {
            view = inflater.inflate(R.layout.right_message_adapter, parent, false);
        } else {
            view = inflater.inflate(R.layout.left_message_adapter, parent, false);
        }
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.RowViewHolder holder, int position) {
        Chat chat = chatMessages.get(position);
        holder.textViewMessage.setText(chat.getText());

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewMessage;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatMessages.get(position).getSenderId().equals(firebaseUser.getUid())) {
            return sender_message;
        } else {
            return receiver_message;
        }
    }
}

package eu.thkox.messaging_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchRowAdapter  extends RecyclerView.Adapter<SearchRowAdapter.RowViewHolder> {

    Context context;

    List<User> users;

    public SearchRowAdapter (Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_row, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
        //position of the list of chats
        User user = users.get(position);

        holder.textViewNickname.setText(user.getNickname());
        holder.textViewEmail.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNickname;
        TextView textViewEmail;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNickname = itemView.findViewById(R.id.textViewNickname);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
        }
    }
}

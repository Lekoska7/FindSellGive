package mk.com.findsellgive.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.holders.InboxItemViewHolder;
import mk.com.findsellgive.listeners.OnChatClickedListener;
import mk.com.findsellgive.models.Chat;


public class InboxRecyclerViewAdapter extends RecyclerView.Adapter<InboxItemViewHolder> {
    private List<Chat> messages;
    private Context context;
    private OnChatClickedListener listener;
    public InboxRecyclerViewAdapter(List<Chat> messages, Context context,OnChatClickedListener listener) {
        this.messages = messages;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InboxItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_item, viewGroup, false);
        return new InboxItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxItemViewHolder holder, int position) {
            Chat message = messages.get(position);
            holder.bind(message,listener);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}

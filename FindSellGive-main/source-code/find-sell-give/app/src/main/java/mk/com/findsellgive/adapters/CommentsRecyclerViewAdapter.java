package mk.com.findsellgive.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.holders.CommentViewHolder;
import mk.com.findsellgive.models.Comment;


public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private Context context;
    private List<Comment> comments;

    public CommentsRecyclerViewAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        if (comment != null) {
            holder.bind(comment);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}

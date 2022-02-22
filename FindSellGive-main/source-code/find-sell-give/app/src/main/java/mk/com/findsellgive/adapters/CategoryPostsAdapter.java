package mk.com.findsellgive.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.holders.CategoryPostItemViewHolder;
import mk.com.findsellgive.listeners.OnItemClickListener;
import mk.com.findsellgive.models.WishListPost;


public class CategoryPostsAdapter extends RecyclerView.Adapter<CategoryPostItemViewHolder> {
    private List<WishListPost> posts;
    private Context context;
    private OnItemClickListener listener;

    public CategoryPostsAdapter(List<WishListPost> posts, Context context, OnItemClickListener listener) {
        this.posts = posts;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryPostItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.wishtlist_item, parent, false);
        return new CategoryPostItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryPostItemViewHolder holder, int position) {
        WishListPost post = posts.get(position);
        if (post != null) {
            holder.bind(post,listener);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}

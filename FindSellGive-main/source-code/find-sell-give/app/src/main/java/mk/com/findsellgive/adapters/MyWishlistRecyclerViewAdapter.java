package mk.com.findsellgive.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.holders.MyWishlistItemViewHolder;
import mk.com.findsellgive.models.WishListPost;


public class MyWishlistRecyclerViewAdapter extends RecyclerView.Adapter<MyWishlistItemViewHolder> {
    private List<WishListPost> items;

    public MyWishlistRecyclerViewAdapter(List<WishListPost> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MyWishlistItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_item, viewGroup, false);
        return new MyWishlistItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWishlistItemViewHolder holder, int position) {
        WishListPost post = items.get(position);
        if (post != null) {
            holder.bind(post);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

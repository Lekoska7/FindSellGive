package mk.com.findsellgive.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import mk.com.findsellgive.R;
import mk.com.findsellgive.models.WishListPost;


public class MyWishlistItemViewHolder extends RecyclerView.ViewHolder {
    private AppCompatImageView ivItemImage;
    private AppCompatTextView tvItemText;

    public MyWishlistItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ivItemImage = itemView.findViewById(R.id.iv_post_image);
        tvItemText = itemView.findViewById(R.id.tv_post_text);
    }
    public void bind(WishListPost post){
        if(post!=null){
            tvItemText.setText(post.getQuestion());
        }
    }
}

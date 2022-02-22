package mk.com.findsellgive.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.picasso.Picasso;

import mk.com.findsellgive.R;
import mk.com.findsellgive.models.Product;


public class MyProductItemViewHolder extends RecyclerView.ViewHolder {
    private AppCompatImageView ivItemImage;
    private AppCompatTextView tvItemText;

    public MyProductItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ivItemImage = itemView.findViewById(R.id.iv_post_image);
        tvItemText = itemView.findViewById(R.id.tv_post_text);
    }
    public void bind(Product product){
        if(product!=null){
            tvItemText.setText(product.getName());
            Picasso.get()
                    .load(product.getImageUrl())
                    .error(R.drawable.ic_home)
                    .placeholder(R.drawable.progress_loader)
                    .fit()
                    .into(ivItemImage);
        }
    }
}

package mk.com.findsellgive.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.holders.ProductItemViewHolder;
import mk.com.findsellgive.listeners.OnProductItemClickListener;
import mk.com.findsellgive.models.Product;


public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductItemViewHolder> {
    private List<Product> products;
    private boolean action;
    private OnProductItemClickListener listener;

    public ProductRecyclerViewAdapter(List<Product> products, boolean action, OnProductItemClickListener listener) {
        this.products = products;
        this.action = action;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item,
                viewGroup, false);
        return new ProductItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        Product product = products.get(position);
        if (product != null) {
            holder.bind(product,action,listener);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}

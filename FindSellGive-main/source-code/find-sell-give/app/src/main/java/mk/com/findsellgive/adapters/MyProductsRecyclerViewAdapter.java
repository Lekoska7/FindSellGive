package mk.com.findsellgive.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.holders.MyProductItemViewHolder;
import mk.com.findsellgive.models.Product;


public class MyProductsRecyclerViewAdapter extends RecyclerView.Adapter<MyProductItemViewHolder> {
    private List<Product> products;

    public MyProductsRecyclerViewAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public MyProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_item, viewGroup, false);
        return new MyProductItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductItemViewHolder holder, int position) {
        Product product = products.get(position);
        if (product != null) {
            holder.bind(product);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}

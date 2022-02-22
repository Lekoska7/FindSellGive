package mk.com.findsellgive.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import mk.com.findsellgive.R;
import mk.com.findsellgive.listeners.OnProductItemClickListener;
import mk.com.findsellgive.models.Product;
import mk.com.findsellgive.models.User;


public class ProductItemViewHolder extends RecyclerView.ViewHolder {
    private AppCompatImageView ivProductImage;
    private AppCompatTextView tvProductName;
    private AppCompatTextView tvProductCategory;
    private AppCompatTextView tvProductCondition;
    private AppCompatTextView tvProductCity;
    private AppCompatTextView tvProductPrice;
    private AppCompatTextView tvProductOwner;
    private AppCompatImageView ivSaveToCollection;

    //povrzuvanje na layoutot od kelijata so polinjata
    public ProductItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ivProductImage = itemView.findViewById(R.id.iv_product_image);
        tvProductName = itemView.findViewById(R.id.tv_product_name);
        tvProductCategory = itemView.findViewById(R.id.tv_product_category);
        tvProductCondition = itemView.findViewById(R.id.tv_product_condition);
        tvProductCity = itemView.findViewById(R.id.tv_product_city);
        tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        tvProductOwner = itemView.findViewById(R.id.tv_product_owner);
        ivSaveToCollection = itemView.findViewById(R.id.iv_save_to_collection);
    }

    public void bind(final Product product, boolean action, final OnProductItemClickListener listener) {
        if (product != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(product);
                }
            });
            Picasso.get()
                    .load(product.getImageUrl())
                    .error(R.drawable.ic_home)
                    .placeholder(R.drawable.progress_loader)
                    .fit()
                    .into(ivProductImage);
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection("users").document(product.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null) {
                                    User user = task.getResult().toObject(User.class);
                                    if (user != null) {
                                        tvProductName.setText(product.getName());
                                        boolean condition = product.isCondition();
                                        tvProductCondition.setText(condition ? "New" : "Used"); //po kratka verzija na proverkata
                                        String price = String.format(Locale.ENGLISH, "%.2f MKD", product.getPrice());
                                        tvProductPrice.setText(price);
                                        tvProductCity.setText(product.getCity());
                                        tvProductCategory.setText(product.getProductCategory(product.getCategory()));
                                        tvProductOwner.setText(user.getFullName());
                                    }
                                }
                            }
                        }
                    });
            if (action) {
                //dodadi vo moja kolekcija
                ivSaveToCollection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                            database.collection("users")
                                    .document(user.getUid()).collection("saved")
                                    .document(product.getId()).set(product.getProductMap(product)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(itemView.getContext(), "Successfully added to My Collection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            } else {
                //izbrisi od moja kolekcija
                ivSaveToCollection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                            database.collection("users")
                                    .document(user.getUid()).collection("saved")
                                    .document(product.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(itemView.getContext(), "Successfully removed from My Collection", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
            }
        }
    }
}

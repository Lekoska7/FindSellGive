package mk.com.findsellgive.fragments;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.activities.ProfileActivity;
import mk.com.findsellgive.adapters.ProductRecyclerViewAdapter;
import mk.com.findsellgive.models.Product;
import mk.com.findsellgive.utills.SharedPreferencesManager;
import mk.com.findsellgive.utills.UtilitiesHelper;

public class AllProductsFragment extends HomeBaseFragment {
    private RecyclerView rvItems;
    private ProductRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private TextView noData;
    private FirebaseFirestore database;
    private List<Product> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = getFragmentView(inflater, container, savedInstanceState);
        rvItems = fragmentView.findViewById(R.id.rv_items);
        progressBar = fragmentView.findViewById(R.id.progress_bar);
        noData = fragmentView.findViewById(R.id.tv_no_data);
        adapter = new ProductRecyclerViewAdapter(products, true, this);
        initRecyclerView(rvItems, adapter);
        database = FirebaseFirestore.getInstance();
        getAllProducts();
        return fragmentView;
    }


    public static AllProductsFragment newInstance() {
        return new AllProductsFragment();
    }

    @Override
    void getProductByPurpose(int purpose) {
        //not used
    }

    @Override
    void getAllProducts() {
        showLoader(true, rvItems, progressBar);
        database.collection("products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documents, FirebaseFirestoreException e) {
                for (DocumentChange document : documents.getDocumentChanges()) {
                    switch (document.getType()) {
                        case ADDED:
                            Product product = new Product(document.getDocument().getData());
                            if (!products.contains(product)) {
                                products.add(product);
                            }
                            displayAd(product);
                            break;
                    }
                }
                if (products.isEmpty()) {
                    showNoData(true, rvItems, noData, progressBar);
                } else {
                    showNoData(false, rvItems, noData, progressBar);
                    showLoader(false, rvItems, progressBar);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void displayAd(final Product product) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Location userLocation = new Location("");
        double lat = SharedPreferencesManager.getInstance(getContext()).getLatitude();
        double lon = SharedPreferencesManager.getInstance(getContext()).getLongitude();
        userLocation.setLongitude(lon);
        userLocation.setLatitude(lat);
        Location productLocation = new Location("");
        productLocation.setLatitude(product.getLocation().getLatitude());
        productLocation.setLongitude(product.getLocation().getLongitude());
        float distance = productLocation.distanceTo(userLocation) / 1000; //vo kilomentri
        if (distance < 50) {
            //proveruvame dali proizvodot se naogja vo radius od 50km od korisnikot (moze da se smeni spored potrebi)
            if (user != null) {
                if (!product.getUid().equals(user.getUid())) {
                    if (getContext() != null) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.new_product);
                        dialog.setCancelable(false);
                        AppCompatImageView logo = dialog.findViewById(R.id.iv_logo);
                        Picasso.get()
                                .load(product.getImageUrl())
                                .fit()
                                .centerInside()
                                .into(logo);
                        dialog.findViewById(R.id.tv_check_product).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                UtilitiesHelper.goToProfilePage(getContext(), ProfileActivity.class, product.getUid());
                            }
                        });
                        dialog.findViewById(R.id.tv_dismiss).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(Product product) {
        UtilitiesHelper.goToProfilePage(getContext(), ProfileActivity.class, product.getUid());
    }
}

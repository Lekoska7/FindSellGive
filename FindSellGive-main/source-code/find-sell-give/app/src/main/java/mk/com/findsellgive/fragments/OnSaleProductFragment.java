package mk.com.findsellgive.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.activities.ProfileActivity;
import mk.com.findsellgive.adapters.ProductRecyclerViewAdapter;
import mk.com.findsellgive.models.Product;
import mk.com.findsellgive.utills.UtilitiesHelper;


public class OnSaleProductFragment extends HomeBaseFragment {
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
        rvItems = fragmentView.findViewById(R.id.rv_items);
        progressBar = fragmentView.findViewById(R.id.progress_bar);
        noData = fragmentView.findViewById(R.id.tv_no_data);
        adapter = new ProductRecyclerViewAdapter(products, true, this);
        initRecyclerView(rvItems, adapter);//povikuvanje na metodot od abstractnata klasa
        database = FirebaseFirestore.getInstance();
        getProductByPurpose(0);
        return fragmentView;
    }

    public static OnSaleProductFragment newInstance() {
        return new OnSaleProductFragment();
    }

    @Override
    void getProductByPurpose(int purpose) {
        database.collection("products")
                .whereEqualTo("purpose", purpose).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documents, FirebaseFirestoreException e) {
                for (DocumentChange document : documents.getDocumentChanges()) {
                    switch (document.getType()) {
                        case ADDED:
                            // Product product = document.getDocument().toObject(Product.class);
                            Product product = new Product(document.getDocument().getData());
                            if (!products.contains(product)) {
                                products.add(product);
                            }
                            break;
                    }
                }
                if (products.isEmpty()) {
                    showNoData(true, rvItems, noData, progressBar);
                } else {
                    showNoData(false, rvItems, noData, progressBar);
                    showLoader(false, rvItems, progressBar);
                    adapter.notifyDataSetChanged();//go izvestuvame adapterot deka ima novi podatoci, osvezi ja listata
                }
            }
        });
    }

    @Override
    void getAllProducts() {
        //not used
    }

    @Override
    public void onItemClick(Product product) {
        UtilitiesHelper.goToProfilePage(getContext(), ProfileActivity.class, product.getUid());
    }
}

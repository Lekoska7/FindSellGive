package mk.com.findsellgive.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.activities.MainActivity;
import mk.com.findsellgive.adapters.ProductRecyclerViewAdapter;
import mk.com.findsellgive.listeners.OnProductItemClickListener;
import mk.com.findsellgive.models.Product;


public class SaveToCollectionFragment extends Fragment implements OnProductItemClickListener {
    private RecyclerView rvItems;
    private TextView noData;
    private ProgressBar progressBar;
    private List<Product> products = new ArrayList<>();
    private ProductRecyclerViewAdapter adapter;
    private FirebaseFirestore database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.tab_saved, container, false);
        initViews(fragmentView);
        loadData();
        return fragmentView;
    }

    private void loadData() {
        if (user != null) {
            database.collection("users").document(user.getUid())
                    .collection("saved").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (documentSnapshots != null && !documentSnapshots.isEmpty()) {
                        showLoader(true,rvItems,progressBar);
                        for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                            Product product = new Product(documentChange.getDocument().getData());
                            switch (documentChange.getType()) {
                                case ADDED:
                                    if (!products.contains(product)) {
                                        products.add(product);
                                    }
                                    break;
                                case REMOVED:
                                    for (Product p : products) {
                                        if (p.getId().equals(product.getId())) {
                                            products.remove(product);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                        if (!products.isEmpty()) {
                            showNoData(false, rvItems, noData, progressBar);
                            showLoader(false, rvItems, progressBar);
                            noData.setVisibility(View.GONE);
                            rvItems.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        } else {
                          // showLoader(false,rvItems,progressBar);
                           rvItems.setVisibility(View.GONE);
                           noData.setVisibility(View.VISIBLE);
                            //showNoData(true, rvItems, noData, progressBar);
                        }
                    }
                }
            });
            Log.e("TAG",products.size()+"");
        }
    }

    private void initViews(View view) {
        rvItems = view.findViewById(R.id.rv_items);
        noData = view.findViewById(R.id.tv_no_data);
        progressBar = view.findViewById(R.id.progress_bar);
        adapter = new ProductRecyclerViewAdapter(products, false,this);
        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvItems.setLayoutManager(llm);
        rvItems.setAdapter(adapter);
    }

    /**
     * metod sto go krie recyclerview-to, a go prikazuva loader i vice versa spored flagot
     *
     * @param show         - flag kojsto odreduva sto da se skrie, a sto da se prikaze
     * @param recyclerView
     * @param progressBar
     */
    public void showLoader(boolean show, RecyclerView recyclerView, ProgressBar progressBar) {
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * metod sto go krie recyclerview-to, a go prikazuva poraka i vice versa spored flagot
     *
     * @param show         - flag kojsto odreduva sto da se skrie, a sto da se prikaze
     * @param recyclerView
     * @param noData
     */
    public void showNoData(boolean show, RecyclerView recyclerView, TextView noData, ProgressBar progressBar) {
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show ? View.GONE : View.VISIBLE);
        noData.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public static SaveToCollectionFragment newInstance() {
        return new SaveToCollectionFragment();
    }

    @Override
    public void onItemClick(Product product) {
        MainActivity.mainPages.setCurrentItem(3);
        MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }
}

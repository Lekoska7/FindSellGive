package mk.com.findsellgive.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.adapters.MyProductsRecyclerViewAdapter;
import mk.com.findsellgive.models.Product;


public class MyProductsFragment extends ProfileBaseFragment {
    private RecyclerView rvMessages;
    private MyProductsRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private TextView noData;
    private FirebaseFirestore database;
    private FirebaseUser user;
    private List<Product> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = getFragmentView(inflater, container, savedInstanceState);
        rvMessages = fragmentView.findViewById(R.id.rv_items);
        progressBar = fragmentView.findViewById(R.id.progress_bar);
        adapter = new MyProductsRecyclerViewAdapter(products);
        initRecyclerView(rvMessages, adapter);//povikuvanje na metodot od abstractnata klasa
        noData = fragmentView.findViewById(R.id.tv_no_data);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseFirestore.getInstance();
        getMyPosts();
        return fragmentView;
    }

    private void initRecyclerView(RecyclerView recyclerView, MyProductsRecyclerViewAdapter adapter) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    public static MyProductsFragment newInstance() {
        return new MyProductsFragment();
    }

    /**
     * metod sto gi vrakja site poraki na korisnikot
     */
    @Override
    void getInbox() {
        //not used
    }

    /**
     * metod sto gi vrakja site postovi na korisnikot
     */
    @Override
    void getMyPosts() {
        if (user != null) {
            progressBar.setVisibility(View.VISIBLE);
            database.collection("products").
                    whereEqualTo("uid", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (documentSnapshots != null && !documentSnapshots.getDocumentChanges().isEmpty()) {
                        for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                         Product product = new Product(documentChange.getDocument().getData());
                            if (!products.contains(product)) {
                                products.add(product);
                            }
                        }
                        if (!products.isEmpty()) {
                            progressBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            showNoData(true, rvMessages, noData, progressBar);
                        }
                    }
                }
            });
        }
    }
}

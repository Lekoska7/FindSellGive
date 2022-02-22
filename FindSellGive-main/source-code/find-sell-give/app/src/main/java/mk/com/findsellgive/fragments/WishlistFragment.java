package mk.com.findsellgive.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.listeners.GoToCategoryPageListener;
import mk.com.findsellgive.models.WishListPost;


public class WishlistFragment extends Fragment {
    private LinearLayout lytElectronics;
    private LinearLayout lytHousing;
    private LinearLayout lytEntertainment;
    private LinearLayout lytOther;
    private TextView tvNumPostsElectronics;
    private TextView tvNumPostsHousing;
    private TextView tvNumPostsEntertainment;
    private TextView tvNumPostsOther;
    private TextView tvNumCommentsElectronics;
    private TextView tvNumCommentsHousing;
    private TextView tvNumCommentsEntertainment;
    private TextView tvNumCommentsOther;
    private int postsElectronics = 0;
    private int postsHousing = 0;
    private int postsEntertainment = 0;
    private int postsOther = 0;
    private int commentsElectronics = 0;
    private int commentsHousing = 0;
    private int commentsEntertainment = 0;
    private int commentsOther = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.tab_wishlist, container, false);
        initViews(fragmentView);
        initListeners();
        getData();
        return fragmentView;
    }

    private void getData() {
        setElectronicsPosts();
        setHousingPosts();
        setEntertaimentsPosts();
        setOtherPosts();
        setCommentsCountElectronics();
        setCommentsCountHousing();
        setCommentsCountEntertainment();
        setCommentsCountOther();
    }

    private void setCommentsCountOther() {
        FirebaseFirestore.getInstance().collection("wishlist").whereEqualTo("category",3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot documentSnapshots,  FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
                       List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                        for (DocumentChange doc:documentChanges){
                            WishListPost post = doc.getDocument().toObject(WishListPost.class);
                            switch (doc.getType()){
                              case ADDED:
                                    commentsOther+=post.getCommentsCount();
                                break;
                                case MODIFIED:
                                    break;
                                case REMOVED:
                                    break;

                            }
                        }
                        tvNumCommentsOther.setText(String.valueOf(commentsOther));
                    }else{
                    tvNumCommentsOther.setText("0");
                }
            }
        });
    }

    private void setCommentsCountEntertainment() {
        FirebaseFirestore.getInstance().collection("wishlist").whereEqualTo("category",2)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots,FirebaseFirestoreException e) {
                        if(documentSnapshots!=null){
                            List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                            for (DocumentChange doc:documentChanges){
                                WishListPost post = doc.getDocument().toObject(WishListPost.class);
                                switch (doc.getType()){
                                    case ADDED:
                                        commentsEntertainment+=post.getCommentsCount();
                                        break;
                                    case REMOVED:
                                        break;
                                    case MODIFIED:
                                        break;
                                }
                            }
                            tvNumCommentsEntertainment.setText(String.valueOf(commentsEntertainment));
                        }else{
                            tvNumCommentsEntertainment.setText("0");
                        }
                    }
                });
    }

    private void setCommentsCountHousing() {
        FirebaseFirestore.getInstance().collection("wishlist").whereEqualTo("category",1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if(documentSnapshots!=null){
                            List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                            for (DocumentChange documentChange : documentChanges){
                                WishListPost post = documentChange.getDocument().toObject(WishListPost.class);
                                switch (documentChange.getType()){
                                    case ADDED:
                                        commentsHousing+=post.getCommentsCount();
                                        break;
                                    case MODIFIED:
                                        break;
                                    case REMOVED:
                                        break;
                                }
                            }
                            tvNumCommentsHousing.setText(String.valueOf(commentsHousing));
                        }else{
                            tvNumCommentsHousing.setText("0");
                        }
                    }
                });
    }

    private void setCommentsCountElectronics() {
        FirebaseFirestore.getInstance().collection("wishlist").whereEqualTo("category",0)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if(documentSnapshots!=null){
                            List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                            for (DocumentChange documentChange: documentChanges){
                                WishListPost post = documentChange.getDocument().toObject(WishListPost.class);
                                switch (documentChange.getType()){
                                    case ADDED:
                                        commentsElectronics+=post.getCommentsCount();
                                        break;
                                    case MODIFIED:
                                        break;
                                    case REMOVED:
                                        break;
                                }
                            }
                            tvNumCommentsElectronics.setText(String.valueOf(commentsElectronics));
                        }else{
                            tvNumCommentsElectronics.setText("0");
                        }
                    }
                });
    }

    private void setOtherPosts() {
        FirebaseFirestore.getInstance().collection("wishlist").whereEqualTo("category",3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
                    List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                    postsOther = documentChanges.size();
                    tvNumPostsOther.setText(String.valueOf(postsOther));
                }else{
                    tvNumPostsOther.setText("0");
                }
            }
        });
    }

    private void setEntertaimentsPosts() {
        FirebaseFirestore.getInstance().collection("wishlist").whereEqualTo("category",2).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
                    postsEntertainment = documentSnapshots.getDocumentChanges().size();
                    tvNumPostsEntertainment.setText(String.valueOf(postsEntertainment));
                }else{
                    tvNumPostsEntertainment.setText("0");
                }
            }
        });
    }

    private void setHousingPosts() {
        FirebaseFirestore.getInstance().collection("wishlist").whereEqualTo("category",1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
                    postsHousing = documentSnapshots.getDocumentChanges().size();
                    tvNumPostsHousing.setText(String.valueOf(postsHousing));
                }else{
                    tvNumPostsHousing.setText("0");
                }
            }
        });
    }

    private void setElectronicsPosts() {
        FirebaseFirestore.getInstance().collection("wishlist").whereEqualTo("category",0).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(documentSnapshots!=null){
                    postsElectronics = documentSnapshots.getDocuments().size();
                    tvNumPostsElectronics.setText(String.valueOf(postsElectronics));
                }else{
                    tvNumPostsElectronics.setText("0");
                }
            }
        });
    }

    private void initListeners() {
        lytElectronics.setOnClickListener(new GoToCategoryPageListener(getContext(), 0));
        lytHousing.setOnClickListener(new GoToCategoryPageListener(getContext(), 1));
        lytEntertainment.setOnClickListener(new GoToCategoryPageListener(getContext(), 2));
        lytOther.setOnClickListener(new GoToCategoryPageListener(getContext(), 3));

    }

    private void initViews(View view) {
        lytElectronics = view.findViewById(R.id.lyt_electronics);
        lytHousing = view.findViewById(R.id.lyt_housing);
        lytEntertainment = view.findViewById(R.id.lyt_entertainment);
        lytOther = view.findViewById(R.id.lyt_other);
        tvNumPostsElectronics = view.findViewById(R.id.tv_num_posts_electronics);
        tvNumPostsHousing = view.findViewById(R.id.tv_num_posts_housing);
        tvNumPostsEntertainment = view.findViewById(R.id.tv_num_posts_entertainment);
        tvNumPostsOther = view.findViewById(R.id.tv_num_posts_other);
        tvNumCommentsElectronics = view.findViewById(R.id.tv_num_comments_electronics);
        tvNumCommentsHousing = view.findViewById(R.id.tv_num_comments_housing);
        tvNumCommentsEntertainment = view.findViewById(R.id.tv_num_comments_entertainment);
        tvNumCommentsOther = view.findViewById(R.id.tv_num_comments_other);
    }

    public static WishlistFragment newInstance() {
        return new WishlistFragment();
    }
}

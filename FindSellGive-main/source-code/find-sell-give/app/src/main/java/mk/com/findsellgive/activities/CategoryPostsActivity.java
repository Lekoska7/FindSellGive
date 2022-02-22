package mk.com.findsellgive.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import mk.com.findsellgive.R;
import mk.com.findsellgive.adapters.CategoryPostsAdapter;
import mk.com.findsellgive.listeners.OnItemClickListener;
import mk.com.findsellgive.models.WishListPost;

public class CategoryPostsActivity extends AppCompatActivity implements OnItemClickListener {
    private CategoryPostsAdapter adapter;
    private List<WishListPost> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_posts);
        RecyclerView rvPosts = findViewById(R.id.rv_posts);
        adapter = new CategoryPostsAdapter(posts, this, this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvPosts.setLayoutManager(llm);
        rvPosts.setAdapter(adapter);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        if (getIntent() != null) {
            int category = getIntent().getIntExtra("category", 0);
            database.collection("wishlist")
                    .whereEqualTo("category", category).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documents, @Nullable FirebaseFirestoreException e) {
                    if (documents != null && documents.size() > 0) {
                        for (DocumentChange documentChange : documents.getDocumentChanges()) {
                            switch (documentChange.getType()) {
                                case ADDED:
                                    WishListPost post = documentChange.getDocument().toObject(WishListPost.class);
                                    if (!posts.contains(post)) {
                                        posts.add(post);
                                    }
                                    break;
                                case MODIFIED:

                                    break;
                                case REMOVED:

                            }
                        }
                        if (!posts.isEmpty()) {
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CategoryPostsActivity.this, "Nema podatoci", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });
        }
    }

    public String getProductCategory(int category) {
        switch (category) {
            case 0:
                return "Electronics";
            case 1:
                return "Housing";
            case 2:
                return "Entertainment";
            case 3:
                return "Other";
            default:
                return "Electronics";
        }
    }

    @Override
    public void onItemClick(WishListPost post) {
        Intent intent = new Intent(CategoryPostsActivity.this, PostDetailsActivity.class);
        intent.putExtra("post_id", post.getId());
        startActivity(intent);
    }
}

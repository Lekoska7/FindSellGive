package mk.com.findsellgive.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

import mk.com.findsellgive.R;
import mk.com.findsellgive.models.WishListPost;
import mk.com.findsellgive.utills.SharedPreferencesManager;

public class AddNewWishlistPostActivity extends AppCompatActivity {
    private AppCompatEditText etPost;
    private Spinner spCategory;
    private FirebaseFirestore firebaseFirestoreDatabase;
    private FirebaseUser firebaseUser;
    private int category = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_wishlist_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseFirestoreDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();//nasiot korisnik
        initViews();
        initListeners();
    }

    private void initListeners() {
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        findViewById(R.id.bt_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePostToDb();
            }
        });
    }

    private void savePostToDb() {
        String description = etPost.getText().toString();
        String id = firebaseFirestoreDatabase.collection("wishlist").document().getId();
        double lat = SharedPreferencesManager.getInstance(this).getLatitude();
        double lon = SharedPreferencesManager.getInstance(this).getLongitude();
        GeoPoint location = new GeoPoint(lat,lon);
        WishListPost post = new WishListPost(firebaseUser.getUid(), description, category,
                0, new Date(), id,location);
        firebaseFirestoreDatabase.collection("wishlist").document(id)
                .set(post.getWishListPostMap(post)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    private void initViews() {
        etPost = findViewById(R.id.et_post_description);
        spCategory = findViewById(R.id.sp_product_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.product_categories));
        spCategory.setAdapter(adapter);
    }

}

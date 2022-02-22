package mk.com.findsellgive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import mk.com.findsellgive.R;
import mk.com.findsellgive.models.RatedUser;
import mk.com.findsellgive.models.User;

public class ProfileActivity extends AppCompatActivity {
    private AppCompatTextView tvUserName;
    private MaterialRatingBar rating;
    private CircleImageView ivAvatar;
    private AppCompatTextView tvEmail;
    private FloatingActionButton fbSendMessage;
    private FirebaseFirestore database;
    private FirebaseUser firebaseUser;//nasiot korisnik
    private User user;//korisnikot na kogo kje prakjame poraka
    private String uid;
    private String msg;
    private float newRating = 0;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        if (getIntent() != null) {
            uid = getIntent().getStringExtra("uid");
            if (!uid.equals("")) {
                database.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isComplete()) {
                            user = task.getResult().toObject(User.class);
                            if (user != null) {
                                tvUserName.setText(user.getFullName());
                                tvEmail.setText(user.getEmail());
                                if (!user.getProfileImage().equals("")) {
                                    Picasso.get().load(user.getProfileImage())
                                            .error(R.drawable.ic_cart)
                                            .placeholder(R.drawable.ic_home)
                                            .into(ivAvatar);
                                } else {
                                    ivAvatar.setImageResource(R.drawable.ic_home);
                                }
                                // UtilitiesHelper.setRating(user, rating);
                            }
                        }
                    }
                });
            }
        }
        fbSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iChat = new Intent(ProfileActivity.this, ChatActivity.class);
                iChat.putExtra("friendId", user.getUid());
                startActivity(iChat);
            }
        });
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.e("rating", rating + "");
                newRating = rating;
                menuItem.setVisible(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile_menu, menu);
        menuItem = menu.findItem(R.id.action_submit);
        menuItem.setVisible(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_submit) {
            Log.e("TAG", firebaseUser.getUid());
            database.collection("users")
                    .document(firebaseUser.getUid()).collection("ratedUsers")
                    .document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult() != null) {
                        RatedUser ratedUser = task.getResult().toObject(RatedUser.class);
                        if (ratedUser != null) {
                            if (ratedUser.getUid().equals(user.getUid())) {
                                if (!ratedUser.isRated()) {
                                    Toast.makeText(ProfileActivity.this, "You have already rated this user!", Toast.LENGTH_SHORT).show();
                                } else {
                                    user.getRating().addRating(newRating);
                                    RatedUser rUser = new RatedUser(user.getUid(), user.getFullName(), true, user.getRating());
                                    database.collection("users").document(user.getUid()).update("rating",
                                            user.getRating().getMap(user.getRating()));
                                    database.collection("users").document(firebaseUser.getUid())
                                            .collection("ratedUsers").document(user.getUid()).set(rUser.getMap(rUser));
                                }
                            }
                        } else {
                            Toast.makeText(ProfileActivity.this,"Rating submitted succesfully!",Toast.LENGTH_SHORT).show();
                            user.getRating().addRating(newRating);
                            RatedUser rUser = new RatedUser(user.getUid(), user.getFullName(), true, user.getRating());
                            database.collection("users").document(user.getUid()).update("rating",
                                    user.getRating().getMap(user.getRating()));
                            database.collection("users").document(firebaseUser.getUid())
                                    .collection("ratedUsers").document(user.getUid()).set(rUser.getMap(rUser));
                        }
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        fbSendMessage = findViewById(R.id.fab);
        tvUserName = findViewById(R.id.tv_userName);
        rating = findViewById(R.id.rating);
        ivAvatar = findViewById(R.id.iv_user_avatar);
        tvEmail = findViewById(R.id.tv_email);
        database = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

}

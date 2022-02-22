package mk.com.findsellgive.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import mk.com.findsellgive.R;
import mk.com.findsellgive.activities.AddNewProductActivity;
import mk.com.findsellgive.activities.AddNewWishlistPostActivity;
import mk.com.findsellgive.activities.EditProfileActivity;
import mk.com.findsellgive.activities.LoginActivity;
import mk.com.findsellgive.adapters.ApplicationViewPagerAdapter;
import mk.com.findsellgive.models.User;
import mk.com.findsellgive.utills.UtilitiesHelper;
import mk.com.findsellgive.viewpagers.NonSwipeableViewPager;


public class ProfileFragment extends Fragment {
    private AppCompatTextView tvFullName;
    private AppCompatTextView tvEmail;
    private CircleImageView ivAvatar;
    private TabLayout tabs;
    private NonSwipeableViewPager vpProfilePages;
    private FloatingActionButton fabProfile;
    private FloatingActionButton fabAddProduct;
    private FloatingActionButton fabAddToWishlist;
    private FirebaseAuth firebaseAuth;
    private MaterialRatingBar ratingBar;
    private FirebaseFirestore database;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> fragmentTitles = new ArrayList<>();
    private boolean isOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.tab_profile, container, false);
        initViews(fragmentView);
        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            getUserData(firebaseUser.getUid());
        }
        vpProfilePages.setOffscreenPageLimit(3);
        setupViewPager(vpProfilePages, tabs);
        initListeners();
        setHasOptionsMenu(true);//izvesti go glavnoto aktiviti deka fragmentot ima svoe menu
        return fragmentView;
    }

    private void initListeners() {
        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    showFabs(true);
                    isOpen = true;
                } else {
                    showFabs(false);
                    isOpen = false;
                }
            }
        });
        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilitiesHelper.openNewPage(getContext(), AddNewProductActivity.class);
            }
        });
        fabAddToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilitiesHelper.openNewPage(getContext(), AddNewWishlistPostActivity.class);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                UtilitiesHelper.openNewPage(getContext(), EditProfileActivity.class);
                return true;
            case R.id.action_logout:
                firebaseAuth.signOut();
                UtilitiesHelper.openNewPage(getContext(), LoginActivity.class);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("RestrictedApi")
    private void showFabs(boolean show) {
        fabAddProduct.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        fabAddToWishlist.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * metod sto gi zema podatocite za mojot korisnik od baza i gi prikazuva vo elementite
     *
     * @param uid
     */
    private void getUserData(String uid) {
        database.collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isComplete()) {
                            User user = task.getResult().toObject(User.class);
                            if (user != null) {
                                tvFullName.setText(user.getFullName());
                                tvEmail.setText(user.getEmail());
                                if(!user.getProfileImage().equals("")){
                                    Picasso.get().load(user.getProfileImage())
                                            .error(R.drawable.ic_cart)
                                            .placeholder(R.drawable.ic_home)
                                            .into(ivAvatar);
                                }else{
                                    ivAvatar.setImageResource(R.drawable.ic_home);
                                }
                                UtilitiesHelper.setRating(user, ratingBar);
                            }
                        }
                    }
                });
    }

    private void setupViewPager(ViewPager viewPager, TabLayout tabs) {
        ApplicationViewPagerAdapter adapter = new ApplicationViewPagerAdapter(getChildFragmentManager(), fragments, fragmentTitles);
        adapter.addFragment(InboxFragment.newInstance(), "Inbox");
        adapter.addFragment(MyWishlistFragment.newInstance(), "My Wishlist");
        adapter.addFragment(MyProductsFragment.newInstance(),"My products");
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }

    private void initViews(View fragmentView) {
        tvFullName = fragmentView.findViewById(R.id.tv_userName);
        tvEmail = fragmentView.findViewById(R.id.tv_email);
        ivAvatar = fragmentView.findViewById(R.id.iv_user_avatar);
        tabs = fragmentView.findViewById(R.id.profile_tabs);
        vpProfilePages = fragmentView.findViewById(R.id.vp_profile_pages);
        ratingBar = fragmentView.findViewById(R.id.rating);
        fabProfile = fragmentView.findViewById(R.id.fab_add_post);
        fabAddProduct = fragmentView.findViewById(R.id.fab_add_product);
        fabAddToWishlist = fragmentView.findViewById(R.id.fab_add_wishlist);
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
}

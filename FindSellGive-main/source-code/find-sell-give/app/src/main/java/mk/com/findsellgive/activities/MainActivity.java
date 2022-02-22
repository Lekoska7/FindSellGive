package mk.com.findsellgive.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.adapters.ApplicationViewPagerAdapter;
import mk.com.findsellgive.fragments.HomeFragment;
import mk.com.findsellgive.fragments.ProfileFragment;
import mk.com.findsellgive.fragments.SaveToCollectionFragment;
import mk.com.findsellgive.fragments.WishlistFragment;
import mk.com.findsellgive.listeners.PermissionDeniedListener;
import mk.com.findsellgive.listeners.PermissionGrantedListener;
import mk.com.findsellgive.services.LocationService;
import mk.com.findsellgive.utills.PermissionsManager;
import mk.com.findsellgive.utills.SharedPreferencesManager;
import mk.com.findsellgive.viewpagers.NonSwipeableViewPager;

import static mk.com.findsellgive.services.LocationService.ACTION_LOCATION_CHANGE;


public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static BottomNavigationView bottomNavigationView;//glavno meni dolu
    public static NonSwipeableViewPager mainPages;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equals(ACTION_LOCATION_CHANGE)) {
                    String lat = intent.getStringExtra("latitude");
                    String lon = intent.getStringExtra("longitude");
                    Log.i("New Location received", lat + "," + lon);
                    SharedPreferencesManager.getInstance(MainActivity.this).setLocation(Double.parseDouble(lon), Double.parseDouble(lat));
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid())
                                .update("location", new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lon)));
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        requestLocationPermission();
        bottomNavigationView = findViewById(R.id.bottomNavigation);//go mapirame menito od layoutot
        mainPages = findViewById(R.id.view_pager);
        mainPages.setOffscreenPageLimit(4);//crash fix
        setupViewPager(mainPages);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver, new IntentFilter("location-access"));
    }

    private void requestLocationPermission() {
        boolean isPermissionEnabled = SharedPreferencesManager.getInstance(this).isLocationPermissionEnabled();
        if (!isPermissionEnabled) {
            PermissionsManager.getInstance(this).requestLocationPermission(new PermissionGrantedListener() {
                @Override
                public void onPermissionGranted() {
                    startService(new Intent(MainActivity.this, LocationService.class));
                }
            }, new PermissionDeniedListener() {
                @Override
                public void onPermissionDenied() {
                    //TODO: Open settings
                }
            });
        } else {
            startService(new Intent(MainActivity.this, LocationService.class));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ApplicationViewPagerAdapter adapter = new ApplicationViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        adapter.addFragment(HomeFragment.newInstance(), "Home");
        adapter.addFragment(WishlistFragment.newInstance(), "Wishlist");
        adapter.addFragment(SaveToCollectionFragment.newInstance(), "Save to Collection");
        adapter.addFragment(ProfileFragment.newInstance(), "Profile");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_main:
                mainPages.setCurrentItem(0);
                return true;
            case R.id.nav_wishlist:
                mainPages.setCurrentItem(1);
                return true;
            case R.id.nav_cart:
                mainPages.setCurrentItem(2);
                return true;
            case R.id.nav_profile:
                mainPages.setCurrentItem(3);
                return true;
            default:
                return true;
        }
    }
}

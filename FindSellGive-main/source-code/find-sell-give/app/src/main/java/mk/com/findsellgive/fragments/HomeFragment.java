package mk.com.findsellgive.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mk.com.findsellgive.R;
import mk.com.findsellgive.adapters.ApplicationViewPagerAdapter;

public class HomeFragment extends Fragment {
    private TabLayout homeTabs;
    private ViewPager homeViewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> fragmentTitles = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.tab_home, container, false);
        homeTabs = homeView.findViewById(R.id.tl_home);
        homeViewPager = homeView.findViewById(R.id.vp_homepages);
        homeViewPager.setOffscreenPageLimit(3);//crash fix
        setupViewPager(homeViewPager, homeTabs);
        return homeView;
    }

    private void setupViewPager(ViewPager viewPager, TabLayout tabs) {
        ApplicationViewPagerAdapter adapter = new ApplicationViewPagerAdapter(getChildFragmentManager(), fragments, fragmentTitles);
        adapter.addFragment(AllProductsFragment.newInstance(), "All");
        adapter.addFragment(OnSaleProductFragment.newInstance(), "On Sale");
        adapter.addFragment(GiveProductFragment.newInstance(), "Give");
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
}

package mk.com.findsellgive.adapters;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ApplicationViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> fragmentTitles;


    public ApplicationViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> fragmentTitles) {
        super(fm);
        this.fragments = fragments;
        this.fragmentTitles = fragmentTitles;
    }

    public void addFragment(Fragment fragment, String title) {
        if (!fragments.contains(fragment)) {
            fragments.add(fragment);
        }
        if (!fragmentTitles.contains(title)) {
            fragmentTitles.add(title);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

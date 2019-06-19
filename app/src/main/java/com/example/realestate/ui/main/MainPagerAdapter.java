package com.example.realestate.ui.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.realestate.ui.main.home.HomeMapFragment;
import com.example.realestate.ui.main.newestlist.ListNewEstateFragment;
import com.example.realestate.ui.main.savedlist.SavedListFragment;
import com.example.realestate.ui.widget.MainTabLayout;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private MainTabLayout mTabs;

    public MainPagerAdapter(FragmentManager fm, MainTabLayout tabs) {
        super(fm);
        mTabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case MainTabLayout.NEWEST_INDEX:
                return ListNewEstateFragment.newInstance();

            case MainTabLayout.SAVED_INDEX:
                return SavedListFragment.newInstance();

            default:
                return HomeMapFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTabs.getCount();
    }

    public int getHomeIndex() {
        return MainTabLayout.HOME_INDEX;
    }

    public String getItemTitle(int i) {
        return mTabs.getTabTitle(i);
    }
}

package com.example.realestate.ui.main;

import com.example.realestate.ui.main.construction.ConstructionFragment;
import com.example.realestate.ui.main.exterior.ExteriorFragment;
import com.example.realestate.ui.main.geomancy.GeomancyFragment;
import com.example.realestate.ui.main.home.HomeMapFragment;
import com.example.realestate.ui.main.listestate.ListNewEstateFragment;
import com.example.realestate.ui.main.savedestate.SavedEstateFragment;
import com.example.realestate.ui.widget.MainTabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
                return SavedEstateFragment.newInstance();

            case MainTabLayout.CONSTRUCTION_INDEX:
                return ConstructionFragment.newInstance();

            case MainTabLayout.EXTERIOR_INDEXT:
                return ExteriorFragment.newInstance();

            case MainTabLayout.GEOMANCY_INDEX:
                return GeomancyFragment.newInstance();

            case MainTabLayout.HOME_INDEX:
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

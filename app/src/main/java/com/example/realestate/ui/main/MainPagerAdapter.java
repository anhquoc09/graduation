package com.example.realestate.ui.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.example.realestate.R;
import com.example.realestate.ui.main.home.HomePagerFragment;
import com.example.realestate.ui.main.home.map.MapFragment;
import com.example.realestate.ui.widget.MainTabLayout;

import java.util.Arrays;
import java.util.List;

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
                return HomePagerFragment.newInstance();

            case MainTabLayout.HOME_INDEX:
            default:
                return HomePagerFragment.newInstance();
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

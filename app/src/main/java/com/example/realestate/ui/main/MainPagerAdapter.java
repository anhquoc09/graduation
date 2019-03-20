package com.example.realestate.ui.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.example.realestate.R;
import com.example.realestate.ui.main.home.HomePagerFragment;
import com.example.realestate.ui.main.home.map.MapFragment;

import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private static final int HOME_INDEX = 0;
    private static final int PROFILE_INDEX = 1;

    private final List<String> mTitleArray;

    private final TypedArray mIconArray;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mTitleArray = Arrays.asList(context.getApplicationContext().getResources().getStringArray(R.array.navigation_title));
        mIconArray = context.getApplicationContext().getResources().obtainTypedArray(R.array.navigation_icon);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case PROFILE_INDEX:
                return HomePagerFragment.newInstance();

            case HOME_INDEX:
            default:
                return HomePagerFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTitleArray.size();
    }

    public int getHomeIndex() {
        return HOME_INDEX;
    }

    public Drawable getItemIcon(int i) {
        return mIconArray.getDrawable(i);
    }

    public String getItemTitle(int i) {
        return mTitleArray.get(i);
    }
}

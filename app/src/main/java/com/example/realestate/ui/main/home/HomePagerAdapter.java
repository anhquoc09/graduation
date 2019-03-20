package com.example.realestate.ui.main.home;

import android.content.Context;
import android.view.ViewGroup;

import com.example.realestate.R;
import com.example.realestate.ui.main.home.map.MapFragment;
import com.example.realestate.ui.main.home.listpost.ListEstateFragment;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = HomePagerAdapter.class.getSimpleName();

    private static final int MAP_INDEX = 0;

    private static final int LIST_INDEX = 1;

    private final List<String> mTitleArray;

    private MapFragment.OnCallBackListener mMapListener;

    private ListEstateFragment.OnCallBackListener mListListener;

    private final SparseArrayCompat<WeakReference<Fragment>> mHolder;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mTitleArray = Arrays.asList(context.getApplicationContext().getResources().getStringArray(R.array.home_title));
        mHolder = new SparseArrayCompat<>(mTitleArray.size());
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case LIST_INDEX:
                return ListEstateFragment.newInstance(mListListener);

            case MAP_INDEX:
            default:
                return MapFragment.newInstance(mMapListener);
        }
    }

    @Override
    public int getCount() {
        return mTitleArray.size();
    }

    public void setMapListener(MapFragment.OnCallBackListener mapListener) {
        mMapListener = mapListener;
    }

    public void setListListener(ListEstateFragment.OnCallBackListener listListener) {
        mListListener = listListener;
    }

    public String getTitle(int position) {
        return mTitleArray.get(position);
    }

    public int getDefaultIndex() {
        return MAP_INDEX;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object item = super.instantiateItem(container, position);
        if (item instanceof Fragment) {
            mHolder.put(position, new WeakReference<>((Fragment) item));
        }
        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mHolder.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getPage(int position) {
        final WeakReference<Fragment> weakRefItem = mHolder.get(position);
        return (weakRefItem != null) ? weakRefItem.get() : null;
    }
}

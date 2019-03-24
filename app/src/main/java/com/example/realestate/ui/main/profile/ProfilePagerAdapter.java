package com.example.realestate.ui.main.profile;

import android.content.Context;
import android.view.ViewGroup;

import com.example.realestate.R;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProfilePagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = ProfilePagerAdapter.class.getSimpleName();

    private static final int DETAIL_INDEX = 0;

    private static final int ACTIVITIES_INDEX = 1;

    private final List<String> mTitleArray;

    private ProfileTabDetailFragment.OnCallBackListener mDetailListener;

    private ProfileTabPostFragment.OnCallBackListener mListListener;

    private final SparseArrayCompat<WeakReference<Fragment>> mHolder;

    public ProfilePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mTitleArray = Arrays.asList(context.getApplicationContext().getResources().getStringArray(R.array.profile_title));
        mHolder = new SparseArrayCompat<>(mTitleArray.size());
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case ACTIVITIES_INDEX:
                return ProfileTabPostFragment.newInstance(mListListener);

            case DETAIL_INDEX:
            default:
                return ProfileTabDetailFragment.newInstance(mDetailListener);
        }
    }

    @Override
    public int getCount() {
        return mTitleArray.size();
    }

    public void setDetailListener(ProfileTabDetailFragment.OnCallBackListener detailListener) {
        mDetailListener = detailListener;
    }

    public void setListListener(ProfileTabPostFragment.OnCallBackListener listListener) {
        mListListener = listListener;
    }

    public String getTitle(int position) {
        return mTitleArray.get(position);
    }

    public int getDefaultIndex() {
        return DETAIL_INDEX;
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

package com.reales.realestate.ui.widget;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * @author anhquoc09
 * @since 08/05/2019
 */

public class InfinitePagerAdapter extends PagerAdapter {

    private PagerAdapter mOriginalAdapter;

    public InfinitePagerAdapter(PagerAdapter originalAdapter) {
        super();
        mOriginalAdapter = originalAdapter;
    }

    public int getRealCount() {
        return mOriginalAdapter == null ? 0 : mOriginalAdapter.getCount();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (getRealPosition(position) != POSITION_NONE) {
            mOriginalAdapter.destroyItem(container, getRealPosition(position), object);
        }
    }

    public int getRealPosition(int position) {
        if (getRealCount() == 0) {
            return POSITION_NONE;
        }
        return position % getRealCount();
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (getRealPosition(position) == POSITION_NONE) {
            return null;
        }
        return mOriginalAdapter.instantiateItem(container, getRealPosition(position));
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (mOriginalAdapter == null) {
            super.saveState();
        } else {
            mOriginalAdapter.restoreState(state, loader);
        }
    }

    @Override
    public Parcelable saveState() {
        if (mOriginalAdapter == null) {
            return super.saveState();
        }
        return mOriginalAdapter.saveState();
    }

    public PagerAdapter getAdapter() {
        return mOriginalAdapter;
    }
}

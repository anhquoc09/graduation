package com.example.realestate.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.realestate.R;
import com.google.android.gms.common.util.CollectionUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author anhquoc09
 * @since 08/05/2019
 */

public class ImageSliderLayout extends FrameLayout {

    private static final long DEFAULT_SLIDE_DURATION = 2000;

    private ViewPager mViewPager;

    private PagerIndicator mPagerIndicator;

    private long mAutoSlideDuration = DEFAULT_SLIDE_DURATION;

    private boolean mAutoSlide = false;

    private AutoSlideTask mAutoSlideTask;

    public ImageSliderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageSliderLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.slider_layout, this);
        mViewPager = findViewById(R.id.pager);
        mPagerIndicator = findViewById(R.id.pagerIndicator);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageSliderLayout);
        try {
            mAutoSlide = a.getBoolean(R.styleable.ImageSliderLayout_autoSlide, false);
            mAutoSlideDuration = a.getInt(R.styleable.ImageSliderLayout_autoSlideDuration, (int) DEFAULT_SLIDE_DURATION);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoSlide();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoSlide();
    }

    public void setAdapter(PagerAdapter adapter) {
        mViewPager.setAdapter(adapter);
        mPagerIndicator.setViewPager(mViewPager);
    }

    public void startAutoSlide() {
        if (mAutoSlide) {
            mAutoSlideTask = new AutoSlideTask(this);
            postDelayed(mAutoSlideTask, mAutoSlideDuration);
        }
    }

    public void stopAutoSlide() {
        if (mAutoSlideTask != null) {
            removeCallbacks(mAutoSlideTask);
        }
    }

    public void enableAutoSlide() {
        mAutoSlide = true;
    }

    public void disableAutoSlide() {
        mAutoSlide = false;
    }

    public void checkIfShowIndicator(List<String> data) {
        if (data.size() < 2) {
            disableAutoSlide();
            mPagerIndicator.setVisibility(GONE);
        } else {
            enableAutoSlide();
            mPagerIndicator.setVisibility(VISIBLE);
        }
    }

    private void slide() {
        if (mViewPager != null) {
            int next = mViewPager.getCurrentItem() + 1;
            mViewPager.setCurrentItem(next, true);
            if (mAutoSlide && mAutoSlideTask != null) {
                postDelayed(mAutoSlideTask, mAutoSlideDuration);
            }
        }
    }

    /**
     * {@link AutoSlideTask}
     */
    private static final class AutoSlideTask implements Runnable {

        private WeakReference<ImageSliderLayout> mRef;

        public AutoSlideTask(ImageSliderLayout imageSliderLayout) {
            mRef = new WeakReference<>(imageSliderLayout);
        }

        @Override
        public void run() {
            ImageSliderLayout imageSliderLayout = mRef.get();
            if (imageSliderLayout != null) {
                imageSliderLayout.slide();
            }
        }
    }
}

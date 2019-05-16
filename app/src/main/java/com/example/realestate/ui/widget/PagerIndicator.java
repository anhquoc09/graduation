package com.example.realestate.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.realestate.R;

/**
 * @author anhquoc09
 * @since 08/05/2019
 */

public class PagerIndicator extends View {

    private int mUnselectedRadius;

    private int mSelectedRadius;

    private int mMargin;

    private int mSelectedColor;

    private int mUnselectedColor;

    private Paint mPaint;

    private int mCount = 0;

    private int mSelectedPosition = 0;

    private ViewPager mViewPager;

    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            if (mViewPager == null) {
                return;
            }

            PagerAdapter adapter = mViewPager.getAdapter();
            if (adapter != null) {
                int selectedPos;
                if (adapter instanceof InfinitePagerAdapter) {
                    selectedPos = ((InfinitePagerAdapter) adapter).getRealPosition(position);
                } else {
                    selectedPos = position;
                }
                if (mSelectedPosition != selectedPos) {
                    mSelectedPosition = selectedPos;
                    invalidate();
                }
            }
        }
    };

    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            updateCount();
        }

        @Override
        public void onInvalidated() {
        }
    };


    public PagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        float density = context.getResources().getDisplayMetrics().density;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);
        try {
            mMargin = a.getDimensionPixelSize(R.styleable.PagerIndicator_margin, (int) (4 * density));
            mUnselectedRadius = a.getDimensionPixelSize(R.styleable.PagerIndicator_radius, (int) (3 * density));
            mSelectedRadius = a.getDimensionPixelSize(R.styleable.PagerIndicator_selectedRadius, mUnselectedRadius);
            mSelectedColor = a.getColor(R.styleable.PagerIndicator_selectedColor, Color.WHITE);
            mUnselectedColor = a.getColor(R.styleable.PagerIndicator_unselectedColor, Color.rgb(128, 128, 128));
        } finally {
            a.recycle();
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter != null) {
            if (adapter instanceof InfinitePagerAdapter) {
                InfinitePagerAdapter infiniteAdapter = (InfinitePagerAdapter) adapter;
                mCount = infiniteAdapter.getRealCount();
                PagerAdapter originalAdapter = infiniteAdapter.getAdapter();
                if (originalAdapter != null) {
                    originalAdapter.registerDataSetObserver(mDataSetObserver);
                }
            } else {
                mCount = adapter.getCount();
                adapter.registerDataSetObserver(mDataSetObserver);
            }
            requestLayout();
        }
    }

    private void updateCount() {
        if (mViewPager == null) {
            mCount = 0;
        } else {
            PagerAdapter adapter = mViewPager.getAdapter();
            if (adapter != null) {
                if (adapter instanceof InfinitePagerAdapter) {
                    mCount = ((InfinitePagerAdapter) adapter).getRealCount();
                } else {
                    mCount = adapter.getCount();
                }
            } else {
                mCount = 0;
            }
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getPaddingLeft() + getPaddingRight();
        int height = getPaddingTop() + getPaddingBottom();
        if (mCount > 0) {
            int unSelectedDiameter = 2 * mUnselectedRadius;
            int selectedDiameter = 2 * mSelectedRadius;
            width += (mCount - 1) * (unSelectedDiameter + mMargin) + selectedDiameter;
            height += selectedDiameter;
        }
        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCount > 0) {
            int startX = getPaddingStart();
            int startY = getPaddingTop();
            int radius;
            int diameter = 2 * mUnselectedRadius;
            for (int i = 0; i < mCount; i++) {
                radius = i == mSelectedPosition ? mSelectedRadius : mUnselectedRadius;
                mPaint.setColor(i == mSelectedPosition ? mSelectedColor : mUnselectedColor);
                canvas.drawCircle(startX + mSelectedRadius, startY + mSelectedRadius, radius, mPaint);
                startX += diameter + mMargin;
            }
        }
    }
}

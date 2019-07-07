package com.reales.realestate.ui.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.reales.realestate.R;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anhquoc09
 * @since 08/05/2019
 */

public class ImageSliderAdapter extends PagerAdapter {

    private List<String> mData;

    private OnItemClickListener mItemClickListener;

    public ImageSliderAdapter(List<String> data) {
        super();
        setData(data);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final Context context = container.getContext();
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                if (0 <= position && position < getCount()) {
                    mItemClickListener.onClick(mData.get(position), position);
                }
            }
        });

        Glide.with(context)
                .load(mData.get(position))
                .placeholder(R.color.silver)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);

        container.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setData(List<String> data) {
        if (CollectionUtils.isEmpty(data)) {
            mData = new ArrayList<>();
        } else {
            mData = new ArrayList<>(data);
        }
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * {@link OnItemClickListener}
     */
    public interface OnItemClickListener {
        void onClick(String data, int position);
    }
}

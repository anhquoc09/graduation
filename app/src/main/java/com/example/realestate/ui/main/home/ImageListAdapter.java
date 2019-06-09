package com.example.realestate.ui.main.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.realestate.R;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by quocha2
 * On 06/06/2019
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ItemViewHolder> {

    private final List<String> mUrlList = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.image_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindView(mUrlList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUrlList.size();
    }

    public void setData(List<String> urlList) {
        mUrlList.clear();
        if (!CollectionUtils.isEmpty(urlList)) {
            mUrlList.addAll(urlList);
        }
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView mImageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(String url) {
            if (!TextUtils.isEmpty(url)) {
                CircularProgressDrawable progressDrawable = new CircularProgressDrawable(itemView.getContext());
                progressDrawable.setStrokeWidth(5f);
                progressDrawable.setCenterRadius(40f);
                progressDrawable.setColorSchemeColors(R.color.colorAccent);
                progressDrawable.start();

                Glide.with(itemView.getContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(progressDrawable)
                        .into(mImageView);
            }
        }
    }
}

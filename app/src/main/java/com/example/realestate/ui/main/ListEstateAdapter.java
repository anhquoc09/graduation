package com.example.realestate.ui.main;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.realestate.EstateApplication;
import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.utils.AndroidUtilities;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class ListEstateAdapter extends RecyclerView.Adapter<ListEstateAdapter.EstateItemVH> {

    private final int mImageSize;

    private final List<EstateDetail> mList = new ArrayList<>();

    private OnItemClickListener mItemClickListener;

    public ListEstateAdapter() {
        Resources resources = EstateApplication.getInstance().getResources();
        int size = resources.getDimensionPixelSize(R.dimen.list_estate_image_size);
        mImageSize = size == 0 ? AndroidUtilities.dp(41) : size;
    }

    @NonNull
    @Override
    public EstateItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_estate_item_layout, parent, false);
        return new EstateItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstateItemVH holder, int position) {
        EstateDetail item = mList.get(position);
        if (item != null) {
            holder.bindView(item);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<EstateDetail> list) {
        mList.clear();
        if (!CollectionUtils.isEmpty(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    /**
     * {@link EstateDetail}
     */
    public class EstateItemVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        ImageView mImage;

        @BindView(R.id.title)
        TextView mTitle;

        @BindView(R.id.time)
        TextView mTime;

        @BindView(R.id.price)
        TextView mPrice;

        @BindView(R.id.status)
        TextView mStatus;

        @BindView(R.id.poster)
        TextView mPoster;

        public EstateItemVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemSelected(getItem());
        }

        private EstateDetail getItem() {
            if (mList.get(getAdapterPosition()) != null) {
                return mList.get(getAdapterPosition());
            }
            return null;
        }

        public void setImage(String imageView) {
            if (!TextUtils.isEmpty(imageView)) {
                Glide.with(itemView.getContext())
                        .load(imageView)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .dontAnimate()
                        .placeholder(R.color.silver)
                        .override(mImageSize)
                        .into(mImage);
            }
        }

        public void setTitle(String title) {
            mTitle.setText(TextUtils.isEmpty(title) ? "" : title);
        }

        public void setTime(String time) {
            mTime.setText(TextUtils.isEmpty(time) ? "" : time);
        }

        public void setPrice(String price) {
            mPrice.setText(TextUtils.isEmpty(price) ? "" : price);
        }

        public void setStatus(String status) {
            mStatus.setText(TextUtils.isEmpty(status) ? "" : status);
        }

        public void setPoster(String poster) {
            mPoster.setText(TextUtils.isEmpty(poster) ? "" : poster);
        }

        public void bindView(EstateDetail item) {
            if (item != null) {
                setImage(item.getImageUrl());
                setTitle(item.getTitle());
                setTime(item.getTime());
                setPrice(item.getPrice());
                setStatus(item.getStatus());
                setPoster(item.getOwnerDisplayName());
            }
        }
    }

    /**
     * {@link OnItemClickListener}
     */
    public interface OnItemClickListener {
        void onItemSelected(EstateDetail item);
    }
}

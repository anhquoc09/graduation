package com.example.realestate.ui.main.geomancy;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realestate.EstateApplication;
import com.example.realestate.R;
import com.example.realestate.data.model.GeomancyDetail;
import com.example.realestate.utils.AndroidUtilities;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

/**
 * @author anhquoc09
 * @since 30/03/2019
 */

public class GeomancyAdapter extends RecyclerView.Adapter<GeomancyAdapter.GeomancyItemVH>{
    private final int mImageSize;

    private final List<GeomancyDetail> mList = new ArrayList<>();

    private OnItemClickListener mItemClickListener;

    public GeomancyAdapter() {
        Resources resources = EstateApplication.getInstance().getResources();
        int size = resources.getDimensionPixelSize(R.dimen.list_estate_image_size);
        mImageSize = size == 0 ? AndroidUtilities.dp(41) : size;
    }

    @NonNull
    @Override
    public GeomancyItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_exterior_item_layout, parent, false);
        return new GeomancyItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeomancyItemVH holder, int position) {
        GeomancyDetail item = mList.get(position);
        if (item != null) {
            holder.bindView(item);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<GeomancyDetail> list) {
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
     * {@link GeomancyItemVH}
     */
    public class GeomancyItemVH extends RecyclerView.ViewHolder implements View.OnClickListener {

//        @BindView(R.id.image)
//        ImageView mImage;
//
//        @BindView(R.id.title)
//        TextView mTitle;
//
//        @BindView(R.id.time)
//        TextView mTime;
//
//        @BindView(R.id.price)
//        TextView mPrice;
//
//        @BindView(R.id.status)
//        TextView mStatus;
//
//        @BindView(R.id.poster)
//        TextView mPoster;

        public GeomancyItemVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemSelected(getItem());
        }

        private GeomancyDetail getItem() {
            if (mList.get(getAdapterPosition()) != null) {
                return mList.get(getAdapterPosition());
            }
            return null;
        }

        //        public void setImage(String imageView) {
//            if (!TextUtils.isEmpty(imageView)) {
//                Glide.with(itemView.getContext())
//                        .load(imageView)
//                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                        .dontAnimate()
//                        .placeholder(R.color.silver)
//                        .override(mImageSize)
//                        .into(mImage);
//            }
//        }
//
//        public void setTitle(String title) {
//            mTitle.setText(TextUtils.isEmpty(title) ? "" : title);
//        }
//
//        public void setTime(String time) {
//            mTime.setText(TextUtils.isEmpty(time) ? "" : time);
//        }
//
//        public void setPrice(String price) {
//            mPrice.setText(TextUtils.isEmpty(price) ? "" : price);
//        }
//
//        public void setStatus(String status) {
//            mStatus.setText(TextUtils.isEmpty(status) ? "" : status);
//        }
//
//        public void setPoster(String poster) {
//            mPoster.setText(TextUtils.isEmpty(poster) ? "" : poster);
//        }
//
        public void bindView(GeomancyDetail item) {
            if (item != null) {

            }
        }
    }

    /**
     * {@link OnItemClickListener}
     */
    public interface OnItemClickListener {
        void onItemSelected(GeomancyDetail item);
    }
}

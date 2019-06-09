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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class ListEstateAdapter extends RecyclerView.Adapter<ListEstateAdapter.EstateItemVH> {

    private final List<EstateDetail> mList = new ArrayList<>();

    private OnItemClickListener mItemClickListener;

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

    public void appendData(List<EstateDetail> data) {
        int index = mList.size();
        if (!CollectionUtils.isEmpty(data)) {
            mList.addAll(data);
            notifyItemRangeInserted(index, data.size());
        }
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

        @BindView(R.id.estate_type)
        TextView mType;

        @BindView(R.id.address)
        TextView mAddress;

        @BindView(R.id.owner)
        TextView mName;

        @BindView(R.id.btn_save)
        ImageView mBtnSave;

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

        public void setImage(List<String> imageList) {
            if (!CollectionUtils.isEmpty(imageList)) {
                Glide.with(itemView)
                        .load(imageList.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(R.color.silver)
                        .into(mImage);
            }
        }

        private void setEstateType(int estateType, int estateStatus) {
            Context context = EstateApplication.getInstance().getApplicationContext();

            String string = "";

            switch (estateStatus) {
                case 1:
                    string += context.getString(R.string.estate_sell);
                    break;
                case 3:
                    string += context.getString(R.string.estate_lease);
                    break;
            }

            switch (estateType) {
                case 1:
                    string += " " + (context.getString(R.string.estate_apartment));
                    break;
                case 2:
                    string += " " + (context.getString(R.string.estate_house));
                    break;
                case 3:
                    string += " " + (context.getString(R.string.estate_land));
                    break;
                case 4:
                    string += " " + (context.getString(R.string.estate_office));
                    break;
                default:
                    break;
            }

            mType.setText(string);
        }

        private void setOwnerName(String ownerName) {
            mName.setText(ownerName);
        }

        private void setEstateTitle(String estateTitle) {
            mTitle.setText(estateTitle);
            mTitle.setSelected(true);
        }

        private void setEstatePrice(float estatePrice) {
            mPrice.setText(String.format("%s VNĐ", NumberFormat.getNumberInstance(Locale.getDefault()).format(estatePrice * 1000000)));
        }

        private void setEstateTimePost(long estateDayPost) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = new Date(estateDayPost * 1000);
            mTime.setText(String.format("%s ", dateFormat.format(date)));
        }

        private void setBtnSave() {
            mBtnSave.setVisibility(View.GONE);
        }

        private void setAddress(String address) {
            mAddress.setText(address);
            mAddress.setSelected(true);
        }

        public void bindView(EstateDetail item) {
            if (item != null) {
                setImage(item.getUrl());
                setEstateTitle(item.getName());
                setEstatePrice(item.getPrice());
                setEstateType(item.getType(), item.getStatusProject());
                setAddress(item.getAddress());
                setOwnerName(item.getFullName());
                setEstateTimePost(item.getCreateTime());
                setBtnSave();
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

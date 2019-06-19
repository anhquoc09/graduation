package com.example.realestate.ui.main.home;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.realestate.EstateApplication;
import com.example.realestate.R;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.login.LoginActivity;
import com.google.android.gms.common.util.CollectionUtils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.realestate.utils.AndroidUtilities.getString;

/**
 * Created by quocha2
 * On 06/06/2019
 */
public class HomeMapEstateListAdapter extends RecyclerView.Adapter<HomeMapEstateListAdapter.ItemViewHolder> {

    private final List<EstateDetail> mList = new ArrayList<>();

    private OnItemClickListener mItemClickListener;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.home_list_estate_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
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

    public void saveSuccess(int position) {
        if (position >= 0 && position < mList.size()) {
            notifyItemChanged(position);
        }
    }

    /**
     * {@link ItemViewHolder}
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.list_estate)
        RecyclerView mImages;

        @BindView(R.id.estate_status)
        TextView mStatus;

        @BindView(R.id.estate_type)
        TextView mType;

        @BindView(R.id.avatar)
        ImageView mAvatar;

        @BindView(R.id.estate_title)
        TextView mTitle;

        @BindView(R.id.name)
        TextView mName;

        @BindView(R.id.estate_price)
        TextView mPrice;

        @BindView(R.id.btn_save)
        ImageView mBtnSave;

        @BindView(R.id.time)
        TextView mTime;

        @BindView(R.id.estate_address)
        TextView mAddress;

        @BindView(R.id.estate_square)
        TextView mSquare;

        private EstateDetail mEstateDetail;

        private ImageListAdapter mAdapter = new ImageListAdapter();

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false);
            mImages.setLayoutManager(layoutManager);
            mImages.setAdapter(mAdapter);
            RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, MotionEvent e) {
                    int action = e.getAction();
                    if (action == MotionEvent.ACTION_MOVE) {
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            };

            mImages.addOnItemTouchListener(mScrollTouchListener);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemSelected(getItem());
            }
        }

        private EstateDetail getItem() {
            if (mList.get(getAdapterPosition()) != null) {
                return mList.get(getAdapterPosition());
            }
            return null;
        }

        private void setImageList(List<String> urls) {
            mAdapter.setData(urls);
        }

        private void setStatus(int status) {
            switch (status) {
                case 1:
                    mStatus.setText(itemView.getResources().getString(R.string.estate_sell));
                    break;
                case 3:
                    mStatus.setText(itemView.getResources().getString(R.string.estate_lease));
                    break;
            }
        }

        private void setType(int type) {
            switch (type) {
                case 1:
                    mType.setText(itemView.getResources().getString(R.string.estate_apartment));
                    break;
                case 2:
                    mType.setText(itemView.getResources().getString(R.string.estate_house));
                    break;
                case 3:
                    mType.setText(itemView.getResources().getString(R.string.estate_land));
                    break;
                case 4:
                    mType.setText(itemView.getResources().getString(R.string.estate_office));
                    break;
            }
        }

        private void setAvatar(String avatar) {
            Glide.with(itemView)
                    .load(avatar)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.color.silver)
                    .error(R.drawable.avatar_default_small)
                    .into(mAvatar);
        }

        private void setTitle(String title) {
            mTitle.setText(title);
            mTitle.setSelected(true);
        }

        private void setName(String fullName) {
            mName.setText(fullName);
        }

        private void setSaved() {
            mBtnSave.setVisibility(View.VISIBLE);
            mBtnSave.setSelected(EstateApplication.savedContain(mEstateDetail.getId()));
        }

        private void setTime(long createTime) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = new Date(createTime * 1000);
            mTime.setText(String.format("%s ", dateFormat.format(date)));
        }

        private void setPrice(float price, int status) {
            String unit;
            if (status == 1) {
                unit = getString(R.string.sell_unit);
            } else {
                unit = getString(R.string.lease_unit);
            }
            mPrice.setText(String.format("%s %s", NumberFormat.getNumberInstance(Locale.getDefault()).format(price * 1000000), unit));
        }

        private void setSquare(float square) {
            mSquare.setText(String.format("%s m2", NumberFormat.getNumberInstance(Locale.getDefault()).format(square)));
        }


        private void setAddress(String address) {
            mAddress.setText(address);
            mAddress.setSelected(true);
        }

        public void bindView(EstateDetail item) {
            mEstateDetail = item;
            if (item != null) {
                setImageList(item.getUrl());
                setStatus(item.getStatusPost());
                setType(item.getType());
                setAvatar(item.getAvatar());
                setTitle(item.getName());
                setName(item.getFullName());
                setPrice(item.getPrice(), item.getStatusPost());
                setSaved();
                setTime(item.getCreateTime());
                setSquare(item.getArea());
                setAddress(item.getAddress());
            }
        }

        @OnClick(R.id.avatar)
        public void onAvatarClick() {
            if (mItemClickListener != null && mEstateDetail != null) {
                mItemClickListener.onAvatarClick(mEstateDetail.getOwnerid());
            }
        }

        @OnClick(R.id.btn_save)
        public void onSaveClick() {
            if (!UserManager.isUserLoggedIn()) {
                AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext()).create();
                alertDialog.setTitle(getString(R.string.not_login));
                alertDialog.setMessage(getString(R.string.login_now));

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_cancel), (dialog, which) -> alertDialog.dismiss());

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.login), (dialog, which) ->
                        itemView.getContext().startActivity(LoginActivity.intentFor(itemView.getContext(), true)));

                alertDialog.setCancelable(true);
                alertDialog.show();

            } else {
                if (!mBtnSave.isSelected()) {
                    mBtnSave.setSelected(true);
                    mItemClickListener.savePost(mEstateDetail, getAdapterPosition());
                } else {
                    mBtnSave.setSelected(false);
                    mItemClickListener.unSavePost(mEstateDetail, getAdapterPosition());
                }
            }
        }
    }

    /**
     * {@link OnItemClickListener}
     */
    public interface OnItemClickListener {
        void onItemSelected(EstateDetail item);

        void onAvatarClick(String userId);

        void savePost(EstateDetail item, int position);

        void unSavePost(EstateDetail item, int position);
    }
}

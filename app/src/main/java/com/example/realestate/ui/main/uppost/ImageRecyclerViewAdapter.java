package com.example.realestate.ui.main.uppost;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.realestate.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder> {

    private final List<Uri> mImageUriList = new ArrayList<>();

    private OnImageClickListener mListener;

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_item_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bindView(mImageUriList.get(position));
    }

    @Override
    public int getItemCount() {
        return (mImageUriList.size());
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        mListener = listener;
    }

    public void addImage(Uri imageUri) {
        if (imageUri != null) {
            mImageUriList.add(imageUri);
        }
        notifyItemInserted(mImageUriList.size());
    }

    public void setImageList(List<Uri> imageUrlList) {
        mImageUriList.clear();
        if (!imageUrlList.isEmpty()) {
            mImageUriList.addAll(imageUrlList);
        }
        notifyDataSetChanged();
    }

    public void removeImage(int position) {
        if (position != RecyclerView.NO_POSITION) {
            mImageUriList.remove(position);
            notifyItemRemoved(position);
            if (mListener != null) {
                mListener.onDeleteImageClick(position);
            }
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo)
        ImageView mPhotoImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Uri uri) {
            if (uri != null) {
                Glide.with(itemView)
                        .load(uri)
                        .placeholder(R.color.silver)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mPhotoImageView);
            }
        }

        @OnClick(R.id.iv_photo)
        public void onViewClick() {

            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (mListener != null) {
                    mListener.onViewImageClick(mImageUriList, position);
                }
            }
        }

        @OnClick(R.id.iv_delete_photo)
        public void onDeleteClick() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                removeImage(position);
            }
        }
    }

    public interface OnImageClickListener {
        void onViewImageClick(List<Uri> imageUrlList, int position);

        void onDeleteImageClick(int position);
    }
}

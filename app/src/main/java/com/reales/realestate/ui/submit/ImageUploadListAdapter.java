package com.reales.realestate.ui.submit;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.reales.realestate.R;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageUploadListAdapter extends RecyclerView.Adapter<ImageUploadListAdapter.ImageViewHolder> {

    private final List<String> mUrlList = new ArrayList<>();

    private final List<ImageInfoItem> mImageList = new ArrayList<>();

    private OnImageClickListener mListener;

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_item_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (position < mUrlList.size()) {
            holder.bindView(mUrlList.get(position));
        } else {
            holder.bindView(mImageList.get(position - mUrlList.size()));
        }
    }

    @Override
    public int getItemCount() {
        return (mUrlList.size() + mImageList.size());
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        mListener = listener;
    }

    public void addImage(Uri imageUri) {
        if (imageUri != null) {
            mImageList.add(new ImageInfoItem(imageUri));
        }
        notifyItemInserted(mImageList.size());
    }

    public void setImageList(List<Uri> imageUriList) {
        mImageList.clear();
        if (!imageUriList.isEmpty()) {
            for (int i = 0; i < imageUriList.size(); i++) {
                mImageList.add(new ImageInfoItem(imageUriList.get(i)));
            }
        }
        notifyDataSetChanged();
    }

    public void setUrlList(List<String> url) {
        mUrlList.clear();
        if (!CollectionUtils.isEmpty(url)) {
            mUrlList.addAll(url);
        }
        notifyDataSetChanged();
    }

    public void removeImage(int position) {
        if (position != RecyclerView.NO_POSITION) {
            if (position < mUrlList.size()) {
                mUrlList.remove(position);
            } else {
                mImageList.remove(position - mUrlList.size());
            }
            notifyItemRemoved(position);
            if (mListener != null) {
                if (position < mUrlList.size()) {
                    mListener.onDeleteImageUrl(position);
                } else {
                    mListener.onDeleteImageUri(position - mUrlList.size());
                }
            }
        }
    }

    public void uploadError(int position) {
        mImageList.get(position).setUploading(false);
        mImageList.get(position).setUploadError(true);
        notifyItemChanged(position + mUrlList.size());
    }

    public void startUpload(int position) {
        mImageList.get(position).setUploading(true);
        notifyItemChanged(position + mUrlList.size());
    }

    public void uploadImageSuccess(int position) {
        mImageList.get(position).setUploading(false);
        mImageList.get(position).setUploadSuccess(true);
        notifyItemChanged(position + mUrlList.size());
    }

    public void updatePercent(int percent, int position) {
        mImageList.get(position).setUploadPercent(percent);
        notifyItemChanged(position + mUrlList.size());
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo)
        ImageView mPhotoImageView;

        @BindView(R.id.ic_error)
        ImageView mIconError;

        @BindView(R.id.progress)
        ProgressBar mProgressBar;

        @BindView(R.id.iv_delete_photo)
        ImageView mIconDeletePhoto;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(ImageInfoItem image) {
            if (image != null) {
                Glide.with(itemView)
                        .load(image.getUri())
                        .placeholder(R.color.silver)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mPhotoImageView);

                if (image.isUploadSuccess()) {
                    mPhotoImageView.setAlpha(1f);
                    mIconDeletePhoto.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                    mIconError.setVisibility(View.GONE);

                } else if (image.isUploading()) {
                    mPhotoImageView.setAlpha(0.4f);
                    mProgressBar.setVisibility(View.VISIBLE);
                    mIconDeletePhoto.setVisibility(View.GONE);
                    mIconError.setVisibility(View.GONE);
                    mProgressBar.setProgress(image.getUploadPercent());

                } else if (image.isUploadError()) {
                    mPhotoImageView.setAlpha(0.4f);
                    mProgressBar.setVisibility(View.GONE);
                    mIconDeletePhoto.setVisibility(View.VISIBLE);
                    mIconError.setVisibility(View.VISIBLE);
                }
            }
        }

        public void bindView(String url) {
            Glide.with(itemView)
                    .load(url)
                    .placeholder(R.color.silver)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mPhotoImageView);
        }

        @OnClick(R.id.iv_photo)
        public void onViewClick() {

            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (mListener != null) {
                    mListener.onViewImageClick(mImageList, position);
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
        void onViewImageClick(List<ImageInfoItem> imageList, int position);

        void onDeleteImageUri(int position);

        void onDeleteImageUrl(int position);
    }
}

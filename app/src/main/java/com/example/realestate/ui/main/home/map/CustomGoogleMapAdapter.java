package com.example.realestate.ui.main.home.map;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.utils.AndroidUtilities;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomGoogleMapAdapter implements GoogleMap.InfoWindowAdapter {

    @BindView(R.id.estate_image)
    ImageView mEstateImage;

    @BindView(R.id.estate_title)
    TextView mEstateTitle;

    @BindView(R.id.estate_price)
    TextView mEstatePrice;

    @BindView(R.id.estate_address)
    TextView mEstateAddress;

    @BindView(R.id.estate_square)
    TextView mEstateSquare;

    @BindView(R.id.estate_avatar)
    ImageView mOwnerAvatar;

    @BindView(R.id.estate_display_name)
    TextView mOwnerName;

    private Context mContext;

    private EstateDetail mEstateDetail;

    public CustomGoogleMapAdapter(Context context) {
        mContext = context;
    }

    private void bindData() {
        if (mEstateDetail != null) {
            Glide.with(mContext)
                    .load(mEstateDetail.getImageUrl())
                    .placeholder(R.color.silver)
                    .override(AndroidUtilities.dp(300), AndroidUtilities.dp(160))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(mEstateImage);

            Glide.with(mContext)
                    .load(mEstateDetail.getOwnerAvatar())
                    .placeholder(R.drawable.avatar_default_small)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(mOwnerAvatar);
            mOwnerName.setText(mEstateDetail.getOwnerDisplayName());
            mEstateTitle.setText(mEstateDetail.getTitle());
            mEstatePrice.setText(mEstateDetail.getPrice());
            mEstateAddress.setText(mEstateDetail.getAddress());
            mEstateSquare.setText(mEstateDetail.getSquare());
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.map_custom_infowindow, null);
        ButterKnife.bind(this, view);

        mEstateDetail = (EstateDetail) marker.getTag();
        bindData();
        return view;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

}
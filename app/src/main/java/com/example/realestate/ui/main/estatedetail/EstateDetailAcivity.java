package com.example.realestate.ui.main.estatedetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.BaseActivity;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EstateDetailAcivity extends BaseActivity {
    public static final String TAG = EstateDetailAcivity.class.getCanonicalName();

    public static final String ESTATE_DETAIL = "estate_detail";

    @BindView(R.id.estate_avatar)
    ImageView mOwnerAvatar;

    @BindView(R.id.estate_display_name)
    TextView mOwnerName;

    @BindView(R.id.estate_province)
    TextView mOwnerProvince;

    @BindView(R.id.estate_image)
    ImageView mEstateImage;

    @BindView(R.id.estate_title)
    TextView mEstateTitle;

    @BindView(R.id.estate_price)
    TextView mEstatePrice;

    @BindView(R.id.estate_day_post)
    TextView mEstateDayPost;

    @BindView(R.id.estate_address)
    TextView mEstateAddress;

    @BindView(R.id.estate_type)
    TextView mEstateType;

    @BindView(R.id.estate_square)
    TextView mEstateSquare;

    @BindView(R.id.estate_contact)
    TextView mEstateContact;

    @BindView(R.id.estate_description)
    TextView mEstateDescription;

    @BindView(R.id.btn_contact)
    View mBtnContact;

    private EstateDetail mEstateDetail;

    private Unbinder mUnbinder;

    public static Intent intentFor(Context context, EstateDetail estateDetail) {
        Intent intent = new Intent();
        intent.putExtra(ESTATE_DETAIL, estateDetail);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_estate_detail_layout);
        mUnbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        mEstateDetail = (EstateDetail) intent.getSerializableExtra(ESTATE_DETAIL);
        bindData();
    }

    private void bindData() {
        if (mEstateDetail != null) {
            setOwnerAvatar(mEstateDetail.getOwnerAvatar());
            setOwnerName(mEstateDetail.getOwnerName());
            setOwnerProvince(mEstateDetail.getOwnerProvince());

            setEstateImage(mEstateDetail.getImage());
            setEstateTitle(mEstateDetail.getTitle());
            setEstatePrice(mEstateDetail.getPrice());
            setEstateTimePost(mEstateDetail.getTime());
            setEstateType(mEstateDetail.getType());
            setEstateAddress(mEstateDetail.getAddress());
            setEstateSquare(mEstateDetail.getSquare());
            setEstateDescription(mEstateDetail.getDescription());
        }
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    public void setOwnerAvatar(String ownerAvatar) {
        Glide.with(this)
                .load(ownerAvatar)
                .placeholder(R.color.silver)
                .into(mOwnerAvatar);
    }

    private void setOwnerName(String ownerName) {
        mOwnerName.setText(ownerName);
    }

    public void setOwnerProvince(String ownerDisStrict) {
        mOwnerProvince.setText(ownerDisStrict);
    }

    public void setEstateImage(String estateImage) {
    }

    public void setEstateTitle(String estateTitle) {
        mEstateTitle.setText(estateTitle);
    }

    public void setEstatePrice(String estatePrice) {
        mEstatePrice.setText(estatePrice);
    }

    public void setEstateTimePost(String estateDayPost) {
        mEstateDayPost.setText(estateDayPost);
    }

    public void setEstateAddress(String estateAddress) {
        mEstateAddress.setText(estateAddress);
    }

    public void setEstateType(String estateType) {
        mEstateType.setText(estateType);
    }

    public void setEstateSquare(String estateSquare) {
        mEstateSquare.setText(estateSquare);
    }

    public void setEstateContact(String estateContact) {
        mEstateContact.setText(estateContact);
    }

    public void setEstateDescription(String estateDescription) {
        mEstateDescription.setText(estateDescription);
    }

    @OnClick(R.id.btn_contact)
    public void onContactClick() {
        AlertDialog.Builder contactDialog = new AlertDialog.Builder(this);
        contactDialog.setTitle("Liên hệ ngay");
        contactDialog.setMessage("Bạn có muốn liên hệ ngay với chủ sở hữu?");

        contactDialog.setPositiveButton(R.string.call, (dialog, which) -> {
            Intent dial = new Intent();
            dial.setAction(Intent.ACTION_DIAL);
            dial.setData(Uri.parse("tel:" + mEstateDetail.getPhoneNumber()));
            startActivity(dial);
        });

        contactDialog.setPositiveButton(R.string.message, (dialog, which) -> {
            Intent messaging = new Intent(Intent.ACTION_VIEW);
            messaging.setDataAndType(Uri.parse("tel:" + mEstateDetail.getPhoneNumber()), "vnd.android-dir/mms-sms");
            startActivity(messaging);
        });
        contactDialog.setCancelable(true);
        contactDialog.show();
    }

    @OnClick({R.id.estate_avatar, R.id.estate_province})
    public void onOwnerDetailClick() {

    }
}

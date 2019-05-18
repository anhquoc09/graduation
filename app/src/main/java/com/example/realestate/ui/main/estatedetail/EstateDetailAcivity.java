package com.example.realestate.ui.main.estatedetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.realestate.EstateApplication;
import com.example.realestate.R;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.BaseActivity;
import com.example.realestate.ui.login.LoginActivity;
import com.example.realestate.ui.main.profile.ProfileActivity;
import com.example.realestate.ui.widget.ImageSliderAdapter;
import com.example.realestate.ui.widget.ImageSliderLayout;
import com.example.realestate.ui.widget.InfinitePagerAdapter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    @BindView(R.id.estate_image_slider)
    ImageSliderLayout mImageSlider;

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

    private ImageSliderAdapter mImageSliderAdapter;

    private Unbinder mUnbinder;

    public static Intent intentFor(Context context, Serializable estateDetail) {
        Intent intent = new Intent(context, EstateDetailAcivity.class);
        intent.putExtra(ESTATE_DETAIL, estateDetail);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_detail_layout);
        mUnbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        mEstateDetail = (EstateDetail) intent.getSerializableExtra(ESTATE_DETAIL);
        mImageSliderAdapter = new ImageSliderAdapter(null);
        mImageSlider.setAdapter(new InfinitePagerAdapter(mImageSliderAdapter));
        bindData();
    }

    private void bindData() {
        if (mEstateDetail != null) {
            Context context = EstateApplication.getInstance().getApplicationContext();

            if (mEstateDetail.getCreateTime() != null) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = new Date(mEstateDetail.getCreateTime() * 1000);
                setEstateTimePost(dateFormat.format(date));
            }
            if (mEstateDetail.getAvatar() != null) {
                setOwnerAvatar(mEstateDetail.getAvatar());
            }

            setOwnerName(mEstateDetail.getFullname());
            setOwnerProvince(mEstateDetail.getEmail());

            setImageSlider(mEstateDetail.getUrl());
            setEstateTitle(mEstateDetail.getName());
            setEstatePrice(mEstateDetail.getPrice().toString());
            switch (mEstateDetail.getType()) {
                case 1:
                    setEstateType(context.getString(R.string.estate_apartment));
                    break;
                case 2:
                    setEstateType(context.getString(R.string.estate_townhouse));
                    break;
                case 3:
                    setEstateType(context.getString(R.string.estate_villa));
                    break;
                case 4:
                    setEstateType(context.getString(R.string.estate_land));
                    break;
                case 5:
                    setEstateType(context.getString(R.string.estate_office));
                    break;
                case 6:
                    setEstateType(context.getString(R.string.estate_warehouse));
                    break;
                default:
                    break;
            }
            setEstateAddress(mEstateDetail.getAddress());
            setEstateSquare(mEstateDetail.getArea().toString());
            setEstateContact(mEstateDetail.getPhone());
            setEstateDescription(mEstateDetail.getInfo());
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
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(mOwnerAvatar);
    }

    private void setOwnerName(String ownerName) {
        mOwnerName.setText(ownerName);
    }

    public void setOwnerProvince(String ownerDisStrict) {
        mOwnerProvince.setText(ownerDisStrict);
    }

    public void setImageSlider(List<String> imageSlider) {
        mImageSliderAdapter.setData(imageSlider);
        mImageSlider.checkIfShowIndicator(imageSlider);
    }

    public void setEstateTitle(String estateTitle) {
        mEstateTitle.setText(estateTitle);
    }

    public void setEstatePrice(String estatePrice) {
        mEstatePrice.setText(estatePrice);
    }

    public void setEstateTimePost(String estateDayPost) {
        mEstateDayPost.setText(estateDayPost + " ");
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

    @OnClick(R.id.btn_edit_estate_detail)
    public void onEditEstateClick() {

    }

    @OnClick(R.id.btn_contact)
    public void onContactClick() {
        if (UserManager.isUserLoggedIn()) {
            AlertDialog contactDialog = new AlertDialog.Builder(this).create();
            contactDialog.setTitle(getResources().getString(R.string.contact_now));
            contactDialog.setMessage(getResources().getString(R.string.contact_mess));

            contactDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.call), (dialog, which) -> {
                Intent dial = new Intent(Intent.ACTION_DIAL);
                dial.setData(Uri.parse("tel:" + mEstateDetail.getPhone()));
                startActivity(dial);
            });

            contactDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.message), (dialog, which) -> {
                Intent messaging = new Intent(Intent.ACTION_SENDTO);
                messaging.setData(Uri.parse("smsto:" + Uri.encode(mEstateDetail.getPhone())));
                startActivity(messaging);
            });

            contactDialog.setCancelable(true);
            contactDialog.show();
        } else {
            startActivity(LoginActivity.intentFor(this));
        }
    }

    @OnClick({R.id.estate_avatar, R.id.estate_province, R.id.estate_display_name})
    public void onOwnerDetailClick() {
        startActivity(ProfileActivity.intentFor(this, mEstateDetail.getOwnerid()));
    }

    @OnClick(R.id.btn_back)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

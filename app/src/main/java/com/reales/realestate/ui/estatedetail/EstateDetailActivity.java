package com.reales.realestate.ui.estatedetail;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.reales.realestate.EstateApplication;
import com.reales.realestate.R;
import com.reales.realestate.UserManager;
import com.reales.realestate.data.model.CommentDetail;
import com.reales.realestate.data.model.CommentResponseDetail;
import com.reales.realestate.data.model.EstateDetail;
import com.reales.realestate.ui.BaseActivity;
import com.reales.realestate.ui.login.LoginActivity;
import com.reales.realestate.ui.profile.ProfileActivity;
import com.reales.realestate.ui.submit.SubmitPostActivity;
import com.reales.realestate.ui.widget.ImageSliderAdapter;
import com.reales.realestate.ui.widget.ImageSliderLayout;
import com.reales.realestate.ui.widget.InfinitePagerAdapter;
import com.reales.realestate.ui.widget.WorkaroundMapFragment;
import com.reales.realestate.utils.AndroidUtilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EstateDetailActivity extends BaseActivity implements OnMapReadyCallback, EstateDetailView, CommentListAdapter.OnCommentItemClickListener {
    public static final String TAG = EstateDetailActivity.class.getCanonicalName();

    public static final String ESTATE_DETAIL = "estate_detail";

    @BindView(R.id.coordination_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.estate_avatar)
    ImageView mOwnerAvatar;

    @BindView(R.id.estate_display_name)
    TextView mOwnerName;

    @BindView(R.id.estate_email)
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

    @BindView(R.id.estate_verify)
    TextView mVerify;

    @BindView(R.id.btn_save)
    ImageView mBtnSave;

    @BindView(R.id.btn_edit_estate_detail)
    ImageView mBtnEdit;

    @BindView(R.id.comment_list)
    RecyclerView mCommentList;

    @BindView(R.id.rating)
    RatingBar mRatingBar;

    @BindView(R.id.comment_box)
    EditText mCommentBox;

    @BindView(R.id.btn_send)
    ImageView mBtnSend;

    @BindView(R.id.progress)
    View mProgress;

    private EstateDetail mEstateDetail;

    private ImageSliderAdapter mImageSliderAdapter;

    private Unbinder mUnbinder;

    private Location mLocation;

    private GoogleMap mGoogleMap;

    private EstateDetailPresenter mPresenter;

    private Snackbar mSnackbar;

    private CommentListAdapter mCommentListAdapter;

    public static Intent intentFor(Context context, Serializable estateDetail) {
        Intent intent = new Intent(context, EstateDetailActivity.class);
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

        mSnackbar = Snackbar
                .make(mCoordinatorLayout, getString(R.string.no_network_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.setting), view -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));

        initImageSlider();
        initMap();
        initComment();
        initPresenter();
    }

    private void initComment() {
        mCommentListAdapter = new CommentListAdapter();
        mCommentListAdapter.setCommentClickListener(this);
        mCommentList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mCommentList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mCommentList.setAdapter(mCommentListAdapter);

        mRatingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) ->
                mBtnSend.setSelected(rating > 0 && !mCommentBox.getText().toString().isEmpty()));

        mCommentBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBtnSend.setSelected(!s.toString().isEmpty() && mRatingBar.getRating() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initPresenter() {
        mPresenter = new EstateDetailPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindData();
        mPresenter.getAllComment(mEstateDetail.getId());
    }

    private void initImageSlider() {
        mImageSliderAdapter = new ImageSliderAdapter(null);
        mImageSlider.setAdapter(new InfinitePagerAdapter(mImageSliderAdapter));
    }

    private void initMap() {
        WorkaroundMapFragment supportMapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.detail_map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
            supportMapFragment.setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));
        }
    }

    private void bindData() {
        if (mEstateDetail != null) {
            setOwnerAvatar(mEstateDetail.getAvatar());
            setEstateTimePost(mEstateDetail.getCreateTime());
            setOwnerName(mEstateDetail.getFullName());
            setOwnerEmail(mEstateDetail.getEmail());
            setImageSlider(mEstateDetail.getUrl());
            setEstateTitle(mEstateDetail.getName());
            setEstatePrice(mEstateDetail.getPrice(), mEstateDetail.getStatusPost());
            setEstateType(mEstateDetail.getType(), mEstateDetail.getStatusPost());
            setEstateAddress(mEstateDetail.getAddress());
            setEstateSquare(mEstateDetail.getArea());
            setEstateContact(mEstateDetail.getPhone());
            setEstateDescription(mEstateDetail.getInfo());
            setEstateVerify(mEstateDetail.getVerify());
            setBtnSave();
            setBtnEdit();
        }
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
        initMarker();
        initMyLocation();
    }

    private void initMyLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps_enabled) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.gps_not_found_title)
                    .setMessage(R.string.gps_not_found_message)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton(R.string.no, null)
                    .show();
        }

        if (!network_enabled) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.no_network_connection)
                    .setMessage(R.string.no_network_message)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)))
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void initMarker() {
        mLocation = new Location(mEstateDetail.getName());
        mLocation.setLatitude(mEstateDetail.getLat());
        mLocation.setLongitude(mEstateDetail.getLong());
        LatLng latLng = new LatLng(mEstateDetail.getLat(), mEstateDetail.getLong());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng)
                .title(mEstateDetail.getName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mGoogleMap.addMarker(markerOptions);
        if (mGoogleMap != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    public void setOwnerAvatar(String ownerAvatar) {
        if (!ownerAvatar.isEmpty()) {
            Glide.with(this)
                    .load(ownerAvatar)
                    .placeholder(R.color.silver)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(mOwnerAvatar);
        }
    }

    private void setOwnerName(String ownerName) {
        mOwnerName.setText(ownerName);
    }

    private void setOwnerEmail(String ownerDisStrict) {
        mOwnerProvince.setText(ownerDisStrict);
    }

    private void setImageSlider(List<String> imageSlider) {
        mImageSliderAdapter.setData(imageSlider);
        mImageSlider.checkIfShowIndicator(imageSlider);
    }

    private void setEstateTitle(String estateTitle) {
        mEstateTitle.setText(estateTitle);
    }

    private void setEstatePrice(float estatePrice, int status) {
        String unit;
        if (status == 1) {
            unit = getString(R.string.sell_unit);
        } else {
            unit = getString(R.string.lease_unit);
        }

        mEstatePrice.setText(String.format("%s %s", NumberFormat.getNumberInstance(Locale.getDefault()).format(estatePrice * 1000000), unit));
    }

    private void setEstateTimePost(long estateDayPost) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(estateDayPost * 1000);
        mEstateDayPost.setText(String.format("%s ", dateFormat.format(date)));
    }

    private void setEstateAddress(String estateAddress) {
        mEstateAddress.setText(estateAddress);
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

        mEstateType.setText(string);
    }

    private void setEstateSquare(float estateSquare) {
        mEstateSquare.setText(String.format("%s m2", NumberFormat.getNumberInstance(Locale.getDefault()).format(estateSquare)));
    }

    private void setEstateContact(String estateContact) {
        if (UserManager.isUserLoggedIn()) {
            mEstateContact.setText(estateContact);
        } else {
            if (!estateContact.isEmpty()) {
                String string = "";
                if (estateContact.length() > 3) {
                    string += estateContact.substring(0, estateContact.length() - 3);
                }
                string += "***";
                mEstateContact.setText(string);
            }
        }
    }

    private void setEstateDescription(String estateDescription) {
        mEstateDescription.setText(estateDescription);
    }

    private void setEstateVerify(boolean isVerify) {
        if (!isVerify && UserManager.isUserLoggedIn() && UserManager.getCurrentUser().getId().equals(mEstateDetail.getOwnerid())) {
            mVerify.setVisibility(View.VISIBLE);
        } else {
            mVerify.setVisibility(View.GONE);
        }
    }

    private void setBtnSave() {
        if (UserManager.isUserLoggedIn() && UserManager.getCurrentUser().getId().equals(mEstateDetail.getOwnerid())) {
            mBtnSave.setVisibility(View.GONE);
        } else {
            mBtnSave.setVisibility(View.VISIBLE);
            mBtnSave.setSelected(EstateApplication.savedContain(mEstateDetail.getId()));
        }
    }

    private void setBtnEdit() {
        if (UserManager.isUserLoggedIn() && UserManager.getCurrentUser().getId().equals(mEstateDetail.getOwnerid())) {
            mBtnEdit.setVisibility(View.VISIBLE);
        } else {
            mBtnEdit.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_edit_estate_detail)
    public void onEditEstateClick() {
        startActivity(SubmitPostActivity.intentForEditSubmit(this, mEstateDetail));
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSaveClick() {
        if (!mBtnSave.isSelected()) {
            mPresenter.savePost(mEstateDetail.getFullName(), mEstateDetail.getId(), mEstateDetail.getCreateTime());
        } else {
            mPresenter.unSavePost(mEstateDetail.getId());
        }
    }

    @OnClick(R.id.estate_contact)
    public void onContactClick() {
        if (UserManager.isUserLoggedIn()) {
            AlertDialog contactDialog = new AlertDialog.Builder(this).create();
            contactDialog.setTitle(getString(R.string.contact_now));
            contactDialog.setMessage(getString(R.string.contact_mess));

            contactDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.call), (dialog, which) -> {
                Intent dial = new Intent(Intent.ACTION_DIAL);
                dial.setData(Uri.parse("tel:" + mEstateDetail.getPhone()));
                startActivity(dial);
            });

            contactDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.message), (dialog, which) -> {
                Intent messaging = new Intent(Intent.ACTION_SENDTO);
                messaging.setData(Uri.parse("smsto:" + Uri.encode(mEstateDetail.getPhone())));
                startActivity(messaging);
            });

            contactDialog.setCancelable(true);
            contactDialog.show();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.not_login));
            alertDialog.setMessage(getString(R.string.login_now));

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_cancel), (dialog, which) -> alertDialog.dismiss());

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.login), (dialog, which) -> startActivity(LoginActivity.intentFor(this, true)));

            alertDialog.setCancelable(true);
            alertDialog.show();
        }
    }

    @OnClick({R.id.estate_avatar, R.id.estate_display_name})
    public void onOwnerDetailClick() {
        startActivity(ProfileActivity.intentFor(this, mEstateDetail.getOwnerid()));
    }

    @OnClick(R.id.btn_address)
    public void onAddressClick() {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 15));
    }

    @OnClick(R.id.btn_send)
    public void onSendCommentClick() {
        if (UserManager.isUserLoggedIn()) {
            String comment = mCommentBox.getText().toString();
            int rating = (int) mRatingBar.getRating();
            mBtnSend.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
            mCommentBox.setSelected(false);
            mPresenter.addComment(mEstateDetail.getId(), comment, rating);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.not_login));
            alertDialog.setMessage(getString(R.string.login_now));

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_cancel), (dialog, which) -> alertDialog.dismiss());

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.login), (dialog, which) -> startActivity(LoginActivity.intentFor(this, true)));

            alertDialog.setCancelable(true);
            alertDialog.show();
        }
    }

    @OnClick(R.id.btn_back)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void saveEstateSuccess() {
        mBtnSave.setSelected(true);
    }

    @Override
    public void unSaveEstateSuccess() {
        mBtnSave.setSelected(false);
    }

    @Override
    public void showNoNetworkConnection() {
        mSnackbar.show();
    }

    @Override
    public void hideNoNetworkConnection() {
        mSnackbar.dismiss();
    }

    @Override
    public void setComment(List<CommentDetail> commentList) {
        mCommentListAdapter.setData(commentList);
    }

    @Override
    public void addCommentSuccess(CommentResponseDetail detail) {
        mBtnSend.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
        mRatingBar.setRating(0);
        mCommentBox.setText("");
        mCommentListAdapter.appendData(detail);
    }

    @Override
    public void addCommentError() {
        AndroidUtilities.showToast(getString(R.string.error_comment));
        mBtnSend.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void editCommentSuccess(int position, CommentResponseDetail detail) {
        mCommentListAdapter.onEditCompleted(position, true, detail);
    }

    @Override
    public void editCommentError(int position) {
        mCommentListAdapter.onEditCompleted(position, false, null);
    }

    @Override
    public void deleteCommentSuccess(int position) {
        mCommentListAdapter.onDeleteCompleted(position, true);
    }

    @Override
    public void deleteCommentError(int position) {
        mCommentListAdapter.onDeleteCompleted(position, false);
    }

    @Override
    public void onEditComment(int position, CommentDetail detail) {
        mPresenter.editComment(position, detail);
    }

    @Override
    public void onDeleteComment(int position, String id) {
        mPresenter.deleteComment(position, id);
    }
}

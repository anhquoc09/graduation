package com.example.realestate.ui.submit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.callback.ErrorInfo;
import com.example.realestate.R;
import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.BaseActivity;
import com.example.realestate.ui.estatedetail.EstateDetailActivity;
import com.example.realestate.ui.widget.DebounceEditText;
import com.example.realestate.ui.widget.WorkaroundMapFragment;
import com.example.realestate.utils.AndroidUtilities;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SubmitPostActivity extends BaseActivity
        implements SubmitPostView,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerDragListener,
        ImageUploadListAdapter.OnImageClickListener,
        AdapterView.OnItemSelectedListener {

    private static String KEY_TYPE = "type";

    private static String KEY_DETAIL = "detail";

    public static int SUBMIT_TYPE_NEW = 0;

    public static int SUBMIT_TYPE_EDIT = 1;

    public static int PICK_IMAGE_MULTIPLE = 1;

    @BindView(R.id.coordination_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.btn_submit)
    ImageView mBtnSubmit;

    @BindView(R.id.scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.up_title)
    TextInputLayout mTitle;

    @BindView(R.id.up_investor)
    TextInputLayout mInvestor;

    @BindView(R.id.up_status)
    Spinner mStatus;

    @BindView(R.id.up_type)
    Spinner mType;

    @BindView(R.id.up_price)
    TextInputLayout mPrice;

    @BindView(R.id.up_square)
    TextInputLayout mSquare;

    @BindView(R.id.up_address)
    TextInputLayout mAddressEditText;

    @BindView(R.id.up_list_image)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    TextView mEmptyImageView;

    @BindView(R.id.up_description)
    TextInputLayout mDescription;

    @BindView(R.id.up_contact_name)
    TextInputLayout mName;

    @BindView(R.id.up_contact_phone)
    TextInputLayout mPhone;

    @BindView(R.id.up_contact_email)
    TextInputLayout mEmail;

    private Unbinder mUnBinder;

    private Snackbar mSnackBar;

    private SubmitPostPresenter mPresenter;

    private Geocoder mGeoCoder;

    private GoogleMap mGoogleMap;

    private Location mLocation;

    private LatLng mLatLng;

    private Marker mMarker;

    private String mAddress;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private DebounceEditText mDebounceEditText;

    private MarkerOptions mMarkerOptions = new MarkerOptions();

    private ImageUploadListAdapter mImageAdapter;

    private int mSubmitType;

    private EstateDetail mEditDetail;

    private int[] mTypeCode;

    private int[] mStatusCode;

    public static Intent intentForNewSubmit(Context context) {
        Intent intent = new Intent(context, SubmitPostActivity.class);
        intent.putExtra(KEY_TYPE, SUBMIT_TYPE_NEW);
        return intent;
    }

    public static Intent intentForEditSubmit(Context context, Serializable estateDetail) {
        Intent intent = new Intent(context, SubmitPostActivity.class);
        intent.putExtra(KEY_TYPE, SUBMIT_TYPE_EDIT);
        intent.putExtra(KEY_DETAIL, estateDetail);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_post);
        mUnBinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        mSubmitType = intent.getIntExtra(KEY_TYPE, SUBMIT_TYPE_NEW);
        if (mSubmitType == SUBMIT_TYPE_EDIT) {
            mEditDetail = (EstateDetail) intent.getSerializableExtra(KEY_DETAIL);
        }

        initView();
        initPresenter();
        fillData();
    }

    private void fillData() {
        if (mSubmitType == SUBMIT_TYPE_EDIT) {
            if (!CollectionUtils.isEmpty(mEditDetail.getUrl())) {
                mEmptyImageView.setVisibility(View.GONE);
            } else {
                mEmptyImageView.setVisibility(View.VISIBLE);
            }
            mImageAdapter.setUrlList(mEditDetail.getUrl());
            mTitle.getEditText().setText(mEditDetail.getName());
            mInvestor.getEditText().setText(mEditDetail.getInvestor());
            mStatus.setSelection(mEditDetail.getStatusPost() == 1 ? 0 : 1);
            mType.setSelection(mEditDetail.getType() - 1);
            mPrice.getEditText().setText(String.valueOf(mEditDetail.getPrice()));
            mSquare.getEditText().setText(String.valueOf(mEditDetail.getArea()));
            mAddressEditText.getEditText().setText(mEditDetail.getAddress());
            mDescription.getEditText().setText(mEditDetail.getInfo());
            mName.getEditText().setText(mEditDetail.getFullName());
            mPhone.getEditText().setText(mEditDetail.getPhone());
            mEmail.getEditText().setText(mEditDetail.getEmail());
        } else {
            User user = UserManager.getCurrentUser();
            mInvestor.getEditText().setText(user.getFullname());
            mName.getEditText().setText(user.getFullname());
            mPhone.getEditText().setText(user.getPhone());
            mEmail.getEditText().setText(user.getEmail());
        }
    }

    private void initView() {
        initRecyclerView();
        initSpinner();
        initMap();
        initGeoCoder();
        initAddressListener();
        mSnackBar = Snackbar
                .make(mCoordinatorLayout, getString(R.string.no_network_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.setting), view -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));
    }

    private void initSpinner() {
        String[] typeList = getResources().getStringArray(R.array.type_label);
        mTypeCode = getResources().getIntArray(R.array.type_code);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mType.setAdapter(typeAdapter);

        String[] statusList = getResources().getStringArray(R.array.status_label);
        mStatusCode = getResources().getIntArray(R.array.status_code);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusList);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mStatus.setAdapter(statusAdapter);
        mStatus.setOnItemSelectedListener(this);

    }

    private void initMap() {
        WorkaroundMapFragment supportMapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.up_map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
            supportMapFragment.setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));
        }
    }

    private void initGeoCoder() {
        mGeoCoder = new Geocoder(this, Locale.getDefault());
    }

    private void initAddressListener() {
        mDebounceEditText = new DebounceEditText(mAddressEditText.getEditText(), result -> {
            mAddress = result.toString();
            updateLocation();
        });
    }

    private void initRecyclerView() {
        mImageAdapter = new ImageUploadListAdapter();
        mImageAdapter.setOnImageClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mImageAdapter);
    }

    private void initPresenter() {
        if (mSubmitType == SUBMIT_TYPE_NEW) {
            mPresenter = new SubmitPostPresenter(mSubmitType, null);
        } else {
            mPresenter = new SubmitPostPresenter(mSubmitType, mEditDetail);
        }
        mPresenter.attachView(this);
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
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null && mSubmitType == SUBMIT_TYPE_NEW) {
                    mLocation = location;
                }
                mAddressEditText.getEditText().setText(getAddressFromLocation(mLocation));
                moveMarker(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
            });
        }
    }

    private void initMarker() {
        mLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        mMarkerOptions.position(mLatLng)
                .title(getResources().getString(R.string.estate_location))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .draggable(true);
        mMarker = mGoogleMap.addMarker(mMarkerOptions);
        mMarker.setDraggable(true);
        mMarker.showInfoWindow();
        if (mGoogleMap != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
        }
    }

    private void updateLocation() {
        if (mGoogleMap != null) {
            moveMarker(getLocationFromAddress(mAddress));
        }
    }

    private void moveMarker(LatLng latLng) {
        if (latLng != null && mMarker != null) {
            mLatLng = latLng;
            mMarker.setPosition(mLatLng);
            if (mGoogleMap != null) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
            }
        }
    }

    private String getAddressFromLocation(Location location) {
        if (location != null) {
            List<Address> addressList;
            try {
                addressList = mGeoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addressList != null && !addressList.isEmpty()) {
                    Address address = addressList.get(0);
                    return address.getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private LatLng getLocationFromAddress(String strAddress) {
        if (!TextUtils.isEmpty(strAddress) && mGeoCoder != null) {
            List<Address> addressList;
            try {
                addressList = mGeoCoder.getFromLocationName(mAddress, 1);
                if (addressList != null && !addressList.isEmpty()) {
                    Address location = addressList.get(0);
                    return new LatLng(location.getLatitude(), location.getLongitude());
                }
            } catch (IOException e) {
                AndroidUtilities.showToast(getString(R.string.no_address));
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMarkerDragListener(this);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        mLocation = new Location("HCMUS");
        mLocation.setLatitude(10.763147);
        mLocation.setLongitude(106.682203);
        initMarker();
        initMyLocation();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        moveMarker(latLng);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mLatLng = marker.getPosition();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mLatLng));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {

                mPresenter.pickImageResultOK(data);

            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (mDebounceEditText != null) {
            mDebounceEditText.destroy();
        }
        if (mPresenter.isViewAttached()) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    public void addImage(Uri imageUri) {
        mImageAdapter.addImage(imageUri);
    }

    @Override
    public void setImageList(List<Uri> imageListUri) {
        mImageAdapter.setImageList(imageListUri);
        if (!CollectionUtils.isEmpty(imageListUri)) {
            mEmptyImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSubmitSuccess(EstateDetail estateDetail) {
        AndroidUtilities.showToast(getString(R.string.submit_success));
        startActivity(EstateDetailActivity.intentFor(this, estateDetail));
        finish();
    }

    @Override
    public void onSubmitFailed(String message) {
        mBtnSubmit.setImageResource(R.drawable.ic_submit);
        AndroidUtilities.showToast(message);
    }

    @Override
    public void uploadError(ErrorInfo error, int position) {
        AndroidUtilities.showToast(error.getDescription());
        mImageAdapter.uploadError(position);
    }

    @Override
    public void startUploadImage(int position) {
        mImageAdapter.startUpload(position);
    }

    @Override
    public void uploadImageSuccess(int position) {
        mImageAdapter.uploadImageSuccess(position);
    }

    @Override
    public void updateUploadPercent(int percent, int position) {
        mImageAdapter.updatePercent(percent, position);
    }

    @Override
    public void showNoNetwork() {
        mSnackBar.show();
    }

    @Override
    public void hideNoNetwork() {
        mSnackBar.dismiss();
    }

    @OnClick(R.id.btn_back)
    @Override
    public void onBackPressed() {
        AlertDialog contactDialog = new AlertDialog.Builder(this).create();
        contactDialog.setTitle(getString(R.string.submitting));
        contactDialog.setMessage(getString(R.string.stop_message));
        contactDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_cancel), (dialog, which) -> dismissDialog());
        contactDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.stop), (dialog, which) -> super.onBackPressed());
        contactDialog.setCancelable(true);
        contactDialog.show();
    }

    @OnClick(R.id.btn_submit)
    public void onUpClick() {

        mScrollView.fullScroll(ScrollView.FOCUS_UP);

        CircularProgressDrawable progressDrawable = new CircularProgressDrawable(this);
        progressDrawable.setStrokeWidth(5f);
        progressDrawable.setCenterRadius(20f);
        progressDrawable.setColorSchemeColors(Color.WHITE);
        progressDrawable.start();

        if (canSubmit()) {
            mBtnSubmit.setImageDrawable(progressDrawable);

            mPresenter.submit(mTitle.getEditText().getText().toString(),
                    mInvestor.getEditText().getText().toString(),
                    Float.valueOf(mPrice.getEditText().getText().toString()),
                    "Triệu",
                    Float.valueOf(mSquare.getEditText().getText().toString()),
                    mTypeCode[mType.getSelectedItemPosition()],
                    mAddress,
                    mDescription.getEditText().getText().toString(),
                    mLatLng.latitude,
                    mLatLng.longitude,
                    mStatusCode[mStatus.getSelectedItemPosition()],
                    mName.getEditText().getText().toString(),
                    mPhone.getEditText().getText().toString(),
                    mEmail.getEditText().getText().toString());

            AndroidUtilities.showToast(getString(R.string.wait_for_submitting));
        } else {
            AndroidUtilities.showToast(getString(R.string.error_can_not_submit));
        }
    }

    private boolean isNotBlank(TextInputLayout textInputLayout, String error) {
        if (TextUtils.isEmpty(textInputLayout.getEditText().getText())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(error);
        } else {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError(null);
        }
        return !textInputLayout.isErrorEnabled();
    }

    private boolean canSubmit() {
        boolean canSubmit = isEmptyImage();
        canSubmit = isNotBlank(mTitle, getString(R.string.error_no_title)) && canSubmit;
        canSubmit = isNotBlank(mPrice, getString(R.string.error_no_price)) && canSubmit;
        canSubmit = isNotBlank(mSquare, getString(R.string.error_no_square)) && canSubmit;
        canSubmit = isNotBlank(mAddressEditText, getString(R.string.error_no_address)) && canSubmit;
        canSubmit = isNotBlank(mName, getString(R.string.error_no_name)) && canSubmit;
        canSubmit = isNotBlank(mPhone, getString(R.string.error_no_phone)) && canSubmit;
        canSubmit = isNotBlank(mEmail, getString(R.string.error_no_email)) && canSubmit;
        return canSubmit;
    }

    private boolean isEmptyImage() {
        if (mImageAdapter.getItemCount() <= 0) {
            mEmptyImageView.setTextColor(Color.RED);
            mEmptyImageView.setText(R.string.error_no_picture);
            return false;
        } else {
            return true;
        }
    }

    @OnClick(R.id.btn_add_image)
    public void onAddImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    @Override
    public void onViewImageClick(List<ImageInfoItem> imageUrlList, int position) {
//                Intent intent = new Intent(mContext, SpacePhotoActivity.class);
//                intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
//                startActivity(intent);
    }

    @Override
    public void onDeleteImageUri(int position) {
        mPresenter.deleteImageUri(position);
        if (mImageAdapter.getItemCount() <= 0) {
            mEmptyImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteImageUrl(int position) {
        mPresenter.deleteImageUrl(position);
        if (mImageAdapter.getItemCount() <= 0) {
            mEmptyImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            mPrice.setHint(getString(R.string.submit_sell_price));
        } else {
            mPrice.setHint(getString(R.string.submit_lease_price));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

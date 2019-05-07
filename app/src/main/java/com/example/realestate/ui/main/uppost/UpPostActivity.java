package com.example.realestate.ui.main.uppost;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.realestate.R;
import com.example.realestate.ui.BaseActivity;
import com.example.realestate.ui.widget.DebounceEditText;
import com.example.realestate.utils.AndroidUtilities;
import com.example.realestate.utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UpPostActivity extends BaseActivity
        implements UpPostView,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerDragListener,
        ImageRecyclerViewAdapter.OnImageClickListener {

    @BindView(R.id.up_title)
    EditText mTitle;

    @BindView(R.id.up_price)
    EditText mPrice;

    @BindView(R.id.up_square)
    EditText mSquare;

    @BindView(R.id.up_address)
    EditText mAddressEditText;

    @BindView(R.id.up_list_image)
    RecyclerView mRecyclerView;

    @BindView(R.id.up_description)
    EditText mDescription;

    private Unbinder mUnbinder;

    private UpPostPresenter mPresenter;

    private Geocoder mGeocoder;

    private GoogleMap mGoogleMap;

    private LocationManager mLocationManager;

    private Location mLocation;

    private LatLng mLatLng;

    private Marker mMarker;

    private String mAddress;

    private DebounceEditText mDebounceEditText;

    private MarkerOptions mMarkerOptions = new MarkerOptions();

    private ImageRecyclerViewAdapter mImageAdapter;

    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, UpPostActivity.class);
        intent.putExtras(new Bundle());
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_post);
        mUnbinder = ButterKnife.bind(this);
        initView();
        initPresenter();
    }

    private void initView() {
        initMap();
        initGeoCoder();
        initAddressListener();
        initRecyclerView();
    }

    private void initMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.up_map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
    }

    private void initGeoCoder() {
        mGeocoder = new Geocoder(this, Locale.getDefault());
    }

    private void initAddressListener() {
        mDebounceEditText = new DebounceEditText(mAddressEditText, result -> {
            mAddress = result.toString();
            updateLocation();
        });
    }

    private void initRecyclerView() {
        mImageAdapter = new ImageRecyclerViewAdapter();
        mImageAdapter.setOnImageClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mImageAdapter);
    }

    private void initPresenter() {
        mPresenter = new UpPostPresenter();
        mPresenter.attachView(this);
    }

    private void initMyLocation() {
        mGoogleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 15));
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
        moveMarker(mLatLng);
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

    private LatLng getLocationFromAddress(String strAddress) {
        if (!TextUtils.isEmpty(strAddress) && mGeocoder != null) {
            List<Address> address;
            try {
                address = mGeocoder.getFromLocationName(mAddress, 5);
                if (address != null && !address.isEmpty()) {
                    Address location = address.get(0);
                    return new LatLng(location.getLatitude(), location.getLongitude());
                }
            } catch (IOException e) {
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

        mLocation = new Location("HCMUS");
        mLocation.setLatitude(10.763147);
        mLocation.setLongitude(106.682203);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            PermissionUtils.Request_FINE_LOCATION(this, 1);

        } else {
            mGoogleMap.setMyLocationEnabled(true);
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 50, mLocationListener);
        }
        initMyLocation();
        initMarker();
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
        if (mUnbinder != null) {
            mUnbinder.unbind();
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
    }

    @Override
    public void onPostSuccess() {
        AndroidUtilities.showToast("Your post is upload success!");
    }

    @Override
    public void onPostFailed(String message) {
        AndroidUtilities.showToast(message);
    }

    @OnClick(R.id.btn_up_post)
    public void onUpClick() {
        mPresenter.upPost();
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
    public void onViewImageClick(List<Uri> imageUrlList, int position) {
//                Intent intent = new Intent(mContext, SpacePhotoActivity.class);
//                intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
//                startActivity(intent);
    }

    @Override
    public void onDeleteImageClick(int position) {
        mPresenter.deleteImage(position);
    }
}

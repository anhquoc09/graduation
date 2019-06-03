package com.example.realestate.ui.main.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.main.estatedetail.EstateDetailActivity;
import com.example.realestate.ui.widget.DebounceEditText;
import com.example.realestate.utils.PermissionUtils;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class HomeMapFragment extends Fragment
        implements HomePagerView,
        OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnInfoWindowClickListener {

    public static final String TAG = HomeMapFragment.class.getSimpleName();

    private static final float LOAD_DISTANCE = 2000;

    @BindView(R.id.ic_delete_search)
    ImageView mIconClear;

    @BindView(R.id.search_text)
    EditText mSearchText;

    @BindView(R.id.more_filter_layout)
    View mMoreFilterLayout;

    @BindView(R.id.btn_more_filter)
    View mBtnMoreFilter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    private HomeMapPresenter mPresenter;

    private String mCurrentSearchKey;

    private DebounceEditText mDebounceEditText;

    private GoogleMap mGoogleMap;

    private Location mMyLocation;

    private Location mOldCamLocation;

    private final List<EstateDetail> mList = new ArrayList<>();

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                mMyLocation = location;
            }
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

    public static HomeMapFragment newInstance() {
        HomeMapFragment fragment = new HomeMapFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMyLocation = new Location("HCMUS");
        mMyLocation.setLatitude(10.763147);
        mMyLocation.setLongitude(106.682203);

        initSearchBox();
        initPresenter();
        initMap();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mDebounceEditText != null) {
            mDebounceEditText.destroy();
        }
        super.onDestroyView();
    }

    private void initSearchBox() {
        mDebounceEditText = new DebounceEditText(mSearchText, result -> mCurrentSearchKey = result.toString());

        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mIconClear.setVisibility(View.INVISIBLE);
                } else {
                    mIconClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initPresenter() {
        mPresenter = new HomeMapPresenter();
        mPresenter.attachView(this);
    }

    private void initMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.setInfoWindowAdapter(new CustomGoogleMapAdapter(getContext()));
        mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setOnCameraIdleListener(this);
        initMyLocation();
        notifyDataChange();
    }

    private void fetchData(double latitude, double longitude) {
        if (mPresenter != null) {
            mPresenter.fetchData(latitude, longitude);
        }
    }

    public void setData(List<EstateDetail> list) {
        mList.clear();
        if (!CollectionUtils.isEmpty(list)) {
            mList.addAll(list);
            notifyDataChange();
        }
    }

    private void notifyDataChange() {
        if (mGoogleMap != null) {
            mGoogleMap.clear();

            for (EstateDetail estate : mList) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(estate.getLat(), estate.getLong()))
                        .title(estate.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                Marker marker = mGoogleMap.addMarker(markerOptions);
                marker.setTag(estate);
            }
        }
    }

    private void initMyLocation() {

        Activity activity = getActivity();
        if (activity != null) {
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gps_enabled && !network_enabled) {
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.gps_not_found_title)
                        .setMessage(R.string.gps_not_found_message)
                        .setPositiveButton(R.string.yes, (dialogInterface, i) -> activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                        .setNegativeButton(R.string.no, null)
                        .show();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                PermissionUtils.Request_FINE_LOCATION(activity, 1);
            } else {
                mGoogleMap.setMyLocationEnabled(true);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    mMyLocation = location;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 50, mLocationListener);
            }
        }
        mOldCamLocation = mMyLocation;
        mGoogleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mOldCamLocation.getLatitude(),
                                mOldCamLocation.getLongitude()),
                        15));
        fetchData(mOldCamLocation.getLatitude(), mOldCamLocation.getLongitude());
    }

    private void animateToMyLocation() {
        if (mGoogleMap.isMyLocationEnabled()) {
            mOldCamLocation = mMyLocation;
            mGoogleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(mOldCamLocation.getLatitude(),
                                    mOldCamLocation.getLongitude()),
                            15));

        } else {
            initMyLocation();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        EstateDetail estateDetail = (EstateDetail) marker.getTag();
        if (estateDetail != null) {
            startActivity(EstateDetailActivity.intentFor(getActivity(), estateDetail));
        }
    }

    @OnClick(R.id.btn_my_location)
    public void onMyLocationClick() {
        animateToMyLocation();
    }

    @Override
    public void onCameraIdle() {
        LatLng target = mGoogleMap.getCameraPosition().target;
        Location newLocation = new Location("");
        newLocation.setLatitude(target.latitude);
        newLocation.setLongitude(target.longitude);
        float distance = mOldCamLocation.distanceTo(new Location(newLocation));

        if (distance >= LOAD_DISTANCE) {
            mOldCamLocation = newLocation;
            fetchData(mOldCamLocation.getLatitude(), mOldCamLocation.getLongitude());
        }
    }

    @OnClick(R.id.btn_more_filter)
    public void onMoreFilterClick() {
        if (mMoreFilterLayout.getVisibility() == View.GONE) {
            mMoreFilterLayout.setVisibility(View.VISIBLE);
            mBtnMoreFilter.setSelected(true);
        } else {
            mMoreFilterLayout.setVisibility(View.GONE);
            mBtnMoreFilter.setSelected(false);
        }
    }

    @OnClick(R.id.btn_for)
    public void onBtnForClick() {

    }

    @OnClick(R.id.btn_type)
    public void onBtnTypeClick() {

    }

    @OnClick(R.id.btn_district)
    public void onBtnDistrictClick() {

    }

    @OnClick(R.id.btn_search)
    public void onBtnSearchClick() {

    }

    @Override
    public void fetchDataSuccess(List<EstateDetail> list) {
        setData(list);
    }
}

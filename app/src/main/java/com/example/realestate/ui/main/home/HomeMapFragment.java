package com.example.realestate.ui.main.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.SnapHelper;

import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.estatedetail.EstateDetailActivity;
import com.example.realestate.ui.profile.ProfileActivity;
import com.example.realestate.ui.widget.DebounceEditText;
import com.example.realestate.utils.AndroidUtilities;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.Unbinder;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class HomeMapFragment extends Fragment
        implements HomePagerView,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraIdleListener,
        HomeMapEstateListAdapter.OnItemClickListener {

    public static final String TAG = HomeMapFragment.class.getSimpleName();

    private static final float LOAD_DISTANCE = 2000;

    private static final float ZOOM = 13;

    @BindView(R.id.coordination_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.loading_layout)
    View mProgress;

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

    @BindView(R.id.tv_show_list)
    View mBtnShowList;

    @BindView(R.id.list_layout)
    View mListLayout;

    @BindView(R.id.spinner_type)
    Spinner mType;

    @BindView(R.id.spinner_status)
    Spinner mStatus;

    @BindView(R.id.spinner_price)
    Spinner mPrice;

    private Unbinder mUnbinder;

    private HomeMapPresenter mPresenter;

    private String mCurrentSearchKey;

    private DebounceEditText mDebounceEditText;

    private GoogleMap mGoogleMap;

    private Geocoder mGeoCoder;

    private Location mMyLocation;

    private Location mOldFetchDataLocation;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private int mPreMarkerSelectedIndex = -1;

    private HomeMapEstateListAdapter mListAdapter;

    private HomeMapDataFilterProvider mFilterProvider = new HomeMapDataFilterProvider();

    private SnapHelper mSnapHelper = new LinearSnapHelper();

    private final List<EstateDetail> mList = new ArrayList<>();

    private final List<Marker> mMarkers = new ArrayList<>();

    private int[] mTypeCode;

    private int[] mStatusCode;

    private int[] mSellCode;

    private int[] mLeaseCode;

    private String[] mSellList;

    private String[] mLeaseList;

    public static HomeMapFragment newInstance() {
        HomeMapFragment fragment = new HomeMapFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGeoCoder = new Geocoder(getContext(), Locale.getDefault());
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
        mOldFetchDataLocation = mMyLocation;

        initRecyclerView();
        initSearchBox();
        initSpinner();
        initPresenter();
        initMap();
    }

    private void initSpinner() {
        Activity activity = getActivity();
        if (activity != null) {

            String[] statusList = getResources().getStringArray(R.array.status_label);
            mStatusCode = getResources().getIntArray(R.array.status_code);
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, statusList);
            statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            mStatus.setAdapter(statusAdapter);

            String[] typeList = getResources().getStringArray(R.array.filter_type_label);
            mTypeCode = getResources().getIntArray(R.array.filter_type_code);
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, typeList);
            typeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            mType.setAdapter(typeAdapter);

            mSellList = getResources().getStringArray(R.array.filter_sell_price_label);
            mSellCode = getResources().getIntArray(R.array.filter_sell_price_value);
            ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mSellList);
            priceAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            mPrice.setAdapter(priceAdapter);

            mLeaseList = getResources().getStringArray(R.array.filter_lease_price_label);
            mLeaseCode = getResources().getIntArray(R.array.filter_lease_price_value);

            mStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayAdapter<String> priceAdapter;
                    if (position == 0) {
                        priceAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, mSellList);
                    } else {
                        priceAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, mLeaseList);
                    }
                    priceAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    mPrice.setAdapter(priceAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void initRecyclerView() {
        mListAdapter = new HomeMapEstateListAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mListAdapter);
        mListAdapter.setItemClickListener(this);
        mSnapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setOnScrollListener(mScrollListener);
    }

    private OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int state) {
            if (state == RecyclerView.SCROLL_STATE_IDLE) {
                int position = getSnapPosition(recyclerView);
                if (position < mMarkers.size() && position != mPreMarkerSelectedIndex) {

                    mMarkers.get(position).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    if (mPreMarkerSelectedIndex != -1 && mPreMarkerSelectedIndex < mMarkers.size()) {
                        mMarkers.get(mPreMarkerSelectedIndex).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }
                    mPreMarkerSelectedIndex = position;
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarkers.get(position).getPosition(), ZOOM));
                }
            }
        }
    };

    private int getSnapPosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        View snapView;

        if (layoutManager == null) {
            return RecyclerView.NO_POSITION;
        } else {
            snapView = mSnapHelper.findSnapView(layoutManager);
            return snapView == null ? RecyclerView.NO_POSITION : layoutManager.getPosition(snapView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData(mOldFetchDataLocation.getLatitude(), mOldFetchDataLocation.getLongitude());
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
        mDebounceEditText = new DebounceEditText(mSearchText, result -> {
            mCurrentSearchKey = result.toString();
            LatLng latLng = getLocationFromAddress(mCurrentSearchKey);
            if (latLng != null) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), ZOOM));
                mPresenter.fetchData(latLng.latitude, latLng.longitude);
            }
        });

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

    private LatLng getLocationFromAddress(String strAddress) {
        if (!TextUtils.isEmpty(strAddress) && mGeoCoder != null) {
            List<Address> addressList;
            try {
                addressList = mGeoCoder.getFromLocationName(strAddress, 1);
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
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnMarkerClickListener(this);

        mOldFetchDataLocation = mMyLocation;
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mOldFetchDataLocation.getLatitude(), mOldFetchDataLocation.getLongitude()), ZOOM));

        initMyLocation();
        notifyMapChange();
    }

    private void fetchData(double latitude, double longitude) {
        if (mOldFetchDataLocation.getLatitude() != latitude && mOldFetchDataLocation.getLongitude() != longitude) {

            mOldFetchDataLocation.setLatitude(latitude);
            mOldFetchDataLocation.setLongitude(longitude);
        }
        if (mPresenter != null) {
            mPresenter.fetchData(latitude, longitude);
        }
    }

    public void setData(List<EstateDetail> list) {
        mList.clear();
        mFilterProvider.setData(list);

        List<EstateDetail> filteredList = filter();
        if (filteredList != null) {
            if (!filteredList.isEmpty()) {
                mList.addAll(filteredList);
            }
        } else {
            if (list != null && !list.isEmpty()) {
                mList.addAll(list);
            }
        }
        setFilteredData(mList, true);
    }

    private List<EstateDetail> filter() {
        List<EstateDetail> filteredList;
        if (mStatus.getSelectedItemPosition() == 0) {
            filteredList = mFilterProvider.filter(mStatusCode[mStatus.getSelectedItemPosition()],
                    mTypeCode[mType.getSelectedItemPosition()],
                    mSellCode[mPrice.getSelectedItemPosition()]);
        } else {
            filteredList = mFilterProvider.filter(mStatusCode[mStatus.getSelectedItemPosition()],
                    mTypeCode[mType.getSelectedItemPosition()],
                    mLeaseCode[mPrice.getSelectedItemPosition()]);
        }

        return filteredList;
    }

    private void setFilteredData(List<EstateDetail> filteredList, boolean isDataChanged) {
        if (isDataChanged) {
            if (!CollectionUtils.isEmpty(filteredList)) {
                showList();
            } else {
                hideList();
            }
            mListAdapter.setData(mList);
            notifyMapChange();
        }
    }

    private void notifyMapChange() {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
            mMarkers.clear();

            for (EstateDetail estate : mList) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(estate.getLat(), estate.getLong()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                Marker marker = mGoogleMap.addMarker(markerOptions);
                mMarkers.add(marker);
            }
        }
    }

    private void initMyLocation() {

        Activity activity = getActivity();
        if (activity != null) {
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gps_enabled) {
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.gps_not_found_title)
                        .setMessage(R.string.gps_not_found_message)
                        .setPositiveButton(R.string.yes, (dialogInterface, i) -> activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                        .setNegativeButton(R.string.no, null)
                        .show();
            }

            if (!network_enabled) {
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.no_network_connection)
                        .setMessage(R.string.no_network_message)
                        .setPositiveButton(R.string.yes, (dialogInterface, i) -> activity.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                        .setNegativeButton(R.string.no, null)
                        .show();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                mGoogleMap.setMyLocationEnabled(true);
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        mMyLocation = location;
                    }
                    animateToMyLocation();
                });
            }
        }
    }

    private void animateToMyLocation() {
        if (mOldFetchDataLocation.distanceTo(mMyLocation) >= LOAD_DISTANCE) {
            fetchData(mMyLocation.getLatitude(), mMyLocation.getLongitude());
        }

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude()), ZOOM));
    }

    private void showList() {
        mBtnShowList.setVisibility(View.GONE);
        mListLayout.setVisibility(View.VISIBLE);
    }

    private void hideList() {
        mBtnShowList.setVisibility(View.VISIBLE);
        mListLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mListLayout.getVisibility() == View.GONE) {
            mListLayout.setVisibility(View.VISIBLE);
        }

        int index = mMarkers.indexOf(marker);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        if (mPreMarkerSelectedIndex != -1 && mPreMarkerSelectedIndex != index && mPreMarkerSelectedIndex < mMarkers.size()) {
            mMarkers.get(mPreMarkerSelectedIndex).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }

        mRecyclerView.smoothScrollToPosition(index);
        mPreMarkerSelectedIndex = index;
        return false;
    }

    @Override
    public void onCameraIdle() {
        LatLng target = mGoogleMap.getCameraPosition().target;
        Location newLocation = new Location("");
        newLocation.setLatitude(target.latitude);
        newLocation.setLongitude(target.longitude);

        if (mOldFetchDataLocation.distanceTo(newLocation) >= LOAD_DISTANCE) {
            if (mPreMarkerSelectedIndex != -1 && mPreMarkerSelectedIndex < mMarkers.size()) {
                Location selectedMarker = new Location("");
                LatLng selectedPosition = mMarkers.get(mPreMarkerSelectedIndex).getPosition();
                selectedMarker.setLatitude(selectedPosition.latitude);
                selectedMarker.setLongitude(selectedPosition.longitude);
                if (newLocation.distanceTo(selectedMarker) > 100) {

                    fetchData(newLocation.getLatitude(), newLocation.getLongitude());
                }
            } else {
                fetchData(newLocation.getLatitude(), newLocation.getLongitude());
            }
        }
    }

    @Override
    public void saveEstateSuccess(int position) {
        mListAdapter.saveSuccess(position);
    }

    @Override
    public void unSaveEstateSuccess(int position) {
        mListAdapter.saveSuccess(position);
    }

    @Override
    public void showNoConnection() {
        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, getString(R.string.no_network_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.setting), view -> {
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });

        snackbar.show();
    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void fetchDataSuccess(List<EstateDetail> list) {
        setData(list);
    }

    @Override
    public void onItemSelected(EstateDetail item) {
        startActivity(EstateDetailActivity.intentFor(getActivity(), item));
    }

    @Override
    public void onAvatarClick(String userId) {
        startActivity(ProfileActivity.intentFor(getActivity(), userId));
    }

    @Override
    public void savePost(EstateDetail item, int position) {
        mPresenter.savePost(item.getFullName(), item.getId(), item.getCreateTime(), position);
    }

    @Override
    public void unSavePost(EstateDetail item, int position) {
        mPresenter.unSavePost(item.getId(), position);
    }

    @OnClick(R.id.btn_my_location)
    public void onMyLocationClick() {
        initMyLocation();
    }

    @OnClick(R.id.tv_show_list)
    public void onShowListClick() {
        showList();
    }

    @OnClick(R.id.tv_hide_list)
    public void onHideListClick() {
        hideList();
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

    @OnClick(R.id.ic_delete_search)
    public void onClearSearch() {
        mSearchText.setText("");
    }

    @OnFocusChange(R.id.search_text)
    public void onSearch() {
        if (mListLayout.getVisibility() == View.VISIBLE) {
            hideList();
        }
    }

    @OnClick(R.id.btn_filter)
    public void onBtnSearchClick() {
        List<EstateDetail> filteredList = filter();
        if (filteredList != null) {
            mList.clear();
            if (!filteredList.isEmpty()) {
                mList.addAll(filteredList);
            }
            setFilteredData(mList, true);

        } else {
            setFilteredData(mList, false);
        }
    }

    @OnClick(R.id.btn_reset)
    public void onResetClick() {
        mType.setSelection(0);
        mPrice.setSelection(0);

        List<EstateDetail> filteredList = filter();
        if (filteredList != null) {
            mList.clear();
            if (!filteredList.isEmpty()) {
                mList.addAll(filteredList);
            }
            setFilteredData(mList, true);

        } else {
            setFilteredData(mList, false);
        }
    }
}

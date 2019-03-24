package com.example.realestate.ui.main.home.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author anhquoc09
 * @since 10/03/2019
 */

public class MapFragment extends Fragment implements MapView {

    private static final String TAG = MapFragment.class.getSimpleName();

    private Unbinder mUnbinder;

    private GoogleMap mGoogleMap;

    private MapPresenter mPresenter;

    private OnCallBackListener mCallBackListener;

    public static MapFragment newInstance(OnCallBackListener listener) {
        MapFragment fragment = new MapFragment();
        fragment.setCallBackListener(listener);
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new MapPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);

        initPresenter();

        initMap();
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    private void initMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(googleMap -> {
            mGoogleMap = googleMap;
            mGoogleMap.setOnMapLoadedCallback(() -> {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            });
            LatLng hcmus = new LatLng(10.762879, 106.682189);
            mGoogleMap.addMarker(new MarkerOptions().position(hcmus).title("HCMUS"));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
        });
    }

    private void initPresenter() {
        mPresenter = new MapPresenter();
        mPresenter.attachView(this);
    }

    public void setCallBackListener(OnCallBackListener listener) {
        mCallBackListener = listener;
    }

    public void setData(List<EstateDetail> list) {
    }

    /**
     * {@link OnCallBackListener}
     */
    public interface OnCallBackListener {
        void onMoveMap();

        void onRefreshMap();
    }
}

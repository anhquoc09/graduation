package com.example.realestate.ui.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.realestate.R;
import com.example.realestate.ui.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author anhquoc09
 * @since 10/03/2019
 */

public class MapActivity extends BaseActivity {

    private GoogleMap mGoogleMap;

    public static Intent intentFor(Context context) {
        return new Intent(context, MapActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initViews();
    }

    private void initViews() {
        showDialogLoading();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                mGoogleMap.setOnMapLoadedCallback(() -> {
                    dismissDialog();
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                });
                LatLng hcmus = new LatLng(10.762879, 106.682189);
                mGoogleMap.addMarker(new MarkerOptions().position(hcmus).title("HCMUS"));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
            }
        });
    }
}

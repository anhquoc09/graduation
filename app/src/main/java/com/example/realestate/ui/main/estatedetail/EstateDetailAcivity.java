package com.example.realestate.ui.main.estatedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.BaseActivity;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EstateDetailAcivity extends BaseActivity {
    public static final String TAG = EstateDetailAcivity.class.getCanonicalName();

    public static final String ESTATE_DETAIL = "estate_detail";

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_layout);
        mUnbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        mEstateDetail = (EstateDetail) intent.getSerializableExtra(ESTATE_DETAIL);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}

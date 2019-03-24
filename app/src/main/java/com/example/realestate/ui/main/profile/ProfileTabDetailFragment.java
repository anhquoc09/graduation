package com.example.realestate.ui.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class ProfileTabDetailFragment extends Fragment {

    @BindView(R.id.profile_id)
    EditText mId;

    @BindView(R.id.profile_display_name)
    EditText mName;

    @BindView(R.id.profile_email)
    EditText mEmail;

    @BindView(R.id.profile_phone)
    EditText mPhone;

    @BindView(R.id.profile_address)
    EditText mAddress;

    private OnCallBackListener mCallBackListener;

    private Unbinder mUnbinder;

    public static Fragment newInstance(OnCallBackListener listener) {
        ProfileTabDetailFragment fragment = new ProfileTabDetailFragment();
        fragment.setOnCallBackListener(listener);
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
        View view = inflater.inflate(R.layout.fragment_profile_detail_layout, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {

    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    public void setData(EstateDetail list) {

    }

    public void setOnCallBackListener(OnCallBackListener listener) {
        mCallBackListener = listener;
    }

    @OnClick(R.id.btn_edit)
    public void onBtnEditClick() {
        mName.setEnabled(true);
        mPhone.setEnabled(true);
        mAddress.setEnabled(true);
    }

    @OnClick(R.id.btn_save)
    public void onBtnSaveClick() {
        if (mCallBackListener != null) {
            mCallBackListener.onEditProfile();
        }

        mName.setEnabled(false);
        mPhone.setEnabled(false);
        mAddress.setEnabled(false);
    }

    /**
     * {@link OnCallBackListener}
     */
    public interface OnCallBackListener {
        void onEditProfile();
    }
}

package com.example.realestate.ui.main.profile;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.realestate.R;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.data.model.ProfileDetail;
import com.example.realestate.utils.AndroidUtilities;

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

    @BindView(R.id.btn_edit)
    Button mBtnEdit;

    @BindView(R.id.btn_save)
    Button mBtnSave;

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

    private void showBtnEdit() {
        if (mBtnEdit != null) {
            mBtnEdit.setVisibility(View.VISIBLE);
        }
    }

    private void hideBtnEdit() {
        if (mBtnEdit != null) {
            mBtnEdit.setVisibility(View.GONE);
        }
    }

    private void showBtnSave() {
        if (mBtnSave != null) {
            mBtnSave.setVisibility(View.VISIBLE);
        }
    }

    private void hideBtnSave() {
        if (mBtnSave != null) {
            mBtnSave.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    public void setData(ProfileDetail profileDetail) {
        if (profileDetail == null) {
            return;
        }
        if (UserManager.isUserLoggedIn() && UserManager.getCurrentUser().getUserId() == profileDetail.getProfileId()) {
            showBtnEdit();
        }
    }

    public void setOnCallBackListener(OnCallBackListener listener) {
        mCallBackListener = listener;
    }

    public void onEditProfileSuccess() {
        hideBtnSave();
        showBtnEdit();
        mName.setEnabled(false);
        mPhone.setEnabled(false);
        mAddress.setEnabled(false);
        AndroidUtilities.showToast("Your profile is saved");
    }

    public void onEditProfileError(String error) {
        AndroidUtilities.showToast(error);
    }

    @OnClick(R.id.btn_edit)
    public void onBtnEditClick() {
        hideBtnEdit();
        showBtnSave();
        mName.setEnabled(true);
        mPhone.setEnabled(true);
        mAddress.setEnabled(true);
    }

    @OnClick(R.id.btn_save)
    public void onBtnSaveClick() {
        String name = String.valueOf(mName.getText());
        String phone = String.valueOf(mPhone.getText());
        String address = String.valueOf(mAddress.getText());
        if (mCallBackListener != null) {
            mCallBackListener.onEditProfile(name, phone, address);
        }
    }

    /**
     * {@link OnCallBackListener}
     */
    public interface OnCallBackListener {
        void onEditProfile(String name, String phone, String address);
    }
}

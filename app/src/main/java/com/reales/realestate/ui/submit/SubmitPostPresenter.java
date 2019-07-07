package com.reales.realestate.ui.submit;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.reales.realestate.EstateApplication;
import com.reales.realestate.R;
import com.reales.realestate.UserManager;
import com.reales.realestate.data.model.EstateDetail;
import com.reales.realestate.data.remote.ServiceProvider;
import com.reales.realestate.data.remote.response.SubmitEstateResponse;
import com.reales.realestate.data.remote.rest.EstateService;
import com.reales.realestate.data.remote.rest.SchedulerProvider;
import com.reales.realestate.data.remote.rest.SimpleResponse;
import com.reales.realestate.ui.BasePresenter;
import com.reales.realestate.utils.AndroidUtilities;
import com.reales.realestate.utils.NetworkUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.reales.realestate.EstateApplication.BEARER_TOKEN;

class SubmitPostPresenter extends BasePresenter<SubmitPostView> {

    private List<Uri> mUriList = new ArrayList<>();

    private boolean[] mImageUploadedList;

    private String[] mUrlList;

    private String[] mPublicIdList;

    private final Object mLock = new Object();

    private int mThreadCount = 0;

    private EstateService mService = ServiceProvider.getEstateService();

    private Subscription mSub;

    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private String mTitle;
    private String mInvestor;
    private float mPrice;
    private String mUnit;
    private float mSquare;
    private int mType;
    private String mAddress;
    private String mDescription;
    private double mLatitude;
    private double mLongitude;
    private int mStatus;
    private String mContactName;
    private String mContactPhone;
    private String mContactEmail;
    private List<String> mUrls = new ArrayList<>();
    private List<String> mPublicIds = new ArrayList<>();
    private int mSubmitType;
    private EstateDetail mEditEstate;

    public SubmitPostPresenter(int submitType, EstateDetail detail) {
        mSubmitType = submitType;
        mEditEstate = detail;
        if (submitType == SubmitPostActivity.SUBMIT_TYPE_EDIT) {
            mUrls.addAll(detail.getUrl());
            mPublicIds.addAll(detail.getPublicId());
        }
    }

    public void submit(String title, String investor, float price, String unit, float square, int type,
                       String address, String description, double latitude, double longitude,
                       int status, String contactName, String contactPhone, String contactEmail) {
        mTitle = title;
        mInvestor = investor;
        mPrice = price;
        mUnit = unit;
        mSquare = square;
        mType = type;
        mAddress = address;
        mDescription = description;
        mLatitude = latitude;
        mLongitude = longitude;
        mStatus = status;
        mContactName = contactName;
        mContactPhone = contactPhone;
        mContactEmail = contactEmail;

        if (!NetworkUtils.isNetworkConnected(EstateApplication.getInstance().getApplicationContext())) {
            showConnectionFailedLayout();
            return;
        }

        hideConnectionFailedLayout();

        uploadImage();
    }

    private void checkSubmit() {
        if (canSubmit()) {
            int urlSize = mUrls.size() + (mUrlList != null ? mUrlList.length : 0);
            String[] urls = new String[urlSize];
            String[] publicIds = new String[urlSize];
            for (int i = 0; i < urlSize; i++) {
                if (i < mUrls.size()) {
                    urls[i] = mUrls.get(i);
                    publicIds[i] = mPublicIds.get(i);
                }
                if (i >= mUrls.size() && mUrlList != null) {
                    urls[i] = mUrlList[i - mUrls.size()];
                    publicIds[i] = mPublicIdList[i - mUrls.size()];
                }
            }

            if (mSubmitType == SubmitPostActivity.SUBMIT_TYPE_NEW) {
                mSub = mService.submitEstate(BEARER_TOKEN + UserManager.getAccessToken(),
                        mTitle, mInvestor, mPrice, mUnit, mSquare, mType, mAddress, mDescription, mLatitude, mLongitude,
                        UserManager.getCurrentUser().getId(), mStatus, Calendar.getInstance().getTimeInMillis() / 1000,
                        Calendar.getInstance().getTimeInMillis() / 1000, mContactName, mContactPhone, mContactEmail,
                        UserManager.getCurrentUser().getAvatar(), urls, publicIds, null)
                        .subscribeOn(SchedulerProvider.io())
                        .observeOn(SchedulerProvider.ui())
                        .subscribe(new SubmitEstateSubscriber());
            } else {
                mSub = mService.editEstate(BEARER_TOKEN + UserManager.getAccessToken(), mEditEstate.getId(),
                        mTitle, mInvestor, mPrice, mUnit, mSquare, mAddress, mType, mDescription, mLatitude, mLongitude,
                        mEditEstate.getOwnerid(), mStatus, mEditEstate.getCreateTime(),
                        Calendar.getInstance().getTimeInMillis() / 1000, mContactName, mContactPhone, mContactEmail,
                        mEditEstate.getAvatar(), urls, publicIds, null)
                        .subscribeOn(SchedulerProvider.io())
                        .observeOn(SchedulerProvider.ui())
                        .subscribe(new EditEstateSubscriber());

                mEditEstate.setName(mTitle);
                mEditEstate.setInvestor(mInvestor);
                mEditEstate.setPrice(mPrice);
                mEditEstate.setUnit(mUnit);
                mEditEstate.setArea(mSquare);
                mEditEstate.setAddress(mAddress);
                mEditEstate.setType(mType);
                mEditEstate.setInfo(mDescription);
                mEditEstate.setLat(mLatitude);
                mEditEstate.setLong(mLongitude);
                mEditEstate.setStatusProject(mStatus);
                mEditEstate.setFullname(mContactName);
                mEditEstate.setPhone(mContactPhone);
                mEditEstate.setEmail(mContactEmail);
                mEditEstate.setUrl(new ArrayList<>(Arrays.asList(urls)));
                mEditEstate.setPublicId(new ArrayList<>(Arrays.asList(publicIds)));
            }

            mSubscriptions.add(mSub);

            Log.d("haq", "Submit");
        } else {
            Log.d("haq", "can not Submit");
        }
    }

    private boolean canSubmit() {
        if (mUriList.size() == 0 && mUrls.size() > 0) {
            return true;
        }

        for (boolean isUploadSuccess : mImageUploadedList) {
            if (!isUploadSuccess) return false;
        }
        return true;
    }

    private void uploadImage() {

        if (mUriList.size() <= 0) {
            if (mUrls.size() > 0) {
                checkSubmit();
            }
        } else {

            for (int i = 0; i < mUriList.size(); i++) {
                int finalI = i;

                if (!mImageUploadedList[finalI]) {
                    MediaManager.get()
                            .upload(mUriList.get(finalI))
                            .unsigned("dels6a22")
                            .callback(new UploadCallback() {
                                @Override
                                public void onStart(String requestId) {
                                    mImageUploadedList[finalI] = false;
                                    startUploadImage(finalI);
                                }

                                @Override
                                public void onProgress(String requestId, long bytes, long totalBytes) {
                                }

                                @Override
                                public void onSuccess(String requestId, Map resultData) {
                                    mImageUploadedList[finalI] = true;
                                    uploadSuccess(requestId, resultData, finalI);
                                }

                                @Override
                                public void onError(String requestId, ErrorInfo error) {
                                    mImageUploadedList[finalI] = false;
                                    uploadError(requestId, error, finalI);
                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {
                                }
                            })
                            .dispatch();
                } else {
                    uploadTaskFinished(true);
                }
            }
        }
    }

    private void uploadTaskFinished(boolean isSuccess) {
        synchronized (mLock) {
            mThreadCount++;
            if (mThreadCount == mUriList.size()) {
                checkSubmit();
            }
        }
    }

    private void uploadError(String requestId, ErrorInfo error, int position) {
        Log.d("haq", "Upload Error" + error.toString());
        if (isViewAttached()) {
            mView.uploadError(error, position);
        }
        uploadTaskFinished(false);
    }

    private void uploadSuccess(String requestId, Map resultData, int position) {
        Log.d("haq", "upload Success");
        Object url = resultData.get("url");
        if (url != null) {
            mUrlList[position] = url.toString();
        } else {
            mUrlList[position] = "";
        }
        Object publicId = resultData.get("public_id");
        if (publicId != null) {
            mPublicIdList[position] = publicId.toString();
        } else {
            mPublicIdList[position] = "";
        }

        mImageUploadedList[position] = true;

        if (isViewAttached()) {
            mView.uploadImageSuccess(position);
        }
        uploadTaskFinished(true);
    }

    private void updateUploadPercent(long percent, int position) {
        if (isViewAttached()) {
            mView.updateUploadPercent((int) percent, position);
        }
    }

    private void startUploadImage(int position) {
        if (isViewAttached()) {
            mView.startUploadImage(position);
        }
    }

    private void showConnectionFailedLayout() {
        if (isViewAttached()) {
            mView.showNoNetwork();
        }
    }

    private void hideConnectionFailedLayout() {
        if (isViewAttached()) {
            mView.hideNoNetwork();
        }

    }

    public void pickImageResultOK(Intent data) {
        if (data.getClipData() != null) {
            ClipData clipData = data.getClipData();
            for (int i = 0; i < clipData.getItemCount(); i++) {

                if (mUriList.size() >= 5) {
                    AndroidUtilities.showToast(AndroidUtilities.getString(R.string.image_limited));
                    break;
                }

                Uri uri = clipData.getItemAt(i).getUri();
                mUriList.add(uri);
            }
        } else if (data.getData() != null) {
            if (mUriList.size() < 5) {
                Uri imageUri = data.getData();
                mUriList.add(imageUri);
            } else {
                AndroidUtilities.showToast(AndroidUtilities.getString(R.string.image_limited));
                return;
            }
        }

        mView.setImageList(mUriList);

        mUrlList = new String[mUriList.size()];
        mPublicIdList = new String[mUriList.size()];
        mImageUploadedList = new boolean[mUriList.size()];
    }

    public void deleteImageUri(int position) {
        if (position >= 0 && position < mUriList.size()) {
            mUriList.remove(position);
        }
    }

    public void deleteImageUrl(int position) {
        mUrls.remove(position);
        mPublicIds.remove(position);
    }

    private class SubmitEstateSubscriber extends Subscriber<SubmitEstateResponse> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            AndroidUtilities.showToast("Submit Error");
            e.printStackTrace();
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showConnectionFailedLayout();
            }
        }

        @Override
        public void onNext(SubmitEstateResponse submitEstateResponse) {

            if (submitEstateResponse != null) {
                mView.onSubmitSuccess(submitEstateResponse.getEstateDetail());
            }
        }
    }

    private class EditEstateSubscriber extends Subscriber<SimpleResponse> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showConnectionFailedLayout();
            }
        }

        @Override
        public void onNext(SimpleResponse response) {

            if (response != null) {
                mView.onSubmitSuccess(mEditEstate);
            }
        }
    }
}

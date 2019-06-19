package com.example.realestate.ui.submit;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.realestate.EstateApplication;
import com.example.realestate.R;
import com.example.realestate.UserManager;
import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.response.SubmitEstateResponse;
import com.example.realestate.data.remote.rest.EstateService;
import com.example.realestate.data.remote.rest.SchedulerProvider;
import com.example.realestate.ui.BasePresenter;
import com.example.realestate.utils.AndroidUtilities;
import com.example.realestate.utils.NetworkUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.example.realestate.EstateApplication.BEARER_TOKEN;

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
        showProgressLayout();

        uploadImage();
    }

    private void checkSubmit() {
        if (canSubmit()) {
            mSub = mService.submitEstate(BEARER_TOKEN + UserManager.getAccessToken(),
                    mTitle, mInvestor, mPrice, mUnit, mSquare, mType, mAddress, mDescription, mLatitude, mLongitude,
                    UserManager.getCurrentUser().getId(), mStatus, Calendar.getInstance().getTimeInMillis() / 1000,
                    Calendar.getInstance().getTimeInMillis() / 1000, mContactName, mContactPhone, mContactEmail,
                    UserManager.getCurrentUser().getAvatar(), mUrlList, mPublicIdList, null)
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new SubmitEstateSubscriber());

            mSubscriptions.add(mSub);
            Log.d("haq", "Submit");
        } else {
            Log.d("haq", "can not Submit");
        }
    }

    private boolean canSubmit() {
        for (boolean isUploadSuccess : mImageUploadedList) {
            if (!isUploadSuccess) return false;
        }
        return true;
    }

    private void uploadImage() {

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
        Log.d("haq", "start Upload");
        if (isViewAttached()) {
            mView.startUploadImage(position);
        }
    }

    private void showConnectionFailedLayout() {

    }

    private void hideConnectionFailedLayout() {

    }

    private void showProgressLayout() {

    }

    private void hideProgressLayout() {

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

    public void deleteImage(int position) {
        if (position >= 0 && position < mUriList.size()) {
            mUriList.remove(position);
        }
    }

    private class SubmitEstateSubscriber extends Subscriber<SubmitEstateResponse> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            hideProgressLayout();
            AndroidUtilities.showToast("Submit Error");
            e.printStackTrace();
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showConnectionFailedLayout();
            }
        }

        @Override
        public void onNext(SubmitEstateResponse submitEstateResponse) {
            hideProgressLayout();

            if (submitEstateResponse != null) {
                mView.onSubmitSuccess(submitEstateResponse.getEstateDetail());
            }
        }
    }
}

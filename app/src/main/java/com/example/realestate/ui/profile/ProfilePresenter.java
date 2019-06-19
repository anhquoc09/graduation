package com.example.realestate.ui.profile;

import com.example.realestate.EstateApplication;
import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.rest.EstateService;
import com.example.realestate.data.remote.rest.SchedulerProvider;
import com.example.realestate.data.remote.rest.UserListEstateResponse;
import com.example.realestate.data.remote.rest.UserProfileEstateResponse;
import com.example.realestate.ui.BasePresenter;
import com.example.realestate.ui.widget.Paging;
import com.example.realestate.utils.NetworkUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.example.realestate.EstateApplication.BEARER_TOKEN;

public class ProfilePresenter extends BasePresenter<ProfileView> {

    public static final String TAG = ProfilePresenter.class.getSimpleName();

    private EstateService mService;

    private User mUser;

    private Subscription mProfileSub;

    private Subscription mListSub;

    private CompositeSubscription mSubscriptions;

    private Paging mPaging;

    private String mProfileId = "";

    private boolean mIsCurrentUser = false;

    public ProfilePresenter() {
        mService = ServiceProvider.getEstateService();
        mUser = UserManager.getCurrentUser();
        mPaging = new Paging();
        mSubscriptions = new CompositeSubscription();
    }

    public void getUserProfile(String id, boolean isCurrentUser) {
        mProfileId = id;
        mIsCurrentUser = isCurrentUser;
        getProfile();
        getListEstate();
    }

    public void loadMore() {
        getListEstate();
    }

    public void resetProfile() {
        mPaging.reset();
        getProfile();
        getListEstate();
    }

    private void getProfile() {
        if (mProfileSub != null && !mProfileSub.isUnsubscribed()) {
            mProfileSub.unsubscribe();
        }

        if (!NetworkUtils.isNetworkConnected(EstateApplication.getInstance().getApplicationContext())) {
            showNoNetwork();
        }

        if (!mProfileId.isEmpty()) {
            mProfileSub = mService.getProfileById(mProfileId)
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new UserProfileSubscriber());

            mSubscriptions.add(mProfileSub);
        }
    }

    private void getListEstate() {
        if (mListSub != null && !mListSub.isUnsubscribed()) {
            mListSub.unsubscribe();
        }

        if (!NetworkUtils.isNetworkConnected(EstateApplication.getInstance().getApplicationContext())) {
            showNoNetwork();
        }

        if (mIsCurrentUser) {
            mListSub = mService.getCurrentUserListEstate(BEARER_TOKEN + mUser.getAccessToken(), mPaging.getNext())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new UserListEstateSubscriber());
            mSubscriptions.add(mListSub);

        } else {
            if (!mProfileId.isEmpty()) {
                mListSub = mService.getUserListEstateById(mProfileId, mPaging.getNext())
                        .subscribeOn(SchedulerProvider.io())
                        .observeOn(SchedulerProvider.ui())
                        .subscribe(new UserListEstateSubscriber());

                mSubscriptions.add(mListSub);
            }
        }
    }

    @Override
    public void detachView() {
        mSubscriptions.unsubscribe();
        super.detachView();
    }

    private void showProgress() {
        if (isViewAttached()) {
            mView.showProgress();
        }
    }

    private void hideProgress() {
        if (isViewAttached()) {
            mView.hideProgress();
        }
    }

    private void hideListProgress() {
        if (isViewAttached()) {
            mView.hideListProgress();
        }
    }

    private void showListProgress() {
        if (isViewAttached()) {
            mView.showListProgress();
        }
    }

    private void hideNoNetwork() {
        if (isViewAttached()) {
            mView.hideNoNetwork();
        }
    }

    private void showNoNetwork() {
        if (isViewAttached()) {
            mView.showNoNetwork();
        }
    }

    public Paging getPaging() {
        return mPaging;
    }

    private class UserProfileSubscriber extends Subscriber<UserProfileEstateResponse> {

        @Override
        public void onStart() {
            hideNoNetwork();
            showProgress();
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            hideProgress();

            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showNoNetwork();
            }
        }

        @Override
        public void onNext(UserProfileEstateResponse userProfileEstateResponse) {
            hideProgress();

            if (isViewAttached()) {
                mView.onFetchProfileSuccess(userProfileEstateResponse.getInfo());
            }
        }
    }

    private class UserListEstateSubscriber extends Subscriber<UserListEstateResponse> {

        @Override
        public void onStart() {
            hideNoNetwork();
            showListProgress();
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            hideListProgress();

            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showNoNetwork();
            }
        }

        @Override
        public void onNext(UserListEstateResponse userListEstateResponse) {
            hideListProgress();

            mPaging.next();
            mPaging.setHasNext(userListEstateResponse.getCount() >= mPaging.getPageSize());

            if (isViewAttached()) {
                if (mPaging.isFirstPage()) {
                    mView.onFetchListEstateSuccess(userListEstateResponse.getEstateDetails());
                } else {
                    mView.onLoadMoreListSuccess(userListEstateResponse.getEstateDetails());
                }
            }
        }
    }
}

package com.reales.realestate.ui.profile;

import com.reales.realestate.EstateApplication;
import com.reales.realestate.User;
import com.reales.realestate.UserManager;
import com.reales.realestate.data.remote.ServiceProvider;
import com.reales.realestate.data.remote.rest.EstateService;
import com.reales.realestate.data.remote.rest.SchedulerProvider;
import com.reales.realestate.data.remote.rest.SimpleResponse;
import com.reales.realestate.data.remote.rest.UserListEstateResponse;
import com.reales.realestate.data.remote.rest.UserProfileResponse;
import com.reales.realestate.ui.BasePresenter;
import com.reales.realestate.ui.widget.Paging;
import com.reales.realestate.utils.NetworkUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.reales.realestate.EstateApplication.BEARER_TOKEN;

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

    public void submitProfile(String phone, String email, String address) {
        if (!NetworkUtils.isNetworkConnected(EstateApplication.getInstance().getApplicationContext())) {
            showNoNetwork();
            return;
        }
        showProgress();

        mProfileSub = mService.editProfile(BEARER_TOKEN + mUser.getAccessToken(),
                mUser.getId(),
                email,
                mUser.getFullname(),
                mUser.getIdentify(),
                phone,
                address,
                mUser.getAvatar(),
                mUser.getDescription())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new EditProfileSubscriber());

        mSubscriptions.add(mProfileSub);
    }

    public void deleteEstate(String estateId, int posistion) {
        if (!NetworkUtils.isNetworkConnected(EstateApplication.getInstance().getApplicationContext())) {
            showNoNetwork();
            return;
        }
        showProgress();

        mSubscriptions.add(mService.deleteEstate(BEARER_TOKEN + mUser.getAccessToken(), estateId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new DeletePostSubscriber(posistion)));
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

    private class UserProfileSubscriber extends Subscriber<UserProfileResponse> {

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
        public void onNext(UserProfileResponse userProfileResponse) {
            hideProgress();

            if (isViewAttached()) {
                mView.onFetchProfileSuccess(userProfileResponse.getInfo());
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

    private class EditProfileSubscriber extends Subscriber<SimpleResponse> {
        @Override
        public void onCompleted() {
            hideProgress();
        }

        @Override
        public void onError(Throwable e) {
            hideProgress();

            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showNoNetwork();
            }
        }

        @Override
        public void onNext(SimpleResponse response) {
            if (isViewAttached()) {
                mView.editProfileSuccess();
            }
        }
    }

    private class DeletePostSubscriber extends Subscriber<SimpleResponse> {
        private int mPosition;

        public DeletePostSubscriber(int position) {
            mPosition = position;
        }

        @Override
        public void onCompleted() {
            hideProgress();
        }

        @Override
        public void onError(Throwable e) {
            hideProgress();

            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showNoNetwork();
            }
        }

        @Override
        public void onNext(SimpleResponse response) {
            if (isViewAttached()) {
                mView.deletePostSuccess(mPosition);
            }
        }
    }
}

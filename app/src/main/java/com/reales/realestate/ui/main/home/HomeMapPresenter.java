package com.reales.realestate.ui.main.home;

import com.reales.realestate.EstateApplication;
import com.reales.realestate.User;
import com.reales.realestate.UserManager;
import com.reales.realestate.data.remote.response.SaveEstateResponse;
import com.reales.realestate.data.remote.response.UnSaveEstateResponse;
import com.reales.realestate.data.remote.ServiceProvider;
import com.reales.realestate.data.remote.response.EstateListResponse;
import com.reales.realestate.data.remote.rest.EstateService;
import com.reales.realestate.data.remote.rest.SchedulerProvider;
import com.reales.realestate.ui.BasePresenter;
import com.reales.realestate.utils.NetworkUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.reales.realestate.EstateApplication.BEARER_TOKEN;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class HomeMapPresenter extends BasePresenter<HomePagerView> {

    public static final String TAG = HomeMapPresenter.class.getSimpleName();

    public static final String RADIUS = "5";

    private EstateService mService;

    private User mUser;

    private Subscription mSub;

    private Subscription mSaveSub;

    private CompositeSubscription mSubscriptions;

    public HomeMapPresenter() {
        mUser = UserManager.getCurrentUser();
        mService = ServiceProvider.getEstateService();
        mSubscriptions = new CompositeSubscription();
    }

    public void fetchData(double latitude, double longitude) {
        if (mSub != null && !mSub.isUnsubscribed()) {
            mSub.unsubscribe();
        }

        if (!NetworkUtils.isNetworkConnected(EstateApplication.getInstance().getApplicationContext())) {
            showConnectionFailedLayout();
            return;
        }

        mSub = mService.getEstatesByPosition(RADIUS, String.valueOf(latitude), String.valueOf(longitude))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new HomeEstateListSubscriber());

        mSubscriptions.add(mSub);
    }

    @Override
    public void detachView() {
        mSubscriptions.clear();
        super.detachView();
    }

    private void showConnectionFailedLayout() {
        if (isViewAttached()) {
            mView.showNoConnection();
        }
    }

    private void hideConnectionFailedLayout() {
    }

    public void savePost(String fullName, String projectId, long createTime, int position) {
        mSaveSub = mService.savePost(BEARER_TOKEN + mUser.getAccessToken(), fullName, projectId, createTime)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new SaveEstateSubscriber(position));

        mSubscriptions.add(mSaveSub);
    }

    public void unSavePost(String projectId, int position) {
        mSaveSub = mService.unSavePost(BEARER_TOKEN + mUser.getAccessToken(), projectId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new UnSaveEstateSubscriber(position, projectId));

        mSubscriptions.add(mSaveSub);
    }

    private class HomeEstateListSubscriber extends Subscriber<EstateListResponse> {

        @Override
        public void onStart() {
            hideConnectionFailedLayout();
            if (isViewAttached()) {
                mView.showProgress();
            }
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            if (isViewAttached()) {
                mView.hideProgress();
            }

            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showConnectionFailedLayout();
            }
        }

        @Override
        public void onNext(EstateListResponse estateListResponse) {
            if (isViewAttached()) {
                mView.hideProgress();
                mView.fetchDataSuccess(estateListResponse.getEstateDetails());
            }
        }
    }

    class SaveEstateSubscriber extends Subscriber<SaveEstateResponse> {

        private int mPosition;

        public SaveEstateSubscriber(int position) {
            mPosition = position;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showConnectionFailedLayout();
            }
        }

        @Override
        public void onNext(SaveEstateResponse saveEstateResponse) {
            EstateApplication.addSavedPostId(saveEstateResponse.getSaveResult().getPost());
            if (isViewAttached()) {
                mView.saveEstateSuccess(mPosition);
            }
        }
    }

    class UnSaveEstateSubscriber extends Subscriber<UnSaveEstateResponse> {
        private int mPosition;

        private String mPostId;

        public UnSaveEstateSubscriber(int position, String postId) {
            mPosition = position;
            mPostId = postId;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showConnectionFailedLayout();
            }
        }

        @Override
        public void onNext(UnSaveEstateResponse saveEstateResponse) {
            EstateApplication.removeSavedPostId(mPostId);
            if (isViewAttached()) {
                mView.unSaveEstateSuccess(mPosition);
            }
        }
    }
}

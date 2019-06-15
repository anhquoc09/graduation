package com.example.realestate.ui.main.home;

import com.example.realestate.EstateApplication;
import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.SaveEstateResponse;
import com.example.realestate.data.model.UnSaveEstateResponse;
import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.response.EstateListResponse;
import com.example.realestate.data.remote.rest.EstateService;
import com.example.realestate.data.remote.rest.SchedulerProvider;
import com.example.realestate.ui.BasePresenter;
import com.example.realestate.utils.NetworkUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.example.realestate.EstateApplication.BEARER_TOKEN;

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

    public void saveProject(String fullName, String projectId, long createTime, int position) {
        mSaveSub = mService.saveProject(BEARER_TOKEN + mUser.getAccessToken(), fullName, projectId, createTime)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new SaveEstateSubscriber(position));

        mSubscriptions.add(mSaveSub);
    }

    public void unSaveProject(String projectId, int position) {
        mSaveSub = mService.unSaveProject(BEARER_TOKEN + mUser.getAccessToken(), projectId)
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
            EstateApplication.addSavedProjectId(saveEstateResponse.getSaveResult().getProject());
            if (isViewAttached()) {
                mView.saveEstateSuccess(mPosition);
            }
        }
    }

    class UnSaveEstateSubscriber extends Subscriber<UnSaveEstateResponse> {
        private int mPosition;

        private String mProjectId;

        public UnSaveEstateSubscriber(int position, String projectId) {
            mPosition = position;
            mProjectId = projectId;
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
            EstateApplication.removeSavedProjectId(mProjectId);
            if (isViewAttached()) {
                mView.unSaveEstateSuccess(mPosition);
            }
        }
    }
}

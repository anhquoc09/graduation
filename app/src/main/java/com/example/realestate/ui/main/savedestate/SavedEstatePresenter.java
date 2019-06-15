package com.example.realestate.ui.main.savedestate;

import com.example.realestate.EstateApplication;
import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.SavedEstateListResponse;
import com.example.realestate.data.model.UnSaveEstateResponse;
import com.example.realestate.data.remote.ServiceProvider;
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
 * @since 30/03/2019
 */

public class SavedEstatePresenter extends BasePresenter<SavedEstateView> {

    private User mUser;

    private EstateService mService;

    private Subscription mSub;

    private Subscription mSaveSub;

    private CompositeSubscription mSubscriptions;

    public SavedEstatePresenter() {
        mUser = UserManager.getCurrentUser();
        mService = ServiceProvider.getEstateService();
        mSubscriptions = new CompositeSubscription();
    }

    public void fetchData() {
        if (mSub != null && !mSub.isUnsubscribed()) {
            mSub.unsubscribe();
        }

        if (!NetworkUtils.isNetworkConnected(EstateApplication.getInstance().getApplicationContext())) {
            showNoNetwork();
            return;
        }

        mSub = mService.getSavedList(BEARER_TOKEN + mUser.getAccessToken())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new SavedEstateListSubscriber());

        mSubscriptions.add(mSub);
    }

    public void unSaveProject(String projectId, int position) {
        mSaveSub = mService.unSaveProject(BEARER_TOKEN + mUser.getAccessToken(), projectId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new UnSaveEstateSubscriber(position, projectId));

        mSubscriptions.add(mSaveSub);
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

    private class SavedEstateListSubscriber extends Subscriber<SavedEstateListResponse> {

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
        public void onNext(SavedEstateListResponse savedListEstateResponse) {
            hideProgress();

            if (isViewAttached()) {
                mView.fetchDataSuccess(savedListEstateResponse.getSavedListResult().getSavedProjects());
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
                showNoNetwork();
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

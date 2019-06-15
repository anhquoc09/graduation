package com.example.realestate.ui.main.estatedetail;

import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.SaveEstateResponse;
import com.example.realestate.data.model.UnSaveEstateResponse;
import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.rest.EstateService;
import com.example.realestate.data.remote.rest.SchedulerProvider;
import com.example.realestate.ui.BasePresenter;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.example.realestate.EstateApplication.BEARER_TOKEN;

public class EstateDetailPresenter extends BasePresenter<EstateDetailView> {

    private EstateService mService;

    private User mUser;

    private Subscription mSaveSub;

    private CompositeSubscription mSubscriptions;

    public EstateDetailPresenter() {
        mService = ServiceProvider.getEstateService();
        mUser = UserManager.getCurrentUser();
        mSubscriptions = new CompositeSubscription();
    }

    public void saveProject(String fullName, String projectId, long createTime) {
        mSaveSub = mService.saveProject(BEARER_TOKEN + mUser.getAccessToken(), fullName, projectId, createTime)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new SaveEstateSubscriber());

        mSubscriptions.add(mSaveSub);
    }

    public void unSaveProject(String projectId) {
        mSaveSub = mService.unSaveProject(BEARER_TOKEN + mUser.getAccessToken(), projectId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new UnSaveEstateSubscriber());

        mSubscriptions.add(mSaveSub);
    }

    private void showNoNetwork() {
        if (isViewAttached()) {
            mView.showNoNetworkConnection();
        }
    }

    class SaveEstateSubscriber extends Subscriber<SaveEstateResponse> {
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
        public void onNext(SaveEstateResponse saveEstateResponse) {
            if (isViewAttached()) {
                mView.saveEstateSuccess();
            }
        }
    }

    class UnSaveEstateSubscriber extends Subscriber<UnSaveEstateResponse> {
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
            if (isViewAttached()) {
                mView.unSaveEstateSuccess();
            }
        }
    }
}

package com.example.realestate.ui.estatedetail;

import com.example.realestate.EstateApplication;
import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.data.remote.response.SaveEstateResponse;
import com.example.realestate.data.remote.response.UnSaveEstateResponse;
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

    public void savePost(String fullName, String projectId, long createTime) {
        mSaveSub = mService.savePost(BEARER_TOKEN + mUser.getAccessToken(), fullName, projectId, createTime)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new SaveEstateSubscriber());

        mSubscriptions.add(mSaveSub);
    }

    public void unSavePost(String projectId) {
        mSaveSub = mService.unSavePost(BEARER_TOKEN + mUser.getAccessToken(), projectId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new UnSaveEstateSubscriber(projectId));

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
            EstateApplication.addSavedPostId(saveEstateResponse.getSaveResult().getPost());
            if (isViewAttached()) {
                mView.saveEstateSuccess();
            }
        }
    }

    class UnSaveEstateSubscriber extends Subscriber<UnSaveEstateResponse> {

        private String mPostId;

        public UnSaveEstateSubscriber(String postId) {
            mPostId = postId;
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
            EstateApplication.removeSavedPostId(mPostId);
            if (isViewAttached()) {
                mView.unSaveEstateSuccess();
            }
        }
    }
}

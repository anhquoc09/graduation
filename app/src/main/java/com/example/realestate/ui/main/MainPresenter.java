package com.example.realestate.ui.main;

import com.example.realestate.EstateApplication;
import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.data.remote.response.SavedEstateListResponse;
import com.example.realestate.data.model.SavedPost;
import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.rest.EstateService;
import com.example.realestate.data.remote.rest.SchedulerProvider;
import com.example.realestate.ui.BasePresenter;
import com.example.realestate.utils.NetworkUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.example.realestate.EstateApplication.BEARER_TOKEN;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */
public class MainPresenter extends BasePresenter<MainView> {
    public static final String TAG = MainPresenter.class.getSimpleName();

    private User mUser;

    private EstateService mEstateService;

    private Subscription mSub;

    private CompositeSubscription mSubscription;

    public MainPresenter() {
        mUser = UserManager.getCurrentUser();
        mEstateService = ServiceProvider.getEstateService();
        mSubscription = new CompositeSubscription();
    }

    public void fetchData() {
        if (mSub != null && !mSub.isUnsubscribed()) {
            mSub.unsubscribe();
        }

        if (!NetworkUtils.isNetworkConnected(EstateApplication.getInstance().getApplicationContext())) {
            showNoNetwork();
            return;
        }

        mSub = mEstateService.getSavedList(BEARER_TOKEN + mUser.getAccessToken())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new SavedEstateListSubscriber());

        mSubscription.add(mSub);
    }

    public void logout() {
        logoutFinished();
    }

    private void logoutFinished() {
        if (mView != null) {
            mView.logoutFinished();
        }
    }

    @Override
    public void detachView() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
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
            showProgress();
        }

        @Override
        public void onCompleted() {
            hideProgress();

            if (isViewAttached()) {
                mView.fetchSavedListSuccess();
            }
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
            List<SavedPost> savedPostList = savedListEstateResponse.getSavedListResult().getSavedPosts();
            for (SavedPost project : savedPostList) {
                if (project.getEstateDetail() != null) {
                    EstateApplication.addSavedPostId(project.getEstateDetail().getId());
                }
            }
        }
    }
}

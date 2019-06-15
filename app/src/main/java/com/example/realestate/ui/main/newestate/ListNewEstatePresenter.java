package com.example.realestate.ui.main.newestate;

import com.example.realestate.EstateApplication;
import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.data.model.SaveEstateResponse;
import com.example.realestate.data.model.UnSaveEstateResponse;
import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.response.EstateListResponse;
import com.example.realestate.data.remote.rest.EstateService;
import com.example.realestate.data.remote.rest.SchedulerProvider;
import com.example.realestate.data.remote.rest.UserListEstateResponse;
import com.example.realestate.ui.BasePresenter;
import com.example.realestate.ui.main.home.HomeMapPresenter;
import com.example.realestate.ui.widget.Paging;
import com.example.realestate.utils.AndroidUtilities;
import com.example.realestate.utils.NetworkUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.PriorityQueue;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static android.app.Application.getProcessName;
import static com.example.realestate.EstateApplication.BEARER_TOKEN;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class ListNewEstatePresenter extends BasePresenter<ListNewEstateView> {
    public static final String TAG = ListNewEstatePresenter.class.getSimpleName();

    private Paging mPaging = new Paging();

    private User mUser;

    private EstateService mService;

    private Subscription mSub;

    private Subscription mSaveSub;

    private CompositeSubscription mSubscriptions;

    public ListNewEstatePresenter() {
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

        mSub = mService.getListNewEstate(mPaging.getNext())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new NewEstateListSubscriber());

        mSubscriptions.add(mSub);
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

    public void reset() {
        mPaging.reset();
        fetchData();
    }

    public Paging getPaging() {
        return mPaging;
    }

    private class NewEstateListSubscriber extends Subscriber<UserListEstateResponse> {

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
        public void onNext(UserListEstateResponse userListEstateResponse) {
            hideProgress();
            mPaging.next();
            mPaging.setHasNext(userListEstateResponse.getCount() >= mPaging.getPageSize());

            if (isViewAttached()) {
                if (mPaging.isFirstPage()) {
                    mView.fetchDataSuccess(userListEstateResponse.getEstateDetails());
                } else {
                    mView.loadMoreDataSuccess(userListEstateResponse.getEstateDetails());
                }
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
                hideNoNetwork();
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

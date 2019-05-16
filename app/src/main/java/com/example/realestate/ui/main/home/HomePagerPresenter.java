package com.example.realestate.ui.main.home;

import android.widget.Toast;

import com.example.realestate.EstateApplication;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.response.EstateListResponse;
import com.example.realestate.data.remote.rest.EstateService;
import com.example.realestate.data.remote.rest.SchedulerProvider;
import com.example.realestate.ui.BasePresenter;
import com.example.realestate.utils.AndroidUtilities;
import com.example.realestate.utils.NetworkUtils;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class HomePagerPresenter extends BasePresenter<HomePagerView> {

    public static final String TAG = HomePagerPresenter.class.getSimpleName();

    public static final String RADIUS = "5";

    private EstateService mService;

    private Subscription mSub;

    private CompositeSubscription mSubscription;

    public HomePagerPresenter() {
        mService = ServiceProvider.getEstateService();
        mSubscription = new CompositeSubscription();
    }

    public void fetchData(double latitude, double longitude) {
        if (mSub != null &&     !mSub.isUnsubscribed()) {
            mSub.unsubscribe();
        }

        if (!NetworkUtils.isNetworkConnected(EstateApplication.getInstance().getApplicationContext())) {
            showConnectionFailedLayout();
        }

        mSub = mService.getEstatesByPosition(RADIUS, String.valueOf(latitude), String.valueOf(longitude))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new HomeEstateListSubscriber());

        mSubscription.add(mSub);
    }

    @Override
    public void detachView() {
        mSubscription.clear();
        super.detachView();
    }

    private void showConnectionFailedLayout() {
    }

    private class HomeEstateListSubscriber extends Subscriber<EstateListResponse> {

        @Override
        public void onStart() {
//            hideConnectionFailedLayout();
//            mPaging.setLoading(true);
        }

        @Override
        public void onCompleted() {
//            hideLoadingLayout();
//
//            mPaging.setLoading(false);
        }

        @Override
        public void onError(Throwable e) {
//            hideLoadingLayout();
            showConnectionFailedLayout();
            AndroidUtilities.showToast("fetch data error");
        }

        @Override
        public void onNext(EstateListResponse estateListResponse) {
            List<EstateDetail> estateList = estateListResponse.getEstateDetails();
            if (estateList != null) {
//                mPaging.next();
//                mPaging.setHasNext(streamList.size() >= mPaging.getPageSize());
            }

            if (isViewAttached()) {
//                if (mPaging.isFirstPage()) {
                mView.fetchDataSuccess(estateList);
//                } else {
//                    mView.appendData(streamList);
//                }
                AndroidUtilities.showToast("fetch data success");
            }
        }
    }
}

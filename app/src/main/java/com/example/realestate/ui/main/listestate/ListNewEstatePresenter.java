package com.example.realestate.ui.main.listestate;

import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.data.remote.response.EstateListResponse;
import com.example.realestate.ui.BasePresenter;
import com.example.realestate.utils.AndroidUtilities;

import java.util.List;

import rx.Subscriber;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class ListNewEstatePresenter extends BasePresenter<ListNewEstateView> {
    public static final String TAG = ListNewEstatePresenter.class.getSimpleName();

    public void fetchData() {
        mView.fetchDataSuccess(null);
    }


    private void showConnectionFailedLayout() {
    }

    private class NewEstateListSubscriber extends Subscriber<EstateListResponse> {

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
            }
        }
    }
}

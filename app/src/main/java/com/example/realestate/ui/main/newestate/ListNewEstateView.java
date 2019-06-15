package com.example.realestate.ui.main.newestate;

import com.example.realestate.data.model.EstateDetail;

import java.util.List;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public interface ListNewEstateView {
    void fetchDataSuccess(List<EstateDetail> list);

    void loadMoreDataSuccess(List<EstateDetail> estateDetails);

    void showProgress();

    void hideProgress();

    void hideNoNetwork();

    void showNoNetwork();

    void saveEstateSuccess(int position);

    void unSaveEstateSuccess(int position);
}

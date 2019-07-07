package com.reales.realestate.ui.main.home;

import com.reales.realestate.data.model.EstateDetail;

import java.util.List;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public interface HomePagerView {

    void saveEstateSuccess(int position);

    void unSaveEstateSuccess(int position);

    void showNoConnection();

    void showProgress();

    void hideProgress();

    void fetchDataSuccess(List<EstateDetail> list);
}

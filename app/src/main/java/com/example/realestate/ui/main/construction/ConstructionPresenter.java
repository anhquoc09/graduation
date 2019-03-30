package com.example.realestate.ui.main.construction;

import com.example.realestate.ui.BasePresenter;

/**
 * @author anhquoc09
 * @since 30/03/2019
 */

public class ConstructionPresenter extends BasePresenter<ConstructionView> {
    public void fetchData() {
        mView.fetchDataSuccess(null);
    }
}

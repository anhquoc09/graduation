package com.example.realestate.ui.main.savedestate;

import com.example.realestate.data.model.EstateDetail;

import java.util.List;

/**
 * @author anhquoc09
 * @since 30/03/2019
 */

public interface SavedEstateView {
    void fetchDataSuccess(List<EstateDetail> list);
}

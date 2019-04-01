package com.example.realestate.ui.main.listestate;

import com.example.realestate.data.model.EstateDetail;

import java.util.List;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public interface ListNewEstateView {
    void fetchDataSuccess(List<EstateDetail> list);
}

package com.example.realestate.ui.main.construction;

import com.example.realestate.data.model.ConstructionDetail;

import java.util.List;

/**
 * @author anhquoc09
 * @since 30/03/2019
 */

public interface ConstructionView {
    void fetchDataSuccess(List<ConstructionDetail> list);
}

package com.example.realestate.ui.main.exterior;

import com.example.realestate.data.model.ExteriorDetail;

import java.util.List;

/**
 * @author anhquoc09
 * @since 30/03/2019
 */
interface ExteriorView {
    void fetchDataSuccess(List<ExteriorDetail> list);
}

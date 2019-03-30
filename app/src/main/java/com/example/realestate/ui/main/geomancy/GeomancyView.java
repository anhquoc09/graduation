package com.example.realestate.ui.main.geomancy;

import com.example.realestate.data.model.GeomancyDetail;

import java.util.List;

/**
 * @author anhquoc09
 * @since 30/03/2019
 */

public interface GeomancyView {
    void fetchDataSuccess(List<GeomancyDetail> list);
}

package com.example.realestate.ui.main.home;

import com.example.realestate.data.model.EstateDetail;

import java.util.List;

public interface HomePagerView {
    void fetchDataSuccess(List<EstateDetail> list);
}

package com.example.realestate.ui.main.listestate;

import com.example.realestate.data.model.EstateDetail;

import java.util.List;

public interface ListEstateView {
    void fetchDataSuccess(List<EstateDetail> list);
}

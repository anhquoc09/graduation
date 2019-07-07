package com.reales.realestate.ui.main.home;

import com.reales.realestate.data.model.EstateDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quocha2
 * On 18/06/2019
 */
public class HomeMapDataFilterProvider {

    private int mStatusCode = 1;

    private int mTypeCode = 0;

    private int mPriceCode = 0;

    private final List<EstateDetail> mOriginalList = new ArrayList<>();

    public void setData(List<EstateDetail> originalList) {
        reset();
        mOriginalList.clear();

        if (!originalList.isEmpty()) {
            mOriginalList.addAll(originalList);
        }
    }

    public List<EstateDetail> filter(int statusCode, int typeCode, int priceCode) {
        if (statusCode == mStatusCode && typeCode == mTypeCode && priceCode == mPriceCode) {
            return null;
        }

        mStatusCode = statusCode;
        mTypeCode = typeCode;
        mPriceCode = priceCode;

        List<EstateDetail> filteredList = new ArrayList<>();

        for (EstateDetail estateDetail : mOriginalList) {
            if (isValid(estateDetail)) {
                filteredList.add(estateDetail);
            }
        }

        return filteredList;
    }

    private void reset() {
        mStatusCode = 1;
        mTypeCode = 0;
        mPriceCode = 0;
    }

    private boolean isValid(EstateDetail estateDetail) {
        return estateDetail.getStatusPost() == mStatusCode
                && (estateDetail.getType() == mTypeCode || mTypeCode == 0)
                && isContentedPrice(estateDetail.getPrice());
    }

    private boolean isContentedPrice(float price) {
        if (mStatusCode == 1) {
            switch (mPriceCode) {
                case 1:
                    return price <= 1000;
                case 2:
                    return 1000 < price && price <= 4000;
                case 3:
                    return 4000 < price && price <= 10000;
                case 4:
                    return 10000 < price && price <= 20000;
                case 5:
                    return 20000 < price && price <= 50000;
                case 6:
                    return 50000 < price;
                default:
                        return true;
            }
        } else {
            switch (mPriceCode) {
                case 1:
                    return price <= 2;
                case 2:
                    return 2 < price && price <= 5;
                case 3:
                    return 5 < price && price <= 10;
                case 4:
                    return 10 < price && price <= 20;
                case 5:
                    return 20 < price && price <= 50;
                case 6:
                    return 50 < price;
                default:
                    return true;
            }
        }
    }
}

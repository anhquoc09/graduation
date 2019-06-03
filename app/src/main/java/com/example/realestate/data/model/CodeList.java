package com.example.realestate.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright (C) 2019, VNG Corporation.
 * Created by quocha2
 * On 03/06/2019
 */
public class CodeList {

    @SerializedName("code")
    private String code;

    @SerializedName("sold")
    private Boolean sold;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }
}
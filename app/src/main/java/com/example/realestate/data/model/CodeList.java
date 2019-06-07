package com.example.realestate.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Copyright (C) 2019, VNG Corporation.
 * Created by quocha2
 * On 03/06/2019
 */
public class CodeList implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("code")
    private String code;

    @SerializedName("sold")
    private Boolean sold;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
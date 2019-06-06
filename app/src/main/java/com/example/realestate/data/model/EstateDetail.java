package com.example.realestate.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class EstateDetail implements Serializable {

    @SerializedName("url")
    private List<String> url = null;

    @SerializedName("publicId")
    private List<String> publicId = null;

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("investor")
    private String investor;

    @SerializedName("price")
    private Float price;

    @SerializedName("unit")
    private String unit;

    @SerializedName("area")
    private Float area;

    @SerializedName("address")
    private String address;

    @SerializedName("type")
    private Integer type;

    @SerializedName("info")
    private String info;

    @SerializedName("lat")
    private Double lat;

    @SerializedName("long")
    private Double _long;

    @SerializedName("ownerid")
    private String ownerid;

    @SerializedName("statusProject")
    private Integer statusProject;

    @SerializedName("amount")
    private Integer amount;

    @SerializedName("__v")
    private Integer v;

    @SerializedName("createTime")
    private Long createTime;

    @SerializedName("updateTime")
    private Long updateTime;

    @SerializedName("verify")
    private Boolean verify;

    @SerializedName("allowComment")
    private Boolean allowComment;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("email")
    private String email;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("phone")
    private String phone;

    @SerializedName("codelist")
    private List<CodeList> codeList = null;

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public List<String> getPublicId() {
        return publicId;
    }

    public void setPublicId(List<String> publicId) {
        this.publicId = publicId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvestor() {
        return investor;
    }

    public void setInvestor(String investor) {
        this.investor = investor;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getArea() {
        return area;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public Integer getStatusProject() {
        return statusProject;
    }

    public void setStatusProject(Integer statusProject) {
        this.statusProject = statusProject;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<CodeList> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<CodeList> codeList) {
        this.codeList = codeList;
    }
}

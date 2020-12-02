package com.tradegenie.android.tradegenieapp.Models;

public class AdvCategoryModel {

    String advertiser_name,img,url;

    public AdvCategoryModel(String advertiser_name, String img, String url) {
        this.advertiser_name = advertiser_name;
        this.img = img;
        this.url = url;
    }

    public String getAdvertiser_name() {
        return advertiser_name;
    }

    public void setAdvertiser_name(String advertiser_name) {
        this.advertiser_name = advertiser_name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

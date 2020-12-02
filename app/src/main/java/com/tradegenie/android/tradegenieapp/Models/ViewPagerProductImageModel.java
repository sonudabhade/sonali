package com.tradegenie.android.tradegenieapp.Models;

public class ViewPagerProductImageModel {

    String Pk_id,image;

    public ViewPagerProductImageModel(String pk_id, String image) {
        Pk_id = pk_id;
        this.image = image;
    }

    public String getPk_id() {
        return Pk_id;
    }

    public void setPk_id(String pk_id) {
        Pk_id = pk_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

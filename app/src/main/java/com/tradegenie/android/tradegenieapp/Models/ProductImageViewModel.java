package com.tradegenie.android.tradegenieapp.Models;

public class ProductImageViewModel {

    String pk_id,image;

    public ProductImageViewModel(String pk_id, String image) {
        this.pk_id = pk_id;
        this.image = image;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package com.tradegenie.android.tradegenieapp.Models;

public class HomePageItemModel {

    String pk_id,item_name,item_image_url;

    public HomePageItemModel(String pk_id, String item_name, String item_image_url) {
        this.pk_id = pk_id;
        this.item_name = item_name;
        this.item_image_url = item_image_url;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_image_url() {
        return item_image_url;
    }

    public void setItem_image_url(String item_image_url) {
        this.item_image_url = item_image_url;
    }
}

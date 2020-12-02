package com.tradegenie.android.tradegenieapp.Models;

public class SubCategoryModel {

    String pk_id,title,subcat_image;

    public SubCategoryModel(String pk_id, String title, String subcat_image) {
        this.pk_id = pk_id;
        this.title = title;
        this.subcat_image = subcat_image;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubcat_image() {
        return subcat_image;
    }

    public void setSubcat_image(String subcat_image) {
        this.subcat_image = subcat_image;
    }
}

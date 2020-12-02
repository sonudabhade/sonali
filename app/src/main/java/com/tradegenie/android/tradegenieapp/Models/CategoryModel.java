package com.tradegenie.android.tradegenieapp.Models;

public class CategoryModel {

    String pk_id,cat_name,cat_image;

    public CategoryModel(String pk_id, String cat_name, String cat_image) {
        this.pk_id = pk_id;
        this.cat_name = cat_name;
        this.cat_image = cat_image;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }
}

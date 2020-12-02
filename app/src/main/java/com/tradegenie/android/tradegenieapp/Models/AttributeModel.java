package com.tradegenie.android.tradegenieapp.Models;

public class AttributeModel {

    String pk_id,title;

    public AttributeModel(String pk_id, String title) {
        this.pk_id = pk_id;
        this.title = title;
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
}

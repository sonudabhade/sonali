package com.tradegenie.android.tradegenieapp.Models;

public class BusinessNatureModel {

    String pk_id,nature_business;

    public BusinessNatureModel(String pk_id, String nature_business) {
        this.pk_id = pk_id;
        this.nature_business = nature_business;
    }


    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getNature_business() {
        return nature_business;
    }

    public void setNature_business(String nature_business) {
        this.nature_business = nature_business;
    }
}

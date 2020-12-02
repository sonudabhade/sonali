package com.tradegenie.android.tradegenieapp.Models;

public class CountryListBusinessModel {

    String pk_id,country_name,country_flag;

    public CountryListBusinessModel(String pk_id, String country_name, String country_flag) {
        this.pk_id = pk_id;
        this.country_name = country_name;
        this.country_flag = country_flag;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_flag() {
        return country_flag;
    }

    public void setCountry_flag(String country_flag) {
        this.country_flag = country_flag;
    }
}

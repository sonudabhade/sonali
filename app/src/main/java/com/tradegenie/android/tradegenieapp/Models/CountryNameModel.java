package com.tradegenie.android.tradegenieapp.Models;

public class CountryNameModel {

    String pk_id,city_name,country_fkid;

    public CountryNameModel(String pk_id, String city_name, String country_fkid) {
        this.pk_id = pk_id;
        this.city_name = city_name;
        this.country_fkid = country_fkid;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountry_fkid() {
        return country_fkid;
    }

    public void setCountry_fkid(String country_fkid) {
        this.country_fkid = country_fkid;
    }
}

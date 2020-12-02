package com.tradegenie.android.tradegenieapp.Models;

import java.util.ArrayList;

public class RecommendedModel {

    String pk_id,nofoleads,grandtotal,currency,heading,days,price,status,country_name,description,noofimages,services;
    int background;
    ArrayList<TaxModel> mTaxModelArrayList=new ArrayList<>();



    public String getNofoleads() {
        return nofoleads;
    }

    public void setNofoleads(String nofoleads) {
        this.nofoleads = nofoleads;
    }

    public ArrayList<TaxModel> getmTaxModelArrayList() {
        return mTaxModelArrayList;
    }

    public void setmTaxModelArrayList(ArrayList<TaxModel> mTaxModelArrayList) {
        this.mTaxModelArrayList = mTaxModelArrayList;
    }

    public String getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(String grandtotal) {
        this.grandtotal = grandtotal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoofimages() {
        return noofimages;
    }

    public void setNoofimages(String noofimages) {
        this.noofimages = noofimages;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}

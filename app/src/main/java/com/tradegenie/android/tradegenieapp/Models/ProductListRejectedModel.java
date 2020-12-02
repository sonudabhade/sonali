package com.tradegenie.android.tradegenieapp.Models;

public class ProductListRejectedModel {

    String pk_id,currency,product_name,cost,cat_name,subcat_name,image_name,cancel_remark;

    public ProductListRejectedModel(String pk_id, String currency, String product_name, String cost, String cat_name, String subcat_name, String image_name, String cancel_remark) {
        this.pk_id = pk_id;
        this.currency = currency;
        this.product_name = product_name;
        this.cost = cost;
        this.cat_name = cat_name;
        this.subcat_name = subcat_name;
        this.image_name = image_name;
        this.cancel_remark = cancel_remark;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getSubcat_name() {
        return subcat_name;
    }

    public void setSubcat_name(String subcat_name) {
        this.subcat_name = subcat_name;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getCancel_remark() {
        return cancel_remark;
    }

    public void setCancel_remark(String cancel_remark) {
        this.cancel_remark = cancel_remark;
    }
}

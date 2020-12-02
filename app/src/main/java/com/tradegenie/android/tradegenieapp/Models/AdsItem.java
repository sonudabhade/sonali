package com.tradegenie.android.tradegenieapp.Models;

public class AdsItem {

    String pk_id,img,status,url,price,fromDate,toDate;

    public AdsItem(String pk_id, String img, String status, String url, String price, String fromDate, String toDate) {
        this.pk_id = pk_id;
        this.img = img;
        this.status = status;
        this.url = url;
        this.price = price;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}

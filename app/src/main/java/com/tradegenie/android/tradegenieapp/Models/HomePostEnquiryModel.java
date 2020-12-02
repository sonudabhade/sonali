package com.tradegenie.android.tradegenieapp.Models;

public class HomePostEnquiryModel {
    private String qty = "";
    private String enquiry_type = "";
    private String cat_image_path = "";

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getEnquiry_type() {
        return enquiry_type;
    }

    public void setEnquiry_type(String enquiry_type) {
        this.enquiry_type = enquiry_type;
    }

    public String getCat_image_path() {
        return cat_image_path;
    }

    public void setCat_image_path(String cat_image_path) {
        this.cat_image_path = cat_image_path;
    }
}

package com.tradegenie.android.tradegenieapp.Models;

public class NewLeadModel {

    String pk_id,en_currency,post_type,enquiry_type,product_name,currency,price_from,price_to,qty,created_date,fk_from_id,fk_conuntry_id,leads;

    public String getEn_currency() {
        return en_currency;
    }

    public void setEn_currency(String en_currency) {
        this.en_currency = en_currency;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getEnquiry_type() {
        return enquiry_type;
    }

    public void setEnquiry_type(String enquiry_type) {
        this.enquiry_type = enquiry_type;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice_from() {
        return price_from;
    }

    public void setPrice_from(String price_from) {
        this.price_from = price_from;
    }

    public String getPrice_to() {
        return price_to;
    }

    public void setPrice_to(String price_to) {
        this.price_to = price_to;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getFk_from_id() {
        return fk_from_id;
    }

    public void setFk_from_id(String fk_from_id) {
        this.fk_from_id = fk_from_id;
    }

    public String getFk_conuntry_id() {
        return fk_conuntry_id;
    }

    public void setFk_conuntry_id(String fk_conuntry_id) {
        this.fk_conuntry_id = fk_conuntry_id;
    }

    public String getLeads() {
        return leads;
    }

    public void setLeads(String leads) {
        this.leads = leads;
    }
}

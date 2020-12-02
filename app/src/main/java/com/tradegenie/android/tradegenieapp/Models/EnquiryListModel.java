package com.tradegenie.android.tradegenieapp.Models;

public class EnquiryListModel {

    String pk_id,attributes_type_name,seller_fkid,paid_status,userName,UA_pkey,UA_email,UA_mobile,product_name,qty,price_from,price_to,created_date,currency,descripation,lead_id,preferred,type_of_buyer;

    public String getWaiting_for_response() {
        return waiting_for_response;
    }

    public void setWaiting_for_response(String waiting_for_response) {
        this.waiting_for_response = waiting_for_response;
    }

    String waiting_for_response;

    public String getUA_pkey() {
        return UA_pkey;
    }

    public void setUA_pkey(String UA_pkey) {
        this.UA_pkey = UA_pkey;
    }

    public String getAttributes_type_name() {
        return attributes_type_name;
    }

    public void setAttributes_type_name(String attributes_type_name) {
        this.attributes_type_name = attributes_type_name;
    }

    public String getSeller_fkid() {
        return seller_fkid;
    }

    public void setSeller_fkid(String seller_fkid) {
        this.seller_fkid = seller_fkid;
    }

    public String getPaid_status() {
        return paid_status;
    }

    public void setPaid_status(String paid_status) {
        this.paid_status = paid_status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUA_email() {
        return UA_email;
    }

    public void setUA_email(String UA_email) {
        this.UA_email = UA_email;
    }

    public String getUA_mobile() {
        return UA_mobile;
    }

    public void setUA_mobile(String UA_mobile) {
        this.UA_mobile = UA_mobile;
    }

    public String getType_of_buyer() {
        return type_of_buyer;
    }

    public void setType_of_buyer(String type_of_buyer) {
        this.type_of_buyer = type_of_buyer;
    }

    public String getPreferred() {
        return preferred;
    }

    public void setPreferred(String preferred) {
        this.preferred = preferred;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getDescripation() {
        return descripation;
    }

    public void setDescripation(String descripation) {
        this.descripation = descripation;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}

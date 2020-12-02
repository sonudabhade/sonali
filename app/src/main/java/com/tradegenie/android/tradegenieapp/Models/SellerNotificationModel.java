package com.tradegenie.android.tradegenieapp.Models;

public class SellerNotificationModel {

    String pk_id,user_id,message,created_date,subject;

    public SellerNotificationModel(String pk_id, String user_id, String message, String created_date, String subject) {
        this.pk_id = pk_id;
        this.user_id = user_id;
        this.message = message;
        this.created_date = created_date;
        this.subject = subject;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}


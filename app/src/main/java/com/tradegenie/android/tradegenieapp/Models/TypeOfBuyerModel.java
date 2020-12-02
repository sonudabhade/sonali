package com.tradegenie.android.tradegenieapp.Models;

public class TypeOfBuyerModel {


    String pk_id,buyer;

    public TypeOfBuyerModel(String pk_id, String buyer) {
        this.pk_id = pk_id;
        this.buyer = buyer;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
}

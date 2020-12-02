package com.tradegenie.android.tradegenieapp.Models;

public class CurrencyModel {

    String pk_id,currency;

    public CurrencyModel(String pk_id, String currency) {
        this.pk_id = pk_id;
        this.currency = currency;
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
}

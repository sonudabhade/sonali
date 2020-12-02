package com.tradegenie.android.tradegenieapp.Models;

public class FirmListModel {

    String pk_id,legal_firm;

    public FirmListModel(String pk_id, String legal_firm) {
        this.pk_id = pk_id;
        this.legal_firm = legal_firm;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getLegal_firm() {
        return legal_firm;
    }

    public void setLegal_firm(String legal_firm) {
        this.legal_firm = legal_firm;
    }
}

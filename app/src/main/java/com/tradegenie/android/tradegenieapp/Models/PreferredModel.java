package com.tradegenie.android.tradegenieapp.Models;

public class PreferredModel {

    String pk_id,preferred;

    public PreferredModel(String pk_id, String preferred) {
        this.pk_id = pk_id;
        this.preferred = preferred;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getPreferred() {
        return preferred;
    }

    public void setPreferred(String preferred) {
        this.preferred = preferred;
    }
}

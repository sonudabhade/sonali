package com.tradegenie.android.tradegenieapp.Models;

public class UnitModel {

    String pk_id,unit;

    public UnitModel(String pk_id, String unit) {
        this.pk_id = pk_id;
        this.unit = unit;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

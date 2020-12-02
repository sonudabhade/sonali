package com.tradegenie.android.tradegenieapp.Models;

public class StateVO {

    String pk_id,class_name;
    private boolean selected;

    public StateVO(String pk_id, String class_name, boolean selected) {
        this.pk_id = pk_id;
        this.class_name = class_name;
        this.selected = selected;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

package com.tradegenie.android.tradegenieapp.Models;

public class SearchSubCatArrayModel {

    String pk_id,sub_cat_pkid,sub_catname;
    Boolean selected;

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getSub_cat_pkid() {
        return sub_cat_pkid;
    }

    public void setSub_cat_pkid(String sub_cat_pkid) {
        this.sub_cat_pkid = sub_cat_pkid;
    }

    public String getSub_catname() {
        return sub_catname;
    }

    public void setSub_catname(String sub_catname) {
        this.sub_catname = sub_catname;
    }
}

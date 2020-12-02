package com.tradegenie.android.tradegenieapp.Models;

public class ProductSpecificationModel {

    String pk_id,attribute_name,attributes_type_name,attribute_fkid,attributetype_fkid,value;

    public ProductSpecificationModel(String pk_id, String attribute_name, String attributes_type_name, String attribute_fkid, String attributetype_fkid, String value) {
        this.pk_id = pk_id;
        this.attribute_name = attribute_name;
        this.attributes_type_name = attributes_type_name;
        this.attribute_fkid = attribute_fkid;
        this.attributetype_fkid = attributetype_fkid;
        this.value = value;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public String getAttributes_type_name() {
        return attributes_type_name;
    }

    public void setAttributes_type_name(String attributes_type_name) {
        this.attributes_type_name = attributes_type_name;
    }

    public String getAttribute_fkid() {
        return attribute_fkid;
    }

    public void setAttribute_fkid(String attribute_fkid) {
        this.attribute_fkid = attribute_fkid;
    }

    public String getAttributetype_fkid() {
        return attributetype_fkid;
    }

    public void setAttributetype_fkid(String attributetype_fkid) {
        this.attributetype_fkid = attributetype_fkid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

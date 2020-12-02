package com.tradegenie.android.tradegenieapp.Models;

public class PostRequirementEnquiryModel {

    String fk_subsubcat,subsubcat_name,quntity,unit,currency,pricefrom,priceto,preferred,typeofbuyer,description;

    public String getSubsubcat_name() {
        return subsubcat_name;
    }

    public void setSubsubcat_name(String subsubcat_name) {
        this.subsubcat_name = subsubcat_name;
    }

    public String getFk_subsubcat() {
        return fk_subsubcat;
    }

    public void setFk_subsubcat(String fk_subsubcat) {
        this.fk_subsubcat = fk_subsubcat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuntity() {
        return quntity;
    }

    public void setQuntity(String quntity) {
        this.quntity = quntity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPricefrom() {
        return pricefrom;
    }

    public void setPricefrom(String pricefrom) {
        this.pricefrom = pricefrom;
    }

    public String getPriceto() {
        return priceto;
    }

    public void setPriceto(String priceto) {
        this.priceto = priceto;
    }

    public String getPreferred() {
        return preferred;
    }

    public void setPreferred(String preferred) {
        this.preferred = preferred;
    }

    public String getTypeofbuyer() {
        return typeofbuyer;
    }

    public void setTypeofbuyer(String typeofbuyer) {
        this.typeofbuyer = typeofbuyer;
    }
}

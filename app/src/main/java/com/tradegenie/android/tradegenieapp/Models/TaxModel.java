package com.tradegenie.android.tradegenieapp.Models;

public class TaxModel {

    String title,tax,fk_country_id,tax_amount,currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getFk_country_id() {
        return fk_country_id;
    }

    public void setFk_country_id(String fk_country_id) {
        this.fk_country_id = fk_country_id;
    }

    public String getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }
}

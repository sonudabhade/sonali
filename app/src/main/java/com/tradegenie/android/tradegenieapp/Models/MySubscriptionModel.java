package com.tradegenie.android.tradegenieapp.Models;

public class MySubscriptionModel {

    String status,used_total_leads,nofoleads,fk_sid,currency,lead_used, pk_id, heading, days, price, description, noofimages,start_date,end_date,services;
    int background;
    String pay_type_status = "";
    String paid_status = "";
    String invoice_url = "";

    public String getInvoice_url() {
        return invoice_url;
    }

    public void setInvoice_url(String invoice_url) {
        this.invoice_url = invoice_url;
    }

    public String getPay_type_status() {
        return pay_type_status;
    }

    public void setPay_type_status(String pay_type_status) {
        this.pay_type_status = pay_type_status;
    }

    public String getPaid_status() {
        return paid_status;
    }

    public void setPaid_status(String paid_status) {
        this.paid_status = paid_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsed_total_leads() {
        return used_total_leads;
    }

    public void setUsed_total_leads(String used_total_leads) {
        this.used_total_leads = used_total_leads;
    }

    public String getFk_sid() {
        return fk_sid;
    }

    public void setFk_sid(String fk_sid) {
        this.fk_sid = fk_sid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLead_used() {
        return lead_used;
    }

    public void setLead_used(String lead_used) {
        this.lead_used = lead_used;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getNofoleads() {
        return nofoleads;
    }

    public void setNofoleads(String nofoleads) {
        this.nofoleads = nofoleads;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoofimages() {
        return noofimages;
    }

    public void setNoofimages(String noofimages) {
        this.noofimages = noofimages;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}

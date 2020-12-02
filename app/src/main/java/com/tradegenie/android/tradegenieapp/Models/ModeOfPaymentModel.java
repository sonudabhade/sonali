package com.tradegenie.android.tradegenieapp.Models;

public class ModeOfPaymentModel {

    String pk_id,mode_of_payment;

    public ModeOfPaymentModel(String pk_id, String mode_of_payment) {
        this.pk_id = pk_id;
        this.mode_of_payment = mode_of_payment;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getMode_of_payment() {
        return mode_of_payment;
    }

    public void setMode_of_payment(String mode_of_payment) {
        this.mode_of_payment = mode_of_payment;
    }
}

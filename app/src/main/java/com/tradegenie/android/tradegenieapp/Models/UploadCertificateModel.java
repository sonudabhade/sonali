package com.tradegenie.android.tradegenieapp.Models;

public class UploadCertificateModel {


        String pk_id,certificate_name;

    public UploadCertificateModel(String pk_id, String certificate_name) {
        this.pk_id = pk_id;
        this.certificate_name = certificate_name;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getCertificate_name() {
        return certificate_name;
    }

    public void setCertificate_name(String certificate_name) {
        this.certificate_name = certificate_name;
    }
}

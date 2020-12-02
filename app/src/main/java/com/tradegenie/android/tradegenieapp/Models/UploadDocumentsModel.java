package com.tradegenie.android.tradegenieapp.Models;

public class UploadDocumentsModel {


    String pk_id,documents_name;

    public UploadDocumentsModel(String pk_id, String documents_name) {
        this.pk_id = pk_id;
        this.documents_name = documents_name;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getDocuments_name() {
        return documents_name;
    }

    public void setDocuments_name(String documents_name) {
        this.documents_name = documents_name;
    }


}

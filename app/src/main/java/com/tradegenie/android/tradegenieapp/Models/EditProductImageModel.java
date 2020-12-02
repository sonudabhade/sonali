package com.tradegenie.android.tradegenieapp.Models;

import android.graphics.Bitmap;

public class EditProductImageModel {

    String pk_id,image_name;
    Bitmap documentBitmap;
    String name;

    public EditProductImageModel(String pk_id, String image_name, Bitmap documentBitmap, String name) {
        this.pk_id = pk_id;
        this.image_name = image_name;
        this.documentBitmap = documentBitmap;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getDocumentBitmap() {
        return documentBitmap;
    }

    public void setDocumentBitmap(Bitmap documentBitmap) {
        this.documentBitmap = documentBitmap;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}

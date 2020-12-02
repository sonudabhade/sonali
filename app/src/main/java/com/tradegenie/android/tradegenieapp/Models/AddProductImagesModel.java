package com.tradegenie.android.tradegenieapp.Models;

import android.graphics.Bitmap;

public class AddProductImagesModel {

    public Bitmap documentBitmap;

    public AddProductImagesModel(Bitmap documentBitmap) {
        this.documentBitmap = documentBitmap;
    }

    public Bitmap getDocumentBitmap() {
        return documentBitmap;
    }

    public void setDocumentBitmap(Bitmap documentBitmap) {
        this.documentBitmap = documentBitmap;
    }
}

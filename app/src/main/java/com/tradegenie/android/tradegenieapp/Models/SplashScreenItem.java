package com.tradegenie.android.tradegenieapp.Models;

public class SplashScreenItem {

    public String id;
    public int image;
    public String link;

    public SplashScreenItem(String id, int image, String link) {
        this.id = id;
        this.image = image;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

package com.tradegenie.android.tradegenieapp.Models;

public class SellerListModel {

    String UA_pkey,userName;

    public SellerListModel(String UA_pkey, String userName) {
        this.UA_pkey = UA_pkey;
        this.userName = userName;
    }

    public String getUA_pkey() {
        return UA_pkey;
    }

    public void setUA_pkey(String UA_pkey) {
        this.UA_pkey = UA_pkey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

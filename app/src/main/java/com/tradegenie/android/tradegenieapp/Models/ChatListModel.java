package com.tradegenie.android.tradegenieapp.Models;

public class ChatListModel {

    String pk_id,fk_buyer_id,fk_uid,userName,UA_Image,fk_from_id,fk_to_id,message, messageTime;

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getFk_buyer_id() {
        return fk_buyer_id;
    }

    public void setFk_buyer_id(String fk_buyer_id) {
        this.fk_buyer_id = fk_buyer_id;
    }

    public String getFk_uid() {
        return fk_uid;
    }

    public void setFk_uid(String fk_uid) {
        this.fk_uid = fk_uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUA_Image() {
        return UA_Image;
    }

    public void setUA_Image(String UA_Image) {
        this.UA_Image = UA_Image;
    }

    public String getFk_from_id() {
        return fk_from_id;
    }

    public void setFk_from_id(String fk_from_id) {
        this.fk_from_id = fk_from_id;
    }

    public String getFk_to_id() {
        return fk_to_id;
    }

    public void setFk_to_id(String fk_to_id) {
        this.fk_to_id = fk_to_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

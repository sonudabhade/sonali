package com.tradegenie.android.tradegenieapp.Models;

public class ChatMessage {

    public final static String MSG_TYPE_SENT = "MSG_TYPE_SENT";

    public final static String MSG_TYPE_RECEIVED = "MSG_TYPE_RECEIVED";

    // Message content.
    private String msgContent;
    private String priceContent;
    private String msgType;

    private String DateTime;

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public static String getMsgTypeSent() {
        return MSG_TYPE_SENT;
    }

    public static String getMsgTypeReceived() {
        return MSG_TYPE_RECEIVED;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getPriceContent() {
        return priceContent;
    }

    public void setPriceContent(String priceContent) {
        this.priceContent = priceContent;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}



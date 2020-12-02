package com.tradegenie.android.tradegenieapp.Models;

import java.io.Serializable;

/**
 * Created by kunal on 25/4/18.
 */

public class DataPart implements Serializable {

    public String filename;
    public byte[] content;
    public String type;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataPart(String filename, byte[] content) {
        this.filename = filename;
        this.content = content;
    }
}

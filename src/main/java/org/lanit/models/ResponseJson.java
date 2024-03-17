package org.lanit.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "info", "uuid", "lastUpdate" })
public class ResponseJson {

    private String lastUpdate;

    private String uuid;

    private Info info;


    public void setLastUpdate(String lastUpdate){
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdate(){
        return lastUpdate;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public String getUuid(){
        return uuid;
    }

    public void setInfo(Info info){
        this.info = info;
    }

    public Info getInfo(){
        return info;
    }
}
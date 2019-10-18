package com.integro.eggpro.model;

import java.io.Serializable;

public class OffersNotificationBody implements Serializable {

    String img,link,message;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OffersNotificationBody(String img, String link, String message, String title) {
        this.img = img;
        this.link = link;
        this.message = message;
    }



}

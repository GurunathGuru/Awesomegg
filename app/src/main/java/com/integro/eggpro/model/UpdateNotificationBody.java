package com.integro.eggpro.model;

import java.io.Serializable;

public class UpdateNotificationBody implements Serializable {

    String link;

    public UpdateNotificationBody(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

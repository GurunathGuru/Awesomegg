package com.integro.eggpro.model;

import java.io.Serializable;

public class Notifications implements Serializable {

    private String dateTime;

    private String img;

    private String link;

    private String topic;

    private String id;

    private String notificationType;

    private String title;

    private String message;

    public String getDateTime ()
    {
        return dateTime;
    }

    public void setDateTime (String dateTime)
    {
        this.dateTime = dateTime;
    }

    public String getImg ()
    {
        return img;
    }

    public void setImg (String img)
    {
        this.img = img;
    }

    public String getLink ()
    {
        return link;
    }

    public void setLink (String link)
    {
        this.link = link;
    }

    public String getTopic ()
    {
        return topic;
    }

    public void setTopic (String topic)
    {
        this.topic = topic;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getNotificationType ()
    {
        return notificationType;
    }

    public void setNotificationType (String notificationType)
    {
        this.notificationType = notificationType;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }
}

package com.fpt.hungnm.assigmentfinal;

import java.util.Date;

public class NotificationModel {
    private String title;
    private String content;
    private Date timeCreated;

    public NotificationModel(String title, String content, Date timeCreated) {
        this.title = title;
        this.content = content;
        this.timeCreated = timeCreated;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }
}

package com.integro.eggpro.model;

import java.util.Calendar;

public class CustomDate {

    private Integer month;
    private Integer date;
    private boolean selected = false;
    private Calendar calendar = Calendar.getInstance();

    public CustomDate(Integer month, Integer date) {
        this.month = month - 1;
        this.date = date;
        calendar.set(Calendar.MONTH, this.month);
        calendar.set(Calendar.DAY_OF_MONTH, date);
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDate() {
        return date;
    }

    public boolean isSelected() {
        return selected;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "CustomDate{" +
                "month=" + month +
                ", date=" + date +
                ", selected=" + selected +
                ", calendar=" + calendar +
                '}';
    }
}

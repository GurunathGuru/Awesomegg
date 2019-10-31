package com.integro.eggpro.model;

import java.io.Serializable;
import java.util.Arrays;

public class CustomCalender implements Serializable {

    private Integer month;
    private Integer[] dates;

    public CustomCalender(Integer month, Integer[] dates) {
        this.month = month;
        this.dates = dates;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer[] getDates() {
        return dates;
    }

    @Override
    public String toString() {
        return "CustomCalender{" +
                "month=" + month +
                ", dates=" + Arrays.toString(dates) +
                '}';
    }
}

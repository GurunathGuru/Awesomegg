package com.integro.eggpro.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemList implements Serializable {
    ArrayList<Items> items;

    public ItemList(ArrayList<Items> items) {
        this.items = items;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }
}
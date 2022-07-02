package com.example.findandbuy.dataStructure;

import java.util.ArrayList;

public class Store {
    Integer storeID = null;
    String storeName = null;
    String storeDescription = null;
    ArrayList<Item> items = null;

    public Store addStore(Integer storeID, String storeName, ArrayList<Item> items, String storeDescription)
    {
        this.storeID = storeID;
        this.storeName = storeName;
        this.items = items;
        this.storeDescription = storeDescription;
        return this;
    }

    public Integer getID()
    {
        return this.storeID;
    }

    public String getName()
    {
        return this.storeName;
    }

    public String getDescription()
    {
        return storeDescription;
    }

    public ArrayList<Item> getItems()
    {
        return items;
    }
}

package com.example.findandbuy.dataStructure;

import java.util.HashMap;

public class Store {
    Integer storeID = null;
    String storeName = null;
    String storeDescription = null;
    HashMap<String, Item> items = null;

    public Store addStore(Integer storeID, String storeName, HashMap<String, Item> items, String storeDescription)
    {
        this.storeID = storeID;
        this.storeName = storeName;
        this.items = items;
        this.storeDescription = storeDescription;
        return this;
    }

    public void addItem(Item item)
    {
        Integer itemID = items.size();
        items.put(String.valueOf(itemID), item);
    }

    public void changeName(String storeName)
    {
        this.storeName = storeName;
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

    public HashMap<String, Item> getItems()
    {
        return items;
    }
}

package com.example.findandbuy.dataStructure;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class Store {
    Integer storeID = null;
    String storeName = null;
    String storeDescription = null;
    HashMap<String, Item> items = null;

    public Store addStore(Integer storeID, String storeName, String storeDescription)
    {
        this.storeID = storeID;
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        return this;
    }

    public Store addStore(Integer storeID, String storeName, String storeDescription, HashMap<String, Item> items)
    {
        this.storeID = storeID;
        this.storeName = storeName;
        if (items != null)
            this.items = items;
        else
            this.items = new HashMap<>();
        this.storeDescription = storeDescription;
        return this;
    }

    public HashMap<String, Item> addItem(@NonNull Item item)
    {
        Integer itemID = item.itemID;
        items.put(String.valueOf(itemID), item);
        return getItems();
    }

    public Store changeName(String storeName)
    {
        this.storeName = storeName;
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

    public HashMap<String, Item> getItems()
    {
        return items;
    }
}

package com.example.findandbuy.dataStructure;

public class Item {
    Integer itemID = null;
    String itemName = "";
    Integer storeID = null;
    String itemCategory = "";
    Integer itemPrice = 0;
    Integer itemCount = 0;
    String itemDescription = "";

    public Item addItem(Integer itemID, String itemName, Integer storeID,
                        String itemCategory, Integer itemPrice,
                        Integer itemCount, String itemDescription) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.storeID = storeID;
        this.itemCategory = itemCategory;
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
        this.itemDescription = itemDescription;

        return this;
    }

    public Item addItem(Integer itemID, String itemName,
                        String itemCategory, Integer itemPrice,
                        Integer itemCount, String itemDescription) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
        this.itemDescription = itemDescription;

        return this;
    }

    public Item setAmount(Integer itemCount)
    {
        this.itemCount = itemCount;
        return this;
    }

    public Item setItemId(Integer itemID)
    {
        this.itemID = itemID;
        return this;
    }

    public Item setItemId(Integer itemID, Integer storeID)
    {
        this.itemID =  Integer.parseInt(String.valueOf(storeID) + String.valueOf(itemID));
        return this;
    }

    public Integer getID()
    {
        return this.itemID;
    }

    public String getName()
    {
        return this.itemName;
    }

    public Integer getStoreID()
    {
        return this.storeID;
    }

    public String getCategory()
    {
        return this.itemCategory;
    }

    public Integer getPrice()
    {
        return this.itemPrice;
    }

    public String getDescription()
    {
        return this.itemDescription;
    }

    public Integer getItemCount()
    {
        return this.itemCount;
    }
}
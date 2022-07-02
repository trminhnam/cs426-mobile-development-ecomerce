package com.example.findandbuy.dataStructure;

public class Item {
    Integer itemID = null;
    String itemName = null;
    Integer storeID = null;
    String itemCategory = null;
    Integer itemPrice = null;
    Integer itemCount = null;
    String itemDescription = null;

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
}
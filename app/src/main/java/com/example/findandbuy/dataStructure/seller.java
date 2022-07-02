package com.example.findandbuy.dataStructure;

public class seller {
    Integer sellerID = null;
    String sellerName = null;
    String sellerUsername = null;
    String sellerPassword = null;
    Store store = null;

    public seller addSeller(Integer sellerID, String sellerName,
                            String sellerUsername, String sellerPassword,
                            Store store)
    {
        this.sellerID = sellerID;
        this.sellerName = sellerName;
        this.sellerUsername = sellerUsername;
        this.sellerPassword = sellerPassword;
        this.store = store;

        return this;
    }

    public Integer getID()
    {
        return this.sellerID;
    }

    public String getName()
    {
        return this.sellerName;
    }

    public String getUsername()
    {
        return this.sellerUsername;
    }

    public String getPassword()
    {
        return this.sellerPassword;
    }

    public Store getStore()
    {
        return this.store;
    }
}

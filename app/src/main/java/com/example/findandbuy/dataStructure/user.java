package com.example.findandbuy.dataStructure;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class user {
    Integer userID = null;
    String userName = null;
    String userUsername = null;
    String userPassword = null;
    HashMap<String, Item> cart = new HashMap<>();

    public user addUser(Integer userID, String userName, String userUsername, String userPassword)
    {
        this.userID = userID;
        this.userName = userName;
        this.userUsername = userUsername;
        this.userPassword = userPassword;

        return this;
    }

    public HashMap<String, Item> addToCart(@NonNull Item item, Integer amount)
    {
        String itemId = String.valueOf(item.getID());
        item.setAmount(amount);
        cart.put(itemId, item);

        return getCart();
    }

    public HashMap<String, Item> getCart()
    {
        return cart;
    }

    public Integer getID()
    {
        return this.userID;
    }

    public String getName()
    {
        return this.userName;
    }

    public String getUsername()
    {
        return this.userUsername;
    }

    public String getPassword()
    {
        return this.userPassword;
    }
}

package com.example.findandbuy.dataStructure;

import java.util.ArrayList;

public class user {
    Integer userID = null;
    String userName = null;
    String userUsername = null;
    String userPassword = null;
    ArrayList<Item> cart = null;

    public user addUser(Integer userID, String userName, String userUsername, String userPassword)
    {
        this.userID = userID;
        this.userName = userName;
        this.userUsername = userUsername;
        this.userPassword = userPassword;

        return this;
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

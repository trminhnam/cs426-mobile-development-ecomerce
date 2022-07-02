package com.example.findandbuy;

import com.example.findandbuy.dataStructure.Item;
import com.example.findandbuy.dataStructure.Store;
import com.example.findandbuy.dataStructure.seller;
import com.example.findandbuy.dataStructure.user;

import java.util.ArrayList;

public class Database {
    private static Database INSTANCE = null;
    private final FireBase firebase;

    private Database() {
        firebase = FireBase.getInstance();
    }
    public static synchronized Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return(INSTANCE);
    }

    ArrayList<seller> sellers = new ArrayList<>();
    ArrayList<user> users = new ArrayList<>();

    public void registerSeller(String sellerName,
                               String sellerUsername, String sellerPassword,
                               Store store)
    {
        int sellerID;
        sellerID = 100000 + sellers.size();

        // TODO: check sellerUsername unique

        sellers.add(new seller().addSeller(sellerID, sellerName, sellerUsername, sellerPassword, store));
    }

    public ArrayList<user> getUsers()
    {
        return users;
    }

    public ArrayList<seller> getSellers()
    {
        return sellers;
    }

    public void registerSeller(String sellerName,
                               String sellerUsername, String sellerPassword,
                               String storeName, ArrayList<Item> items, String storeDescription)
    {
        int sellerID, storeID;
        sellerID = 100000 + sellers.size();
        storeID = sellerID;

        // TODO: check sellerUsername unique

        Store store = new Store().addStore(storeID, storeName, items, storeDescription);
        sellers.add(new seller().addSeller(sellerID, sellerName, sellerUsername, sellerPassword, store));
    }


    public void registerUser(String userName,
                             String userUsername, String userPassword)
    {
        int userID = 200000 + users.size();

        // TODO: check userUsername unique

        users.add(new user().addUser(userID, userName, userUsername, userPassword));
    }

    public void pushToFirebase()
    {
        firebase.addData(this);
    }


}

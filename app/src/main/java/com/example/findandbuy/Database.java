package com.example.findandbuy;

import com.example.findandbuy.dataStructure.Item;
import com.example.findandbuy.dataStructure.Store;
import com.example.findandbuy.dataStructure.seller;
import com.example.findandbuy.dataStructure.user;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Database {
    private static Database INSTANCE = null;
    private final FireBase firebase;
    private static String UserID = null;

    private Database() {
        firebase = FireBase.getInstance();
    }
    public static synchronized Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return(INSTANCE);
    }

    HashMap<String, seller> sellers = new HashMap<>();
    HashMap<String, user> users = new HashMap<>();

    public HashMap<String, user> getUsers()
    {
        return users;
    }

    public HashMap<String, seller> getSellers()
    {
        return sellers;
    }

    public void registerSeller(String sellerName,
                               String sellerUsername, String sellerPassword,
                               Store store)
    {
        int sellerID;
        sellerID = 100000 + sellers.size();

        // TODO: check sellerUsername unique
        sellers.put(String.valueOf(sellerID), new seller().addSeller(sellerID, sellerName, sellerUsername, sellerPassword, store));
        pushToFirebase();
    }

    public void registerSeller(String sellerName,
                               String sellerUsername, String sellerPassword,
                               String storeName, HashMap<String, Item> items, String storeDescription)
    {
        int sellerID, storeID;
        sellerID = 100000 + sellers.size();
        storeID = sellerID;

        // TODO: check sellerUsername unique

        Store store = new Store().addStore(storeID, storeName, storeDescription, items);
        sellers.put(String.valueOf(sellerID), new seller().addSeller(sellerID, sellerName, sellerUsername, sellerPassword, store));
        pushToFirebase();
    }


    public void registerUser(String userName,
                             String userUsername, String userPassword)
    {
        int userID = 200000 + users.size();

        // TODO: check userUsername unique

        users.put(String.valueOf(userID), new user().addUser(userID, userName, userUsername, userPassword));
        pushToFirebase();
    }

    public boolean loginSeller(String sellerUsername, String sellerPassword)
    {
        UserID = "10000";
        return true;
    }

    public boolean loginUser(String userUsername, String userPassword)
    {
        UserID = "20000";
        return true;
    }

    public HashMap<String, seller> getAllSellers()        // for user
    {
        return sellers;
    }

    public HashMap<String, Item> getItems(String storeID) // for each shop
    {
        return Objects.requireNonNull(sellers.get(storeID)).getStore().getItems();
    }

    public void pushToFirebase()
    {
        firebase.addData(this);
    }

    public void getDataFromFirebase() throws Exception {
        // TODO: FIX LOAD DATA FROM FIREBASE
        FieldMapper.copy(firebase.getData(), this);
    }
}

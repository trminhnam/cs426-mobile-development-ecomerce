package com.example.findandbuy;

import androidx.annotation.NonNull;

import com.example.findandbuy.dataStructure.Item;
import com.example.findandbuy.dataStructure.Store;
import com.example.findandbuy.dataStructure.seller;
import com.example.findandbuy.dataStructure.user;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class MapConverter {
    public Map<String, Object> convert_seller(@NonNull Map<String, seller> oldMap) {
        Map<String, Object> ret = new HashMap<>();
        for (String key : oldMap.keySet()) {
            ret.put(key, oldMap.get(key));
        }
        return ret;
    }

    public Map<String, Object> convert_user(@NonNull Map<String, user> oldMap) {
        Map<String, Object> ret = new HashMap<>();
        for (String key : oldMap.keySet()) {
            ret.put(key, oldMap.get(key));
        }
        return ret;
    }
}

public class Database {

    /* --------------- CODE EXAMPLE ---------------

        Database database = Database.getInstance();

        database.registerSeller("Seller APPLE STORE", "APPLESTORE", "APPLESTORE1234",
                "SamsungStore", "only Iphone in this store", null);
        database.loginSeller("APPLESTORE", "APPLESTORE1234");

        Item item1 = new Item().addItem(1002, "Iphone Note 10", "Phone", 1223, 12, "Run via Android");
        Item item2 = new Item().addItem(1003, "Iphone Note 20", "Phone", 43333, 3, "Run via Android");

        database.addItem(item1, 12);
        database.addItem(item2);

        database.registerUser("VIT CON SAU XI ", "snuck", "snuck1234");
        database.loginUser("snuck", "snuck1234");

        database.addToCart(100000, 1002, 3);
        database.addToCart(100000, 1003, 1);
     */


    private static Database INSTANCE = null;
    private final FireBase firebase;
    private static String UserID = null;
    MapConverter convert = new MapConverter();

    private Database() {
        firebase = FireBase.getInstance();
    }
    public static synchronized Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return(INSTANCE);
    }

    private final HashMap<String, seller> sellers = new HashMap<>();
    private final HashMap<String, user> users = new HashMap<>();

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
        updateFirebase("sellers");
    }

    public void registerSeller(String sellerName,
                               String sellerUsername, String sellerPassword,
                               String storeName, String storeDescription, HashMap<String, Item> items)
    {
        int sellerID, storeID;
        sellerID = 100000 + sellers.size();
        storeID = sellerID;

        // TODO: check sellerUsername unique

        Store store = new Store().addStore(storeID, storeName, storeDescription, items);
        sellers.put(String.valueOf(sellerID), new seller().addSeller(sellerID, sellerName, sellerUsername, sellerPassword, store));
        updateFirebase("sellers");
    }


    public void registerUser(String userName,
                             String userUsername, String userPassword)
    {
        int userID = 200000 + users.size();

        // TODO: check userUsername unique

        users.put(String.valueOf(userID), new user().addUser(userID, userName, userUsername, userPassword));
        updateFirebase("users");
    }

    public boolean loginSeller(String sellerUsername, String sellerPassword)
    {
        UserID = "100000";
        return true;
    }

    public boolean loginUser(String userUsername, String userPassword)
    {
        UserID = "200000";
        return true;
    }

    /*              HELPER FUNCTION FOR USER            */
    public HashMap<String, seller> getAllSellers()        // for user
    {
        return sellers;
    }

    public HashMap<String, Item> getItems(String storeID) // for each shop
    {
        return Objects.requireNonNull(sellers.get(storeID)).getStore().getItems();
    }

    public void addToCart(Item item, Integer amount)
    {

    }

    public void addToCart(Integer storeID, Integer itemID, Integer amount)
    {
        Item item = Objects.requireNonNull(sellers.get(String.valueOf(storeID)))
                .getStore().getItems()
                .get(String.valueOf(storeID) + String.valueOf(itemID));

        assert item != null;
        Objects.requireNonNull(users.get(UserID)).addToCart(item, amount);
        updateFirebase("users");
    }
    /*              END HELPER FUNCTION FOR USER              */


    /*              HELPER FUNCTION FOR SELLER                  */
    public void addItem(@NonNull Item item, Integer amount)
    {
        item = item.setAmount(amount);
        item = item.setItemId(item.getID(), Objects.requireNonNull(sellers.get(UserID)).getStore().getID());
        Objects.requireNonNull(sellers.get(UserID)).getStore().addItem(item);
        updateFirebase("sellers");
    }

    public void addItem(@NonNull Item item)
    {
        item = item.setItemId(item.getID(), Objects.requireNonNull(sellers.get(UserID)).getStore().getID());
        Objects.requireNonNull(sellers.get(UserID)).getStore().addItem(item);
        updateFirebase("sellers");
    }





    /*              END HELPER FUNCTION FOR SELLER              */


    public DatabaseReference pushToFirebase()
    {
        return firebase.getDatabaseReference();
    }

    public void updateFirebase(String role)
    {
        if (Objects.equals(role, "users"))
            pushToFirebase().child("users").updateChildren(convert.convert_user(users));
        else if (Objects.equals(role, "sellers"))
            pushToFirebase().child("sellers").updateChildren(convert.convert_seller(sellers));
        else
        {
            // TODO: raise error here
        }
    }

    public void pushToFirebase(Object obj)
    {
        firebase.addData(obj);
    }

    public void pushToFirebase(String child, Object obj)
    {
        firebase.addData(child, obj);
    }


    public void getDataFromFirebase() throws Exception
    {
        // TODO: load data from firebase
    }
}

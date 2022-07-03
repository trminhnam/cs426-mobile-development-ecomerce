package com.example.findandbuy.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findandbuy.R;
import com.example.findandbuy.adapters.SellerItemAdapter;
import com.example.findandbuy.models.Item;

import java.util.ArrayList;

public class ShopDetailsActivity extends AppCompatActivity {

    private ImageView shopIv;
    private TextView shopName, phoneNum, email, address;
    private ImageButton callButton, mapButton, backButton;

    private String shopUid;
    private ArrayList<Item> listItems = new ArrayList<Item>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        shopIv = findViewById(R.id.shopIv);
        shopName = findViewById(R.id.shopNameDetailTv);
        phoneNum = findViewById(R.id.phoneDetailTv);
        email = findViewById(R.id.emailDetailTv);
        address = findViewById(R.id.addressDetailTv);
        callButton = findViewById(R.id.callBtn);
        mapButton = findViewById(R.id.mapBtn);
        backButton = findViewById(R.id.backBtn);

        shopUid = getIntent().getStringExtra("shopUid");

        RecyclerView listItemRv = (RecyclerView) findViewById(R.id.listProductsRv);

        //Initial the fake  data
        //String itemID, String itemName, String itemCategory, String itemPrice, String itemCount, String itemDescription, String timestamp, String itemImage, String uid
        for (int i = 0; i < 10; ++i){
            Item item = new Item("1", "Cute bear", "NFT", "20", "10", "ABC", "123", "123", "2");
            listItems.add(item);
        }

        SellerItemAdapter sellerItemAdapter = new SellerItemAdapter(this, listItems);

        listItemRv.setAdapter(sellerItemAdapter);

        listItemRv.setLayoutManager(new LinearLayoutManager(this));


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to list shop
                onBackPressed();
            }
        });


    }
}
package com.example.findandbuy.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.findandbuy.R;
import com.example.findandbuy.adapters.SellerItemAdapter;
import com.example.findandbuy.models.Item;
import com.example.findandbuy.Constants;

import java.util.ArrayList;

public class ShopDetailsActivity extends AppCompatActivity {

    private ImageView shopIv;
    private TextView shopName, phoneNum, email, address;
    private ImageButton callButton, mapButton, backButton;
    private Spinner categorySpinner;

    private String shopUid;

    // TODO: Load from database
    private ArrayList<Item> listItems = new ArrayList<>();
    private ArrayList<Item> fillteredListItems = new ArrayList<>();

    private final String[] listCategories = Constants.options;


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
        categorySpinner = findViewById(R.id.categorySpinner);

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategories);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(ad);

        shopUid = getIntent().getStringExtra("shopUid");

        RecyclerView listItemRv = (RecyclerView) findViewById(R.id.listProductsRv);

        SellerItemAdapter sellerItemAdapter = new SellerItemAdapter(this, fillteredListItems, "User");

        listItemRv.setAdapter(sellerItemAdapter);

        listItemRv.setLayoutManager(new LinearLayoutManager(this));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to list shop
                onBackPressed();
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCategory = adapterView.getItemAtPosition(i).toString();
                fillteredListItems.clear();
                for (Item item : listItems) {
                    if (item.getItemCategory().equals(selectedCategory)) {
                        fillteredListItems.add(item);
                    }
                }
                sellerItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
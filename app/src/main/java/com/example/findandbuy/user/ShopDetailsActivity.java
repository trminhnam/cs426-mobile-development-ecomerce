package com.example.findandbuy.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findandbuy.R;

public class ShopDetailsActivity extends AppCompatActivity {

    private ImageView shopIv;
    private TextView shopName, phoneNum, email, address;
    private ImageButton callButton, mapButton, backButton;

    private String shopUid;
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to list shop
                onBackPressed();
            }
        });


    }
}
package com.example.findandbuy.seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.findandbuy.R;

public class SellerMainActivity extends AppCompatActivity {

    private TextView nameTextView;
    private ImageButton addProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        Bundle extras = getIntent().getExtras();
        String fullname = extras.getString("fullname");

        nameTextView = findViewById(R.id.nameTextView);
        addProductButton = findViewById(R.id.addProductButton);

        nameTextView.setText(fullname);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
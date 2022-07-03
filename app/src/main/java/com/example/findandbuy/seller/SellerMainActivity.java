package com.example.findandbuy.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.findandbuy.R;
import com.example.findandbuy.fragment.SellerItemListFragment;
import com.example.findandbuy.fragment.UserProfileFragment;
import com.example.findandbuy.navigation.BottomNavigationBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SellerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.seller_frame_container, SellerItemListFragment.class, null)
                    .commit();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.seller_navigation);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();

        layoutParams.setBehavior(new BottomNavigationBehavior());

        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                //Delete all fragment in container before switch tab
                for (Fragment fragmentInContainer : getSupportFragmentManager().getFragments()) {
                    if (fragmentInContainer != null) {
                        getSupportFragmentManager().beginTransaction().remove(fragmentInContainer).commit();
                    }
                }
                switch (item.getItemId()) {
                    case R.id.navigation_item_list:
                        Log.d("shop", "onNavigationItemSelected: Im shop");
                        if (savedInstanceState == null) {
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .add(R.id.seller_frame_container, SellerItemListFragment.class, null)
                                    .commit();
                        }
                        return true;
                    case R.id.navigation_add_item:
                        Log.d("game", "onNavigationItemSelected: Im game");
//                        if (savedInstanceState == null) {
//                            getSupportFragmentManager().beginTransaction()
//                                    .setReorderingAllowed(true)
//                                    .add(R.id.seller_frame_container, UserGameFragment.class, null)
//                                    .commit();
//                        }
                        return true;
                    case R.id.navigation_modify_items:
                        Log.d("card", "onNavigationItemSelected: Im card");
//                        if (savedInstanceState == null) {
//                            getSupportFragmentManager().beginTransaction()
//                                    .setReorderingAllowed(true)
//                                    .add(R.id.seller_frame_container, UserShoppingCartFragment.class, null)
//                                    .commit();
//                        }
                        return true;
                    case R.id.navigation_seller_profile:
                        Log.d("profile", "onNavigationItemSelected: Im profile");
                        if (savedInstanceState == null) {
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .add(R.id.seller_frame_container, UserProfileFragment.class, null)
                                    .commit();
                        }
                        return true;
                }
                return true;
            }
        });
    }



}

//package com.example.findandbuy.seller;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import com.example.findandbuy.R;
//
//public class SellerMainActivity extends AppCompatActivity {
//
//    private TextView nameTextView;
//    private ImageButton addProductButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_seller_main);
//
//        Bundle extras = getIntent().getExtras();
//        String fullname = extras.getString("fullname");
//
//        nameTextView = findViewById(R.id.shopNameTextView);
//        addProductButton = findViewById(R.id.addItemButton);
//
//        nameTextView.setText(fullname);
//
//        addProductButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SellerMainActivity.this, SellerAddItem.class));
//            }
//        });
//    }
//}
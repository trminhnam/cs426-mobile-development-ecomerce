package com.example.findandbuy.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.findandbuy.R;
import com.example.findandbuy.fragment.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.frame_container, ShopFragment.class, null)
                    .commit();
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_shop:
                        Log.d("shop", "onNavigationItemSelected: Im shop");
                        return true;
                    case R.id.navigation_game:
                        Log.d("game", "onNavigationItemSelected: Im game");
                        return true;
                    case R.id.navigation_cart:
                        Log.d("card", "onNavigationItemSelected: Im card");
                        return true;
                    case R.id.navigation_profile:
                        Log.d("profile", "onNavigationItemSelected: Im profile");
                        return true;
                }
                return true;
            }
        });
    }


}
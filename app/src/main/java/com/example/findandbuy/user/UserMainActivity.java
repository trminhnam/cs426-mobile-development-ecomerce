package com.example.findandbuy.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.findandbuy.R;
import com.example.findandbuy.fragment.UserGameFragment;
import com.example.findandbuy.fragment.UserProfileFragment;
import com.example.findandbuy.fragment.UserShopFragment;
import com.example.findandbuy.fragment.UserShoppingCartFragment;
import com.example.findandbuy.navigation.BottomNavigationBehavior;
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
                    .add(R.id.frame_container, UserShopFragment.class, null)
                    .commit();
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
                    case R.id.navigation_shop:
                        Log.d("shop", "onNavigationItemSelected: Im shop");
                        if (savedInstanceState == null) {
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .add(R.id.frame_container, UserShopFragment.class, null)
                                    .commit();
                        }
                        return true;
                    case R.id.navigation_game:
                        Log.d("game", "onNavigationItemSelected: Im game");
                        if (savedInstanceState == null) {
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .add(R.id.frame_container, UserGameFragment.class, null)
                                    .commit();
                        }
                        return true;
                    case R.id.navigation_cart:
                        Log.d("card", "onNavigationItemSelected: Im card");
                        if (savedInstanceState == null) {
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .add(R.id.frame_container, UserShoppingCartFragment.class, null)
                                    .commit();
                        }
                        return true;
                    case R.id.navigation_profile:
                        Log.d("profile", "onNavigationItemSelected: Im profile");
                        if (savedInstanceState == null) {
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .add(R.id.frame_container, UserProfileFragment.class, null)
                                    .commit();
                        }
                        return true;
                }
                return true;
            }
        });
    }



}
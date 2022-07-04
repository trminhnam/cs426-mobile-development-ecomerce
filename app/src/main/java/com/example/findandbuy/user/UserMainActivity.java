package com.example.findandbuy.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.findandbuy.R;
import com.example.findandbuy.fragment.CustomMapFragment;
import com.example.findandbuy.fragment.UserGameFragment;
import com.example.findandbuy.fragment.UserProfileFragment;
import com.example.findandbuy.fragment.UserShopFragment;
import com.example.findandbuy.fragment.UserShoppingCartFragment;
import com.example.findandbuy.navigation.BottomNavigationBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserMainActivity extends AppCompatActivity {

    // save all the fragments in this array
    private static Fragment[] fragments;
    private static BottomNavigationView navigation;
    private CoordinatorLayout.LayoutParams layoutParams;

    // initialize the fragments
    private void initFragments() {
        fragments = new Fragment[5];
        fragments[0] = null;
        fragments[1] = null;
        fragments[2] = null;
        fragments[3] = null;
        fragments[4] = null;
    }

    // get the current fragment
    private Fragment getCurrentFragment(int index) {
        if (fragments[index] == null) {
            switch (index) {
                case 0:
                    fragments[index] = new UserProfileFragment();
                    break;
                case 1:
                    fragments[index] = new UserShopFragment();
                    break;
                case 2:
                    fragments[index] = new UserGameFragment();
                    break;
                case 3:
                    fragments[index] = new UserShoppingCartFragment();
                    break;
                case 4:
                    fragments[index] = new CustomMapFragment();
                    break;
            }
        }
        return fragments[index];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        initFragments();

        // set the bottom navigation bar
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // set the default fragment
        switchFragment(1);

        // set the listener for the bottom navigation bar
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_profile:
                        switchFragment(0);
                        return true;
                    case R.id.navigation_shop:
                        switchFragment(1);
                        return true;
                    case R.id.navigation_game:
                        switchFragment(2);
                        return true;
                    case R.id.navigation_cart:
                        switchFragment(3);
                        return true;
                    case R.id.navigation_map:
                        switchFragment(4);
                        return true;
                }
                return false;
            }
        });

    }

    private void switchFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, getCurrentFragment(index));
        transaction.commit();
    }
}
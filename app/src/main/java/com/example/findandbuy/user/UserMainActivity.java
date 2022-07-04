package com.example.findandbuy.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.findandbuy.MapsActivity;
import com.example.findandbuy.R;
import com.example.findandbuy.fragment.CustomMapFragment;
import com.example.findandbuy.fragment.UserGameFragment;
import com.example.findandbuy.fragment.UserProfileFragment;
import com.example.findandbuy.fragment.UserShopFragment;
import com.example.findandbuy.fragment.UserShoppingCartFragment;
import com.example.findandbuy.navigation.BottomNavigationBehavior;
import com.google.android.gms.maps.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserMainActivity extends AppCompatActivity {
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        // load the store fragment by default
        assert toolbar != null;
        toolbar.setTitle("Shop");
        loadFragment(new UserShopFragment());
    }

    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new NavigationBarView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    toolbar.setTitle("Shop");
                    fragment = new UserShopFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_game:
                    toolbar.setTitle("My Gifts");
                    fragment = new UserGameFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_cart:
                    toolbar.setTitle("Cart");
                    fragment = new UserShoppingCartFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    fragment = new UserProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }




    /*
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
                    case R.id.navigation_map:
//                        Log.d("game", "onNavigationItemSelected: Im map");
//                        if (savedInstanceState == null) {
//                            getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.frame_container, CustomMapFragment.class, null)
//                                    .commit();
//                        }
                        startActivity(new Intent(UserMainActivity.this, MapsActivity.class));
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
     */
}